package com.appunite.mialarm.helpers;

import java.util.concurrent.TimeUnit;

public class TimeHelper {

    public static Long dayTimeFromTime(long time) {
        return time / TimeUnit.DAYS.toMillis(1) * TimeUnit.DAYS.toMillis(1);
    }

    public static String timeToString(long time) {
        final long hs = time / TimeUnit.HOURS.toMillis(1);
        final long minutes = (time- TimeUnit.HOURS.toMillis(hs)) / TimeUnit.MINUTES.toMillis(1);
        return timeToString(hs, minutes);
    }

    public static String timeToString(long hs, long minutes) {
        return (hs != 0 ? hs : "0") +
                ":" +
                (minutes < 10 ? "0" + minutes : minutes);
    }
}
