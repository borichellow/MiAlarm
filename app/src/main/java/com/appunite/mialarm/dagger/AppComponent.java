package com.appunite.mialarm.dagger;


import android.content.res.Resources;

import com.appunite.mialarm.MainApplication;
import com.appunite.rx.dagger.NetworkScheduler;
import com.appunite.rx.dagger.UiScheduler;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.zhaoxiaodan.miband.MiBand;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent extends DaoComponent {

    /**
     * There should be provided all objects used in presenters,
     * those providers come from AppModule.java
     **/

    @UiScheduler
    rx.Scheduler provideUiScheduler();

    @NetworkScheduler
    rx.Scheduler provideNetworkScheduler();

    Resources provideResources();

    Picasso providePicasso();

    Gson provideGson();

    MiBand miBand();

    /**
     * Inject methods for Activities that dont need any special parameters to pass
     */

    void inject(MainApplication mainApplication);

}
