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
package com.database.movie.data_layer.repository.now_playing.source;

import com.database.movie.data_layer.api.response.PaginatedMovies;

import io.reactivex.Observable;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 15th of January 2018
 */
public interface NowPlayingMoviesDataSource {

    /**
     * Get an {@link Observable} which will emit a {@link PaginatedMovies}
     *
     * @param current_page
     * @param per_page
     * @return {@link Observable} <{@link PaginatedMovies}>
     */
    Observable<PaginatedMovies> getNowPlayingMovies(int current_page, int per_page);

    Observable<Boolean> saveNowPlayingMovies(PaginatedMovies paginatedMovies);
}
