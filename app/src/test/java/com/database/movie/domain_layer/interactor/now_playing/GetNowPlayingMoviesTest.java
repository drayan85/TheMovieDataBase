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

package com.database.movie.domain_layer.interactor.now_playing;


import com.database.movie.domain_layer.executor.PostExecutionThread;
import com.database.movie.domain_layer.executor.ThreadExecutor;
import com.database.movie.domain_layer.interactor.PaginatedParams;
import com.database.movie.domain_layer.repository.NowPlayingMoviesRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 22nd of January 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class GetNowPlayingMoviesTest {

    private GetNowPlayingMovies getNowPlayingMovies;

    @Mock
    private NowPlayingMoviesRepository mockNowPlayingMoviesRepository;
    @Mock
    private ThreadExecutor mockThreadExecutor;
    @Mock
    private PostExecutionThread mockPostExecutionThread;

    @Mock
    private PaginatedParams mockPaginatedParams;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp(){
        getNowPlayingMovies = new GetNowPlayingMovies(mockThreadExecutor, mockPostExecutionThread, mockNowPlayingMoviesRepository);
    }

    @Test
    public void testGetNowPlayingMoviesUseCaseObservable(){
        getNowPlayingMovies.buildUseCaseObservable(mockPaginatedParams);

        verify(mockNowPlayingMoviesRepository).getNowPlayingMovies(mockPaginatedParams.getCurrent_page(),
                mockPaginatedParams.getPer_page(), mockPaginatedParams.isInternetAvailable());
        verifyNoMoreInteractions(mockNowPlayingMoviesRepository);
        verifyNoMoreInteractions(mockPostExecutionThread);
        verifyNoMoreInteractions(mockThreadExecutor);
    }

    @Test
    public void testShouldFailWhenNoOrEmptyParameters(){
        expectedException.expect(NullPointerException.class);
        getNowPlayingMovies.buildUseCaseObservable(null);
    }
}