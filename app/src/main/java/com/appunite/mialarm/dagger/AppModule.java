package com.appunite.mialarm.dagger;

import android.content.Context;
import android.content.res.Resources;

import com.appunite.mialarm.BuildConfig;
import com.appunite.mialarm.MainApplication;
import com.appunite.mialarm.storage.UserPreferences;
import com.appunite.rx.android.MyAndroidSchedulers;
import com.appunite.rx.android.NetworkObservableProviderImpl;
import com.appunite.rx.dagger.NetworkScheduler;
import com.appunite.rx.dagger.UiScheduler;
import com.appunite.rx.observables.NetworkObservableProvider;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.zhaoxiaodan.miband.MiBand;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.schedulers.Schedulers;

@Module
public class AppModule {

    private final MainApplication app;

    public AppModule(MainApplication app) {
        this.app = app;
    }

    @Provides
    @Singleton
    @ForApplication
    public Context applicationContext() {
        return app.getApplicationContext();
    }


    @Provides
    Resources provideResources(@ForApplication Context context) {
        return context.getResources();
    }

//    @Provides
//    @Singleton
//    FromAtoBService provideDefaultApiService(@Nonnull OkHttpClient client, @Nonnull Gson gson) {
//        final Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BuildConfig.RELEASE ? AppConstants.API_URL : AppConstants.API_URL_STAGING)
//                .client(client)
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        return retrofit.create(FromAtoBService.class);
//    }

    @Singleton
    @Provides
    public Cache provideCacheOrNull(@ForApplication Context context) {
        File httpCacheDir = new File(context.getCacheDir(), "cache");
        long httpCacheSize = 150 * 1024 * 1024; // 150 MiB
        return new Cache(httpCacheDir, httpCacheSize);
    }

    @Provides
    @Singleton
    public UserPreferences userPreferences(@ForApplication Context context, Gson gson) {
        return new UserPreferences(context, gson);
    }

    @Provides
    @Singleton
    public MiBand miBand(@ForApplication Context context) {
        return new MiBand(context);
    }

    @Singleton
    @Provides
    @Named("picasso")
    @Nonnull
    public OkHttpClient provideOkHttpClientPicasso(@Nonnull Cache cache) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cache(cache);

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                final Request.Builder requestBuilder = chain
                        .request()
                        .newBuilder();

//                requestBuilder.addHeader("api-user-key", AppConstants.API_USER_KEY_BOTH_APIS);

//                if (!BuildConfig.RELEASE) {
//                    requestBuilder.addHeader("Authorization", AppConstants.API_AUTHORIZATION_STAGING);
//                }

                return chain.proceed(requestBuilder.build());
            }
        });

        return builder
                .build();
    }

//    @Singleton
//    @Provides
//    @Nonnull
//    public OkHttpClient provideOkHttpClient(@Nonnull Cache cache, @Nonnull final UserPreferences userPreferences) {
//        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                .cache(cache);
//
//        builder.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                final Request.Builder requestBuilder = chain.request().newBuilder();
//
//                requestBuilder.addHeader("api-user-key", AppConstants.API_USER_KEY_BOTH_APIS);
//
//                if (userPreferences.getAuthInfoInfo() != null) {
//                    requestBuilder.addHeader("Api-Auth-Token", userPreferences.getAuthInfoInfo().getAuthToken());
//                }
//
//                if (!BuildConfig.RELEASE) {
//                    requestBuilder.addHeader("Authorization", AppConstants.API_AUTHORIZATION_STAGING);
//                }
//
//                return chain.proceed(requestBuilder.build());
//            }
//        });
//
//        if (!BuildConfig.RELEASE)
//            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
//
//        return builder
//                .build();
//    }

    @Provides
    @Singleton
    Picasso providePicasso(@ForApplication Context context, @Named("picasso") OkHttpClient okHttpClient) {
        final LruCache lruCache = new LruCache(context);
        return new Picasso.Builder(context)
                .indicatorsEnabled(BuildConfig.DEBUG)
                .memoryCache(lruCache)
                .loggingEnabled(BuildConfig.DEBUG)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();
    }

    @Singleton
    @Provides
    Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//                .registerTypeAdapter(ImmutableList.class, new ImmutableListDeserializer())
//                .registerTypeAdapter(ImmutableSet.class, new ImmutableSetDeserializer())
                .create();
    }

    @UiScheduler
    @Provides
    @Singleton
    rx.Scheduler provideUiScheduler() {
        return MyAndroidSchedulers.mainThread();
    }

    @NetworkScheduler
    @Provides
    @Singleton
    rx.Scheduler provideNetworkScheduler() {
        return Schedulers.io();
    }

    @Provides
    NetworkObservableProvider provideNetworkProvider(@ForApplication Context context) {
        return new NetworkObservableProviderImpl(context);
    }

}
