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

package com.database.movie.presentation_layer.main.home;

import android.content.Context;

import com.database.movie.data_layer.api.response.PaginatedMovies;
import com.database.movie.domain_layer.interactor.PaginatedParams;
import com.database.movie.domain_layer.interactor.now_playing.GetNowPlayingMovies;
import com.database.movie.domain_layer.interactor.now_playing.SaveNowPlayingMovies;
import com.database.movie.utils.ImageLoadingHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.observers.DisposableObserver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 18th of January 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class HomeScreenPresenterTest {

    @Mock
    private Context mockContext;
    @Mock
    private HomeScreenContractor.View mockHomeScreenView;
    @Mock
    private GetNowPlayingMovies mockGetNowPlayingMovies;
    @Mock
    private SaveNowPlayingMovies mockSaveNowPlayingMovies;

    @Mock
    private PaginatedMovies mockPaginatedMovies;

    private HomeScreenPresenter presenter;
    private ImageLoadingHelper mImageLoadingHelper;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mImageLoadingHelper = new ImageLoadingHelper(mockContext);
        presenter = new HomeScreenPresenter(mockHomeScreenView, mockGetNowPlayingMovies, mockSaveNowPlayingMovies, mImageLoadingHelper);
        mockHomeScreenView.setPresenter(presenter);
    }

    @Test
    public void testGetFirstSetOfLocalItems_whenNoInternet() {
        presenter.getFirstSetOfLocalItems(false);
        verify(mockHomeScreenView).showLoadingIndicator();
        verify(mockGetNowPlayingMovies).execute(any(DisposableObserver.class), any(PaginatedParams.class));
    }

    @Test
    public void getFirstSetOfRemoteItems_whenInternetAvailable() {
        presenter.getFirstSetOfRemoteItems(false);
        verify(mockGetNowPlayingMovies).execute(any(DisposableObserver.class), any(PaginatedParams.class));
    }

    @Test
    public void getFirstSetOfRemoteItems_whenInternetAvailable_whenForceToUpdate() {
        presenter.getFirstSetOfRemoteItems(true);
        verify(mockGetNowPlayingMovies).execute(any(DisposableObserver.class), any(PaginatedParams.class));
    }

    @Test
    public void saveItemListInToLocalDataBase_afterGetPaginatedMoviesFromRemote() {
        presenter.saveItemListInToLocalDataBase(mockPaginatedMovies);
        verify(mockSaveNowPlayingMovies).execute(any(DisposableObserver.class), any(PaginatedMovies.class));
    }

}