package com.appunite.mialarm.dagger;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;

import com.appunite.mialarm.base.BaseFragment;

import javax.annotation.Nonnull;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {

    private BaseFragment fragment;

    public FragmentModule(BaseFragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @ForActivity
    @Nonnull
    Context provideContext() {
        return fragment.getContext();
    }

    @Provides
    Activity provideActivity() {
        return fragment.getActivity();
    }

    @Provides
    LayoutInflater provideLayoutInflater() {
        return fragment.getActivity().getLayoutInflater();
    }

    @Provides
    public FragmentManager provideFragmentManager() {
        return fragment.getChildFragmentManager();
    }
}
