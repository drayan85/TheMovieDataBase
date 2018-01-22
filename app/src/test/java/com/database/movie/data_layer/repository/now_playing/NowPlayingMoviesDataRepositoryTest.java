/*
 *
 *  * Copyright (c) 2018 Ilanthirayan Paramanathan Open Source Project
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *  http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.database.movie.data_layer.repository.now_playing;

import com.database.movie.data_layer.api.response.PaginatedMovies;
import com.database.movie.data_layer.repository.now_playing.source.NowPlayingMoviesDataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 22nd of January 2018
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class NowPlayingMoviesDataRepositoryTest {

    private NowPlayingMoviesDataRepository nowPlayingMoviesDataRepository;

    @Mock
    private NowPlayingMoviesDataSource mockDiskNowPlayingMoviesDataSource;
    @Mock
    private NowPlayingMoviesDataSource mockRemoteNowPlayingMoviesDataSource;
    @Mock
    private PaginatedMovies mockPaginatedMovies;

    @Before
    public void setUp(){
        nowPlayingMoviesDataRepository = new NowPlayingMoviesDataRepository(mockDiskNowPlayingMoviesDataSource, mockRemoteNowPlayingMoviesDataSource);
    }

    @Test
    public void testGetNowPlayingMoviesFromRemoteRepository(){
        given(nowPlayingMoviesDataRepository.getNowPlayingMovies(anyInt(), anyInt(), true)).willReturn(Observable.just(mockPaginatedMovies));
    }

    @Test
    public void testGetNowPlayingMoviesFromLocalRepository(){
        given(nowPlayingMoviesDataRepository.getNowPlayingMovies(anyInt(), anyInt(), false)).willReturn(Observable.just(mockPaginatedMovies));
    }

    @Test
    public void testSavePaginatedMoviesInLocalRepository(){
        given(nowPlayingMoviesDataRepository.saveNowPlayingMovies(mockPaginatedMovies)).willReturn(Observable.just(true));
    }
}