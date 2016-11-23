package com.appunite.mialarm.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.appunite.mialarm.miband.MiBandHelper;
import com.zhaoxiaodan.miband.MiBand;

import javax.inject.Inject;

public class Receiver extends BroadcastReceiver {

    public final static String PERIOD_EXTRA = "period_extra";
    private static final String FLAG_EXTRA = "iteration_extra";
    static final String LOG_TAG = "CYKA";

    @Inject
    MiBand miBand;

    @Override
    public void onReceive(Context context, Intent intent) {
        final int period = intent.getIntExtra(PERIOD_EXTRA, 5000);
        final int flag = intent.getIntExtra(FLAG_EXTRA, 1);
        Log.d(LOG_TAG, "onReceive");
        Log.d(LOG_TAG, "period = " + period);
        Log.d(LOG_TAG, "flag = " + flag);

        MiBandHelper.wakeUpNeo(miBand);

        final long nextTime = System.currentTimeMillis() + period;
        AlarmHelper.setAlarm(context, nextTime, flag, period);

        Log.d(LOG_TAG, "-------------------------");

    }

    public static Intent createIntent(Context context, String action, int period, int flag) {
        final Intent intent = new Intent(context, Receiver.class);
        intent.setAction(action);
        intent.putExtra(PERIOD_EXTRA, period);
        intent.putExtra(FLAG_EXTRA, flag);
        return intent;
    }
}