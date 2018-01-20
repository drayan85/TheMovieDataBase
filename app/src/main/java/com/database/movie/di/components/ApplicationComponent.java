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
package com.database.movie.di.components;

import android.content.Context;

import com.database.movie.data_layer.database.DataBaseManager;
import com.database.movie.di.modules.ApplicationModule;
import com.database.movie.domain_layer.executor.PostExecutionThread;
import com.database.movie.domain_layer.executor.ThreadExecutor;
import com.database.movie.utils.ImageLoadingHelper;
import com.database.movie.utils.SharedPreferencesHelper;
import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * A component whose lifetime is the life of the application.
 *
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 15th of January 2018
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @Named("TheMovieDBRetrofit")
    Retrofit exposeTheMovieDBRetrofit();

    DataBaseManager exposeDatabaseManager();

    SharedPreferencesHelper exposeSharedPreferencesHelper();

    Gson exposeGson();

    ImageLoadingHelper exposeImageLoadingHelper();

    ThreadExecutor exposeThreadExecutor();

    PostExecutionThread exposePostExecutionThread();

    Context exposeContext();
}
