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

import com.database.movie.data_layer.database.DataBaseManager;
import com.database.movie.data_layer.repository.Local;
import com.database.movie.data_layer.repository.Remote;
import com.database.movie.data_layer.repository.now_playing.NowPlayingMoviesDataRepository;
import com.database.movie.data_layer.repository.now_playing.source.NowPlayingMoviesDataSource;
import com.database.movie.data_layer.repository.now_playing.source.disk.DiskNowPlayingMoviesDataSource;
import com.database.movie.data_layer.repository.now_playing.source.remote.RemoteNowPlayingMoviesDataSource;
import com.database.movie.di.PerActivity;
import com.database.movie.domain_layer.repository.NowPlayingMoviesRepository;
import com.google.gson.Gson;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Dagger module that provides Liked Films related collaborators.
 *
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 15th of January 2018
 */
@Module
public class NowPlayingMovieModule {

    @PerActivity
    @Provides
    @Local
    NowPlayingMoviesDataSource provideDiskNowPlayingMoviesDataSource(DataBaseManager dataBaseManager, Gson gson){
        return new DiskNowPlayingMoviesDataSource(dataBaseManager, gson);
    }

    @PerActivity
    @Provides
    @Remote
    NowPlayingMoviesDataSource provideRemoteNowPlayingMoviesDataSource(@Named("TheMovieDBRetrofit")Retrofit retrofit){
        return new RemoteNowPlayingMoviesDataSource(retrofit);
    }

    @PerActivity
    @Provides
    NowPlayingMoviesRepository provideNowPlayingMoviesRepository(NowPlayingMoviesDataRepository nowPlayingMoviesDataRepository){
        return nowPlayingMoviesDataRepository;
    }
}
