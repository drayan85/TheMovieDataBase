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

import com.database.movie.data_layer.api.response.PaginatedMovies;
import com.database.movie.utils.ImageLoadingHelper;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 15th of January 2018
 */
public interface HomeScreenContractor {

    interface View{

        boolean isInternetAvailable();

        void showLoadingIndicator();

        void showEmptyMessage();

        void showItemListLayout();

        void showToastMessage(String message);

        void showNoInternetMessage();

        void onSwipeRefreshItemsLoadComplete(PaginatedMovies paginatedMovies, ImageLoadingHelper mImageLoadingHelper);

        void onLoadMoreItemsCompleted(PaginatedMovies paginatedMovies);

        int getTotalNumberOfItemsInAdapter();

        void addNullObjectToEnableLoadMoreProgress();

        void initializeList(PaginatedMovies paginatedMovies, ImageLoadingHelper imageLoadingHelper);

        void noMoreItemsToDisplay();

        void errorLoadingDisableLoading();

        void setPresenter(HomeScreenContractor.Presenter presenter);
    }

    interface Presenter<T> {

        void getPaginatedItems(boolean isInternetAvailable);

        void getFirstSetOfLocalItems(boolean forceUpdate);

        void getFirstSetOfRemoteItems(boolean forceUpdate);

        void saveItemListInToLocalDataBase(PaginatedMovies paginatedMovies);

        void onDestroy();

        int getTotal_local_movies();

        int getTotal_remote_movies();
    }

}
