package com.appunite.mialarm.storage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.appunite.mialarm.dagger.ForApplication;
import com.appunite.mialarm.model.Alarm;
import com.appunite.mialarm.model.SmallAlarm;
import com.appunite.rx.functions.Functions1;
import com.appunite.rx.operators.MoreOperators;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

public class UserPreferences {

    private static final String PREFERENCES_NAME = "user_preferences";
    private static final String ALARM_INFO = "alarms_info";
    private static final String SMALL_ALARM = "small_alarms";

    private final PublishSubject<Object> alarmsInfoRefresh = PublishSubject.create();
    private final PublishSubject<Object> smallAlarmsRefresh = PublishSubject.create();
    @Nonnull
    private final SharedPreferences preferences;
    @Nonnull
    private final Gson gson;

    public UserPreferences(@Nonnull @ForApplication Context context, @Nonnull Gson gson) {
        this.gson = gson;
        preferences = context.getSharedPreferences(PREFERENCES_NAME, 0);
    }

    //Small Alarms info

    @SuppressLint("CommitPrefEdits")
    public void setSmallAlarm(SmallAlarm alarm) {
        final String newJson = gson.toJson(alarm);
        preferences.edit().putString(SMALL_ALARM, newJson).commit();
        smallAlarmsRefresh.onNext(null);
    }

    @Nonnull
    public SmallAlarm getSmallAlarm() {
        final String json = preferences.getString(SMALL_ALARM, null);

        final Type type = new TypeToken<SmallAlarm>() {
        }.getType();

        final SmallAlarm smallAlarm;
        if (!Strings.isNullOrEmpty(json)) {
            smallAlarm = (gson.<SmallAlarm>fromJson(json, type));
        } else {
            smallAlarm = new SmallAlarm(0, 0);
        }

        return smallAlarm;
    }

    @Nonnull
    public Observable<SmallAlarm> getSmallAlarmObservable() {
        return Observable
                .defer(new Func0<Observable<SmallAlarm>>() {
                    @Override
                    public Observable<SmallAlarm> call() {
                        return Observable.just(getSmallAlarm())
                                .filter(Functions1.isNotNull());
                    }
                })
                .compose(MoreOperators.<SmallAlarm>refresh(smallAlarmsRefresh));
    }

    //Alarms info

    @SuppressLint("CommitPrefEdits")
    public void addAlarm(Alarm alarm) {
        final String json = preferences.getString(ALARM_INFO, null);

        final Type type = new TypeToken<Set<Alarm>>() {
        }.getType();

        final Set<Alarm> set;
        if (!Strings.isNullOrEmpty(json)) {
            set = gson.fromJson(json, type);
        } else {
            set = new HashSet<Alarm>();
        }
        set.add(alarm);

        final String newJson = gson.toJson(set);
        preferences.edit().putString(ALARM_INFO, newJson).commit();
        alarmsInfoRefresh.onNext(null);
    }

    @SuppressLint("CommitPrefEdits")
    public void removeAlarm(long alarmId) {
        final String json = preferences.getString(ALARM_INFO, null);

        final Type type = new TypeToken<List<Alarm>>() {
        }.getType();

        final List<Alarm> list;
        if (!Strings.isNullOrEmpty(json)) {
            list = gson.fromJson(json, type);
            for (Alarm alarm : list) {
                if (alarm.getId() == (alarmId)) {
                    list.remove(alarm);
                    break;
                }
            }
        } else {
            list = Lists.newArrayList();
        }

        final String newJson = gson.toJson(list);
        preferences.edit().putString(ALARM_INFO, newJson).commit();
        alarmsInfoRefresh.onNext(null);
    }

    public void editAlarm(Alarm alarm) {
        removeAlarm(alarm.getId());
        addAlarm(alarm);
    }

    @Nonnull
    private List<Alarm> getAlarmsInfo() {
        final String json = preferences.getString(ALARM_INFO, null);

        final Type type = new TypeToken<Set<Alarm>>() {
        }.getType();

        final List<Alarm> list = Lists.<Alarm>newArrayList();
        if (!Strings.isNullOrEmpty(json)) {
            list.addAll(gson.<Set<Alarm>>fromJson(json, type));
        }

        return list;
    }

    @Nullable
    private Alarm getAlarm(int id) {
        final String json = preferences.getString(ALARM_INFO, null);

        final Type type = new TypeToken<Set<Alarm>>() {
        }.getType();

        if (!Strings.isNullOrEmpty(json)) {
            Set<Alarm> set = gson.fromJson(json, type);
            for (Alarm alarm : set) {
                if (alarm.getId() == id)
                    return alarm;
            }
        }

        return null;
    }

    @Nonnull
    public Observable<List<Alarm>> getAlarmsObservable() {
        return Observable
                .defer(new Func0<Observable<List<Alarm>>>() {
                    @Override
                    public Observable<List<Alarm>> call() {
                        return Observable.just(getAlarmsInfo())
                                .filter(Functions1.isNotNull());
                    }
                })
                .compose(MoreOperators.<List<Alarm>>refresh(alarmsInfoRefresh));
    }

    @Nonnull
    public Observable<Long> getNearestAlarmsObservable() {
        return Observable
                .defer(new Func0<Observable<Long>>() {
                    @Override
                    public Observable<Long> call() {
                        return Observable.just(getAlarmsInfo())
                                .filter(Functions1.isNotNull())
                                .map(new Func1<List<Alarm>, Long>() {
                                    @Override
                                    public Long call(List<Alarm> alarms) {
                                        long nextTime = 0L;
                                        for (Alarm alarm : alarms) {
                                            if (nextTime == 0) {
                                                nextTime = alarm.getTime();
                                                continue;
                                            }
                                            if (nextTime > alarm.getTime())
                                                nextTime = alarm.getTime();

                                        }
                                        return nextTime;
                                    }
                                });
                    }
                })
                .compose(MoreOperators.<Long>refresh(alarmsInfoRefresh));
    }


}
