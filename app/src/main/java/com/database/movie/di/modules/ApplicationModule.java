/*
 * Copyright (c) 2018 Ilanthirayan Paramanathan Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.database.movie.di.modules;

import android.content.Context;

import com.database.movie.BuildConfig;
import com.database.movie.data_layer.api.RxErrorHandlingCallAdapterFactory;
import com.database.movie.data_layer.database.DBHelper;
import com.database.movie.data_layer.database.DataBaseManager;
import com.database.movie.data_layer.executor.JobExecutor;
import com.database.movie.domain_layer.executor.PostExecutionThread;
import com.database.movie.domain_layer.executor.ThreadExecutor;
import com.database.movie.presentation_layer.UIThread;
import com.database.movie.utils.ImageLoadingHelper;
import com.database.movie.utils.JsonFormatHttpLogging;
import com.database.movie.utils.SharedPreferencesHelper;
import com.database.movie.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Dagger module that provides objects which will live during the application lifecycle.
 *
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 15th of January 2018
 */
@Module
public class ApplicationModule {

    private String mBaseUrl;
    private Context mContext;

    private ApplicationModule() {}

    public ApplicationModule(Context context) {
        this.mContext = context;
        mBaseUrl = "https://api.themoviedb.org";
    }

    @Provides
    @Singleton
    DBHelper provideDBHelper(){
        return new DBHelper(mContext);
    }

    @Provides
    @Singleton
    DataBaseManager provideDataBaseManager(DBHelper dbHelper){
        return new DataBaseManager(dbHelper);
    }

    @Provides
    @Singleton
    SharedPreferencesHelper provideSharedPreferences(){
        return new SharedPreferencesHelper(mContext);
    }

    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor){
        return jobExecutor;
    }

    @Provides
    @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread){
        return uiThread;
    }

    @Provides
    @Singleton
    Cache provideHttpCache() {
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(mContext.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    Gson provideGson(){
        return new GsonBuilder()
                //.registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .create();
    }

    @Provides
    @Singleton
    GsonConverterFactory provideGsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    Interceptor provideHeaderInterceptor(){
        // Define the interceptor, add authentication headers
        return new Interceptor(){
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder requestBuilder = chain
                        .request()
                        .newBuilder()
                        //.addHeader("Content-Encoding", "gzip")
                        .addHeader("Accept", "application/vnd.the_movie_db.v1")
                        .addHeader("Time-Zone", TimeZone.getDefault().getID());
                requestBuilder.addHeader("Content-Type", "application/json");
                requestBuilder.addHeader("Accept-Language", Locale.getDefault().toString());
                requestBuilder.addHeader("device-type","android");
                requestBuilder.addHeader("User-Agent", Utils.getUserAgentString(mContext));
                return chain.proceed(requestBuilder.build());
                /*Response response = chain.proceed(newRequest);
                return GzipUtils.unzip(response);*/
            }
        };
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideHttpLoggingInterceptor(){
        return new HttpLoggingInterceptor(new JsonFormatHttpLogging())
                .setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Provides
    @Singleton
    @Named("the-movie-db")
    OkHttpClient provideOkHttpClient1(Cache cache,
                                      Interceptor headerInterceptor,
                                      HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpClient.Builder builder =  new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(headerInterceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS);
        if(BuildConfig.DEBUG){
            //builder.networkInterceptors().add(httpLoggingInterceptor);
            //builder.addNetworkInterceptor(httpLoggingInterceptor);
            //builder.interceptors().add(httpLoggingInterceptor);
            //builder.addNetworkInterceptor(new StethoInterceptor());
            builder.addInterceptor(httpLoggingInterceptor);
        }
        return builder.build();
    }

    @Provides
    @Singleton
    CallAdapter.Factory provideRxJavaCallAdapterFactory() {
        //return RxJavaCallAdapterFactory.create();
        //return RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());
        return RxErrorHandlingCallAdapterFactory.create();
    }

    @Provides
    @Singleton
    @Named("TheMovieDBRetrofit")
    Retrofit provideTheMovieDBRetrofit(@Named("the-movie-db") OkHttpClient client,
                                          GsonConverterFactory converterFactory,
                                          CallAdapter.Factory adapterFactory) {
        return new Retrofit.Builder()
                .baseUrl(mBaseUrl + "/")
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(adapterFactory)
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    ImageLoadingHelper provideImageLoadingHelper(){
        return new ImageLoadingHelper(mContext);
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mContext;
    }
}
