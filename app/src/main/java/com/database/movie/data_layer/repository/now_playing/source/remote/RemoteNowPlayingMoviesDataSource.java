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
package com.database.movie.data_layer.repository.now_playing.source.remote;

import com.database.movie.BuildConfig;
import com.database.movie.data_layer.api.ApiServiceInterface;
import com.database.movie.data_layer.api.response.PaginatedMovies;
import com.database.movie.data_layer.repository.now_playing.source.NowPlayingMoviesDataSource;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 15th of January 2018
 */
public class RemoteNowPlayingMoviesDataSource implements NowPlayingMoviesDataSource {

    private ApiServiceInterface mApiServiceInterface;

    //prevent direct instantiation.
    private RemoteNowPlayingMoviesDataSource() {}

    public RemoteNowPlayingMoviesDataSource(ApiServiceInterface apiServiceInterface) {
        this.mApiServiceInterface = apiServiceInterface;
    }


    /**
     * Get {@link PaginatedMovies} via HTTP Request (Remote)
     *
     * @param current_page
     * @param per_page
     * @return {@link Observable}<{@link PaginatedMovies}>
     */
    @Override
    public Observable<PaginatedMovies> getNowPlayingMovies(int current_page, int per_page) {
        return mApiServiceInterface.getNowPlayingMovies(BuildConfig.API_KEY, "en-US", current_page);
    }

    @Override
    public Observable<Boolean> saveNowPlayingMovies(PaginatedMovies paginatedMovies) {
        return Observable.just(false);
    }
}
