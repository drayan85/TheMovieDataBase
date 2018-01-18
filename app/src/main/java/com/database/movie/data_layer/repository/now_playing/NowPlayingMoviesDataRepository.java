/*
 * Copyright (c) 2018 Ilanthirayan Paramanathan
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
 */package com.database.movie.data_layer.repository.now_playing;

import com.database.movie.data_layer.api.response.PaginatedMovies;
import com.database.movie.data_layer.repository.Local;
import com.database.movie.data_layer.repository.Remote;
import com.database.movie.data_layer.repository.now_playing.source.NowPlayingMoviesDataSource;
import com.database.movie.domain_layer.repository.NowPlayingMoviesRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 15th of January 2018
 */
public class NowPlayingMoviesDataRepository implements NowPlayingMoviesRepository {

    private final NowPlayingMoviesDataSource mDiskNowPlayingMoviesDataSource;
    private final NowPlayingMoviesDataSource mRemoteNowPlayingMoviesDataSource;

    @Inject
    public NowPlayingMoviesDataRepository(@Local NowPlayingMoviesDataSource mDiskNowPlayingMoviesDataSource,
                                          @Remote NowPlayingMoviesDataSource mRemoteNowPlayingMoviesDataSource) {
        this.mDiskNowPlayingMoviesDataSource = mDiskNowPlayingMoviesDataSource;
        this.mRemoteNowPlayingMoviesDataSource = mRemoteNowPlayingMoviesDataSource;
    }

    @Override
    public Observable<PaginatedMovies> getNowPlayingMovies(int current_page, int per_page, boolean isInternetAvailable) {
        if(isInternetAvailable){
            return mRemoteNowPlayingMoviesDataSource.getNowPlayingMovies(current_page, per_page);
        }else{
            return mDiskNowPlayingMoviesDataSource.getNowPlayingMovies(current_page, per_page);
        }
    }

    @Override
    public Observable<Boolean> saveNowPlayingMovies(PaginatedMovies paginatedMovies) {
        return mDiskNowPlayingMoviesDataSource.saveNowPlayingMovies(paginatedMovies);
    }
}
