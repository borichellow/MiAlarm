package com.appunite.mialarm.model;

import com.google.common.base.Objects;

import java.io.Serializable;

public class SmallAlarm implements Serializable {

    private final int hours;
    private final int minutes;

    public SmallAlarm(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SmallAlarm)) return false;
        SmallAlarm that = (SmallAlarm) o;
        return hours == that.hours &&
                minutes == that.minutes;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hours, minutes);
    }
}
