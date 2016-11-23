package com.appunite.mialarm;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.appunite.mialarm.dagger.AppComponent;
import com.appunite.mialarm.dagger.AppModule;
import com.appunite.mialarm.dagger.DaggerAppComponent;

import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;

public class MainApplication extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeDagger();
        logRxJavaErrors();
        initializeStrictMode();

    }

    private void initializeStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }
    }

    private void initializeDagger() {
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        component.inject(this);
    }

    public AppComponent getComponent() {
        return component;
    }

    private void logRxJavaErrors() {
        if (BuildConfig.DEBUG) {
            RxJavaPlugins.getInstance().registerErrorHandler(new RxJavaErrorHandler() {
                @Override
                public void handleError(Throwable e) {
                    Log.e("rxjava error handler", "error", e);
                    super.handleError(e);
                }
            });
        }
    }

    public static MainApplication getApplication(Context context) {
        return (MainApplication) context.getApplicationContext();
    }

    public static AppComponent getAppComponent(Application app) {
        if (!(app instanceof MainApplication)) {
            throw new IllegalStateException("Passed Application is not of type MainApplication!");
        } else {
            return ((MainApplication) app).component;
        }
    }

}
