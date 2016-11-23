package com.appunite.mialarm.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.appunite.mialarm.MainApplication;
import com.appunite.mialarm.R;
import com.appunite.mialarm.base.BaseActivity;
import com.appunite.mialarm.dagger.ActivityComponent;
import com.appunite.mialarm.dagger.ActivityModule;
import com.appunite.mialarm.dagger.DaggerActivityComponent;
import com.appunite.mialarm.helpers.DialogHelper;
import com.appunite.mialarm.helpers.PermissionHelper;
import com.appunite.mialarm.helpers.SnackbarHelper;
import com.appunite.mialarm.helpers.TimeHelper;
import com.appunite.mialarm.model.SmallAlarm;
import com.appunite.mialarm.helpers.AlarmHelper;
import com.jakewharton.rxbinding.view.RxView;
import com.zhaoxiaodan.miband.MiBand;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_PERMISSION_STORAGE = 0;

    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.button_set_alarm)
    View setButton;
    @BindView(R.id.button_stop_alarm)
    View cancelButton;
    @BindView(R.id.time)
    TextView timeText;
    @BindView(R.id.day_text)
    TextView dayText;

    @Inject
    MainPresenter presenter;
    @Inject
    MiBand miBand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        askPermissions();

        presenter.getAlarmSetObservable()
                .compose(this.<Long>bindToLifecycle())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long alarmTime) {
                        AlarmHelper.setAlarm(MainActivity.this, alarmTime, (int) System.currentTimeMillis());
                    }
                });

        presenter.getTimeLeftToAlarmObservable()
                .compose(this.<Long>bindToLifecycle())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long timeLeft) {
                        SnackbarHelper.showSnackBar(rootView, getString(R.string.main_time_left_text, TimeHelper.timeToString(timeLeft)));
                    }
                });

        presenter.getIsTimeTodayObservable()
                .compose(this.<Boolean>bindToLifecycle())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean isToday) {
                        dayText.setText(getString(isToday ? R.string.main_today_text : R.string.main_tomorrow_text));
                    }
                });

        presenter.getOpenTimePickerObservable()
                .compose(this.<SmallAlarm>bindToLifecycle())
                .subscribe(new Action1<SmallAlarm>() {
                    @Override
                    public void call(SmallAlarm smallAlarm) {
                        DialogHelper.showTimePickerDialog(MainActivity.this, smallAlarm.getMinutes(), smallAlarm.getHours(),
                                presenter.getTimeSelectedSubject());
                    }
                });

        presenter.getTimeTextObservable()
                .compose(this.<SmallAlarm>bindToLifecycle())
                .subscribe(new Action1<SmallAlarm>() {
                    @Override
                    public void call(SmallAlarm smallAlarm) {
                        timeText.setText(TimeHelper.timeToString(smallAlarm.getHours(), smallAlarm.getMinutes()));
                    }
                });

        RxView.clicks(timeText)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(presenter.getTimeClickSubject());

        RxView.clicks(setButton)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(presenter.getSetClickSubject());

        RxView.clicks(cancelButton)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        AlarmHelper.cancel(miBand, MainActivity.this);
                    }
                });

    }

    private void askPermissions() {
        PermissionHelper.checkPermissionsAndAsk(
                this, REQUEST_PERMISSION_STORAGE,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION});
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                SnackbarHelper.showLongSnackBar(rootView, R.string.main_permission_message);
            }
        }
    }

    @Override
    protected void initDagger() {
        final ActivityComponent component = DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule(this))
                .appComponent(MainApplication.getAppComponent(getApplication()))
                .build();
        component.inject(this);
    }

}
