package com.appunite.mialarm.dagger;

import dagger.Component;

@FragmentScope
@Component(
        dependencies = AppComponent.class,
        modules = FragmentModule.class
)
public interface FragmentComponent extends DaoComponent {

//    void inject(MainSearchFragment mainSearchFragment);

}
