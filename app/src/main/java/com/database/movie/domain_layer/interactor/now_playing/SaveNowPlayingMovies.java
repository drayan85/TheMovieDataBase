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
 */
package com.database.movie.domain_layer.interactor.now_playing;

import com.database.movie.data_layer.api.response.PaginatedMovies;
import com.database.movie.domain_layer.executor.PostExecutionThread;
import com.database.movie.domain_layer.executor.ThreadExecutor;
import com.database.movie.domain_layer.repository.NowPlayingMoviesRepository;
import com.database.movie.domain_layer.interactor.UseCase;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import io.reactivex.Observable;

/**
 *
 * This class is an implementation of {@link UseCase} that represents a use case for
 * save NowPlaying Movies in SQLite Table {@link PaginatedMovies}.
 *
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 15th of January 2018
 */
public class SaveNowPlayingMovies extends UseCase<Boolean, PaginatedMovies> {

    private NowPlayingMoviesRepository mNowPlayingMoviesRepository;

    @Inject
    public SaveNowPlayingMovies(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                NowPlayingMoviesRepository mNowPlayingMoviesRepository) {
        super(threadExecutor, postExecutionThread);
        this.mNowPlayingMoviesRepository = mNowPlayingMoviesRepository;
    }


    @Override
    public Observable<Boolean> buildUseCaseObservable(PaginatedMovies paginatedMovies){
        Preconditions.checkNotNull(paginatedMovies);
        return mNowPlayingMoviesRepository.saveNowPlayingMovies(paginatedMovies);
    }
}
