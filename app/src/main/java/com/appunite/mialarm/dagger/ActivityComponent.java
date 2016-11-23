package com.appunite.mialarm.dagger;

import com.appunite.mialarm.main.MainActivity;
import com.appunite.mialarm.service.Receiver;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = ActivityModule.class
)
public interface ActivityComponent extends DaoComponent {

    void inject(MainActivity mainSearchActivity);

    void inject(Receiver receiver);

}
