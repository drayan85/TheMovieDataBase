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

package com.database.movie.data_layer.repository.now_playing.source.remote;

import com.database.movie.BuildConfig;
import com.database.movie.data_layer.api.ApiServiceInterface;
import com.database.movie.data_layer.api.response.PaginatedMovies;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 22nd of January 2018
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class RemoteNowPlayingMoviesDataSourceTest {

    private RemoteNowPlayingMoviesDataSource remoteNowPlayingMoviesDataSource;

    @Mock
    private ApiServiceInterface mockApiServiceInterface;
    @Mock
    private PaginatedMovies mockPaginatedMovies;

    @Before
    public void setUp(){
        remoteNowPlayingMoviesDataSource = new RemoteNowPlayingMoviesDataSource(mockApiServiceInterface);
    }

    @Test
    public void testGetNowPlayingMoviesFromRemoteRepository(){
        given(mockApiServiceInterface.getNowPlayingMovies(eq(BuildConfig.API_KEY), eq("en-US"), anyInt())).willReturn(Observable.just(mockPaginatedMovies));

        remoteNowPlayingMoviesDataSource.getNowPlayingMovies(1, 10);

        verify(mockApiServiceInterface).getNowPlayingMovies(eq(BuildConfig.API_KEY), eq("en-US"), anyInt());
    }
}