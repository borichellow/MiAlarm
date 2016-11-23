package com.appunite.mialarm.dagger;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;

import com.appunite.mialarm.base.BaseActivity;

import javax.annotation.Nonnull;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private BaseActivity activity;

    public ActivityModule(BaseActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ForActivity
    @Nonnull
    Context provideContext() {
        return activity;
    }

    @Provides
    Activity provideActivity() {
        return activity;
    }

    @Provides
    LayoutInflater provideLayoutInflater() {
        return activity.getLayoutInflater();
    }

}