package com.appunite.mialarm.main;

import com.appunite.mialarm.helpers.TimeHelper;
import com.appunite.mialarm.model.Alarm;
import com.appunite.mialarm.model.SmallAlarm;
import com.appunite.mialarm.storage.UserPreferences;
import com.appunite.rx.ObservableExtensions;
import com.appunite.rx.android.adapter.BaseAdapterItem;
import com.appunite.rx.functions.Functions2;
import com.google.common.base.Objects;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observers.Observers;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

public class MainPresenter {

    private final PublishSubject<Void> timeClickSubject = PublishSubject.create();
    private final BehaviorSubject<SmallAlarm> timeSelectedSubject;
    private final PublishSubject<Void> setClickSubject = PublishSubject.create();
    private final Observable<Long> alarmSetObservable;
    private final Observable<Boolean> isTimeTodayObservable;

    @Inject
    public MainPresenter(final @Nonnull UserPreferences userPreferences) {

        timeSelectedSubject = BehaviorSubject.create(userPreferences.getSmallAlarm());

        final Observable<Long> alarmTimeObservable = timeSelectedSubject
                .distinctUntilChanged()
                .map(new Func1<SmallAlarm, Long>() {
                    @Override
                    public Long call(SmallAlarm smallAlarm) {
                        userPreferences.setSmallAlarm(smallAlarm);

                        final Calendar calendar = Calendar.getInstance();
                        final long currentTime = calendar.getTimeInMillis();
                        final long currentDayTime = TimeHelper.dayTimeFromTime(currentTime);
                        return TimeUnit.HOURS.toMillis(smallAlarm.getHours()) +
                                TimeUnit.MINUTES.toMillis(smallAlarm.getMinutes()) +
                                currentDayTime -
                                calendar.getTimeZone().getRawOffset();
                    }
                })
                .compose(ObservableExtensions.<Long>behaviorRefCount());

        isTimeTodayObservable = alarmTimeObservable
                .map(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long alarmTime) {
                        return TimeHelper.dayTimeFromTime(alarmTime)
                                .equals(TimeHelper.dayTimeFromTime(System.currentTimeMillis()));
                    }
                });

        alarmSetObservable = setClickSubject
                .withLatestFrom(alarmTimeObservable, Functions2.<Long>secondParam())
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long alarmTime) {
                        final long currentTime = System.currentTimeMillis();
                        if (currentTime >= alarmTime)
                            return alarmTime + TimeUnit.DAYS.toMillis(1);
                        return alarmTime;
                    }
                })
                .compose(ObservableExtensions.<Long>behaviorRefCount());

    }

    Observer<Void> getTimeClickSubject() {
        return timeClickSubject;
    }

    Observer<SmallAlarm> getTimeSelectedSubject() {
        return timeSelectedSubject;
    }

    Observable<SmallAlarm> getTimeTextObservable() {
        return timeSelectedSubject;
    }

    Observable<SmallAlarm> getOpenTimePickerObservable() {
        return timeClickSubject
                .withLatestFrom(timeSelectedSubject, Functions2.<SmallAlarm>secondParam());
    }

    Observer<Void> getSetClickSubject() {
        return setClickSubject;
    }

    Observable<Long> getAlarmSetObservable() {
        return alarmSetObservable;
    }

    Observable<Long> getTimeLeftToAlarmObservable() {
        return alarmSetObservable
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long alarmTime) {
                        return alarmTime - System.currentTimeMillis();
                    }
                });
    }

    Observable<Boolean> getIsTimeTodayObservable() {
        return isTimeTodayObservable;
    }

    //For future, multiple-alarms
    public class AlarmItem implements BaseAdapterItem {

        private final Alarm alarm;

        public AlarmItem(Alarm alarm) {
            this.alarm = alarm;
        }

        public Alarm getAlarm() {
            return alarm;
        }

        public Observer<Void> getTimeClickObserver() {
            return Observers.create(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
//                    timeClickSubject.onNext(alarm);
                }
            });
        }

        @Override
        public long adapterId() {
            return alarm.getId();
        }

        @Override
        public boolean matches(@Nonnull BaseAdapterItem item) {
            return equals(item);
        }

        @Override
        public boolean same(@Nonnull BaseAdapterItem item) {
            return equals(item);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AlarmItem)) return false;
            final AlarmItem that = (AlarmItem) o;
            return Objects.equal(alarm, that.alarm);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(alarm);
        }
    }
}
