package com.appunite.mialarm.model;

import com.appunite.mialarm.helpers.TimeHelper;
import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

public class Alarm implements Serializable {

    private final long id;
    private final boolean isOn;
    private final long time;
    private final boolean isRepeatable;
    private final List<Integer> repeats;

    public Alarm(long id,boolean isOn, long time, boolean isRepeatable, @Nonnull List<Integer> repeats) {
        this.id = id;
        this.isOn = isOn;
        this.time = time;
        this.isRepeatable = isRepeatable;
        this.repeats = repeats;
    }

    public Alarm(long id, boolean isOn, long time) {
        this.id = id;
        this.isOn = isOn;
        this.time = time;
        this.isRepeatable = false;
        this.repeats = null;
    }

    public static Alarm newAlarm(long id,boolean isOn, long time, @Nonnull List<Integer> repeats) {
        return new Alarm(id, isOn, time, true, repeats);
    }

    public static Alarm newAlarm() {
        final long currentDatTime = TimeHelper.getCurrentDayTime();
        final long time = System.currentTimeMillis() > currentDatTime + TimeUnit.HOURS.toMillis(8) ?
                currentDatTime + TimeUnit.HOURS.toMillis(8 + 24) :
                currentDatTime + TimeUnit.HOURS.toMillis(8);
        return new Alarm(System.currentTimeMillis(), true, time);
    }

    public long getId() {
        return id;
    }

    public boolean isOn() {
        return isOn;
    }

    public long getTime() {
        return time;
    }

    public boolean isRepeatable() {
        return isRepeatable;
    }

    public List<Integer> getRepeats() {
        return repeats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Alarm)) return false;
        Alarm that = (Alarm) o;
        return Objects.equal(isOn, that.isOn) &&
                Objects.equal(time, that.time) &&
                Objects.equal(isRepeatable, that.isRepeatable) &&
                Objects.equal(repeats, that.repeats) &&
                Objects.equal(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, isOn, time, isRepeatable, repeats);
    }

}
