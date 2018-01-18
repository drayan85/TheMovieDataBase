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
package com.database.movie.presentation_layer.main.home;


import com.database.movie.data_layer.api.RetrofitException;
import com.database.movie.data_layer.api.response.APIError;
import com.database.movie.data_layer.api.response.PaginatedMovies;
import com.database.movie.domain_layer.interactor.DefaultObserver;
import com.database.movie.domain_layer.interactor.PaginatedParams;
import com.database.movie.domain_layer.interactor.now_playing.GetNowPlayingMovies;
import com.database.movie.domain_layer.interactor.now_playing.SaveNowPlayingMovies;
import com.database.movie.utils.ImageLoadingHelper;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 15th of January 2018
 */
public class HomeScreenPresenter implements HomeScreenContractor.Presenter{

    private HomeScreenContractor.View mHomeScreenView;
    private final GetNowPlayingMovies mGetNowPlayingMovies;
    private final SaveNowPlayingMovies mSaveNowPlayingMovies;
    private ImageLoadingHelper mImageLoadingHelper;

    private AtomicInteger current_page = new AtomicInteger(1);
    private final int per_page = 10;
    private int total_local_movies = 0;
    private int total_remote_movies = 0;

    @Inject
    public HomeScreenPresenter(HomeScreenContractor.View homeScreenView,
                               GetNowPlayingMovies getNowPlayingMovies,
                               SaveNowPlayingMovies saveNowPlayingMovies,
                               ImageLoadingHelper imageLoadingHelper) {
        this.mHomeScreenView = homeScreenView;
        this.mGetNowPlayingMovies = getNowPlayingMovies;
        this.mSaveNowPlayingMovies = saveNowPlayingMovies;
        this.mImageLoadingHelper = imageLoadingHelper;
    }
    /**
     * Method injection is used here to safely reference {@code this} after the object is created.
     * For more information, see Java Concurrency in Practice.
     */
    @Inject
    void setupListeners() {
        mHomeScreenView.setPresenter(this);
        if(mHomeScreenView.isInternetAvailable()){
            getFirstSetOfRemoteItems(false);
        }else{
            getFirstSetOfLocalItems(false);
        }
    }

    @Override
    public void getPaginatedItems(final boolean isInternetAvailable) {
        //if there is not internet, check in the local table have more item,
        // if have more item then only execute otherwise no need request
        if (!isInternetAvailable && total_local_movies <= mHomeScreenView.getTotalNumberOfItemsInAdapter()) {
            //no need to load more videos
            return;
        }
        //All items are loaded check
        if (total_remote_movies > 0 && total_remote_movies <= mHomeScreenView.getTotalNumberOfItemsInAdapter()) {
            //no need to load more videos
            return;
        }

        //Enable loading progress bar
        mHomeScreenView.addNullObjectToEnableLoadMoreProgress();


        mGetNowPlayingMovies.execute(new PaginatedMoviesObserver() {
            @Override
            public void onNext(PaginatedMovies paginatedMovies) {
                if(paginatedMovies.getResults() != null && paginatedMovies.getResults().length > 0){
                    if(isInternetAvailable){
                        saveItemListInToLocalDataBase(paginatedMovies);
                        total_remote_movies = paginatedMovies.getTotal_results();
                    }else{
                        total_local_movies = paginatedMovies.getTotal_results();
                    }
                    mHomeScreenView.onLoadMoreItemsCompleted(paginatedMovies);
                }else{
                    //remove progress added at the bottom
                    mHomeScreenView.noMoreItemsToDisplay();
                }
            }

            @Override
            public void onError(Throwable e) {
                retrofitExceptionHandler(e);
            }
        }, new PaginatedParams(current_page.incrementAndGet(), per_page, isInternetAvailable));
    }

    @Override
    public void getFirstSetOfLocalItems(boolean forceUpdate) {
        current_page.set(1);
        mGetNowPlayingMovies.execute(new PaginatedMoviesObserver() {
            @Override
            public void onNext(PaginatedMovies paginatedMovies) {
                total_local_movies = paginatedMovies.getTotal_results();
                mHomeScreenView.initializeList(paginatedMovies, mImageLoadingHelper);
            }

            @Override
            public void onError(Throwable e) {
                retrofitExceptionHandler(e);
            }
        }, new PaginatedParams(current_page.get(), per_page, false));
    }

    @Override
    public void getFirstSetOfRemoteItems(final boolean forceUpdate) {
        current_page.set(1);
        mGetNowPlayingMovies.execute(new PaginatedMoviesObserver() {
            @Override
            public void onNext(PaginatedMovies paginatedMovies) {
                if(forceUpdate){
                    mHomeScreenView.onSwipeRefreshItemsLoadComplete(paginatedMovies, mImageLoadingHelper);
                }else{
                    mHomeScreenView.initializeList(paginatedMovies, mImageLoadingHelper);
                }
                total_remote_movies = paginatedMovies.getTotal_results();
                saveItemListInToLocalDataBase(paginatedMovies);
            }

            @Override
            public void onError(Throwable e) {
                retrofitExceptionHandler(e);
            }
        }, new PaginatedParams(current_page.get(), per_page, true));
    }

    @Override
    public void saveItemListInToLocalDataBase(PaginatedMovies paginatedMovies) {
        mSaveNowPlayingMovies.execute(new BooleanObserver() {}, paginatedMovies);
    }

    @Override
    public void onDestroy() {
        mHomeScreenView = null;
        mGetNowPlayingMovies.dispose();
        mSaveNowPlayingMovies.dispose();
    }

    private void retrofitExceptionHandler(Throwable e) {
        APIError errorBody = null;
        if(e instanceof RetrofitException){
            try {
                RetrofitException retrofitException = (RetrofitException) e;
                errorBody = retrofitException.getErrorBodyAs(APIError.class);
                //int responseCode = retrofitException.getResponse() != null ? retrofitException.getResponse().code() : 0;//From Response Header
                //if(responseCode == 401 || responseCode == 403){
                    //access token has been expired, need to renew the token
                //}
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        mHomeScreenView.showToastMessage(errorBody != null ? errorBody.getStatus_message() : e.getMessage());
        mHomeScreenView.errorLoadingDisableLoading();
    }


    private abstract class PaginatedMoviesObserver extends DefaultObserver<PaginatedMovies> {}
    private abstract class BooleanObserver extends DefaultObserver<Boolean> {}
}
