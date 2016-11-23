package com.appunite.mialarm.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.appunite.mialarm.service.Receiver;
import com.zhaoxiaodan.miband.MiBand;

public class AlarmHelper {

    private final static String ACTION = "ALARM!!!";
    private final static int DEFAULT_PERIOD = 1300;

    public static void setAlarm(Context context, long time, int flag) {
        setAlarm(context, time, flag, DEFAULT_PERIOD);
    }

    public static void setAlarm(Context context, long time, int flag, int period) {
        final Intent intent1 = Receiver.createIntent(context, ACTION, period, flag);
        final PendingIntent pIntent1 = PendingIntent.getBroadcast(context, 0, intent1, 0);
        getAlarmManager(context).set(AlarmManager.RTC_WAKEUP, time, pIntent1);
    }

    public static void cancel(MiBand miBand, Context context) {
        final Intent intent1 = Receiver.createIntent(context, ACTION, 0, 0);
        final PendingIntent pIntent1 = PendingIntent.getBroadcast(context, 0, intent1, 0);
        getAlarmManager(context).set(AlarmManager.RTC_WAKEUP, 0, pIntent1);
        getAlarmManager(context).cancel(pIntent1);
        if (miBand.getDevice() != null)
            miBand.stopVibration();
    }

    private static AlarmManager getAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }
}
