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

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.database.movie.R;
import com.database.movie.data_layer.api.response.PaginatedMovies;
import com.database.movie.data_layer.model.Movie;
import com.database.movie.databinding.HomeFragmentBinding;
import com.database.movie.presentation_layer.adapters.MovieRVAdapter;
import com.database.movie.presentation_layer.main.MainActivity;
import com.database.movie.presentation_layer.main.NavigationFragment;
import com.database.movie.utils.ImageLoadingHelper;

import java.util.Arrays;

/**
 * This specifies the contract between the view and the presenter.
 *
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 15th of January 2018
 */
public class HomeScreenFragment extends NavigationFragment implements HomeScreenContractor.View, MovieRVAdapter.MovieRecyclerViewAdapterCallback {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private MovieRVAdapter mRecyclerViewAdapter;

    private View loadingLayout;
    private View noInternetLayout;
    private View emptyListLayout;
    private View listConstrainLayout;

    private boolean startDownloading;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 1; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    HomeScreenContractor.Presenter mPresenter;

    public static HomeScreenFragment newInstance(MainActivity mainActivity){
        HomeScreenFragment fragment = new HomeScreenFragment();
        fragment.mMainActivity = mainActivity;
        return fragment;
    }
    @Override
    public void onClickItem(Movie movie) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        HomeFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_screen, container, false);
        mSwipeRefreshLayout = binding.swipeRefreshLayout;
        mRecyclerView = binding.listRecyclerView;
        loadingLayout = binding.loadingLayout;
        noInternetLayout = binding.noInternetLayout;
        emptyListLayout = binding.emptyListLayout;
        listConstrainLayout = binding.moviesListConstraintLayout;

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(loadingLayout.getVisibility() == View.VISIBLE || !isInternetAvailable()){
                    // Stop refresh animation
                    mSwipeRefreshLayout.setRefreshing(false);
                }else{
                    mPresenter.getFirstSetOfRemoteItems(true);
                }
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                if (loading) {
                    if (totalItemCount > previousTotal + 1) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold) && !startDownloading) {
                    // End has been reached
                    loading = true;
                    onLoadMore();
                }
            }
        });

        return binding.getRoot();
    }

    private void onLoadMore() {
        if(mPresenter != null){
            //if there is not internet, check in the local table have more item,
            // if have more item then only execute otherwise no need request
            if (!isInternetAvailable() && mPresenter.getTotal_local_movies() <= getTotalNumberOfItemsInAdapter()) {
                //no need to load more videos
                return;
            }
            //All items are loaded check
            if (mPresenter.getTotal_remote_movies() > 0 && mPresenter.getTotal_remote_movies() <= getTotalNumberOfItemsInAdapter()) {
                //no need to load more videos
                return;
            }

            mPresenter.getPaginatedItems(isInternetAvailable());
        }
    }

    @Override
    public void setPresenter(HomeScreenContractor.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showLoadingIndicator() {
        mRecyclerView.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        noInternetLayout.setVisibility(View.GONE);
        emptyListLayout.setVisibility(View.GONE);
        listConstrainLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyMessage() {
        if(mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
            // Stop refresh animation
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mRecyclerView.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
        noInternetLayout.setVisibility(View.GONE);
        emptyListLayout.setVisibility(View.VISIBLE);
        listConstrainLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoInternetMessage() {
        mRecyclerView.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
        noInternetLayout.setVisibility(View.VISIBLE);
        emptyListLayout.setVisibility(View.GONE);
        listConstrainLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showItemListLayout() {
        mRecyclerView.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        noInternetLayout.setVisibility(View.GONE);
        emptyListLayout.setVisibility(View.GONE);
        listConstrainLayout.setVisibility(View.GONE);
    }

    @Override
    public void onSwipeRefreshItemsLoadComplete(PaginatedMovies paginatedMovies, ImageLoadingHelper mImageLoadingHelper) {
        if(mRecyclerViewAdapter == null){
            initializeList(paginatedMovies, mImageLoadingHelper);
        }else{
            mRecyclerViewAdapter.swapItems(Arrays.asList(paginatedMovies.getResults()));
        }
        previousTotal = 0;//enable the load more again

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMoreItemsCompleted(PaginatedMovies paginatedMovies) {
        startDownloading = false;
        mRecyclerViewAdapter.removeNullObjectToDisableLoadMoreProgress();
        mRecyclerViewAdapter.addMoreItemsAtTheBottomOfTheList(Arrays.asList(paginatedMovies.getResults()));
    }

    @Override
    public int getTotalNumberOfItemsInAdapter() {
        if(mRecyclerViewAdapter != null){
            return mRecyclerViewAdapter.getItemCount();
        }
        return 0;
    }

    @Override
    public void addNullObjectToEnableLoadMoreProgress() {
        //GET THE NEXT SET OF DATA FROM REMOTE
        mRecyclerViewAdapter.addNullObjectToEnableLoadMoreProgress();
        startDownloading = true;
    }

    @Override
    public void initializeList(@NonNull PaginatedMovies paginatedMovies, ImageLoadingHelper imageLoadingHelper) {
        if(paginatedMovies.getResults() == null){
            if(isInternetAvailable()){
                showNoInternetMessage();
            }else{
                showEmptyMessage();
            }
        }else if(mRecyclerView.getVisibility() != View.VISIBLE || mRecyclerViewAdapter == null){
            showItemListLayout();
            //initialise the adapter
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerViewAdapter = new MovieRVAdapter(Arrays.asList(paginatedMovies.getResults()), imageLoadingHelper, this);
            mRecyclerView.setAdapter(mRecyclerViewAdapter);
        }else{
            //swapItems in the adapter
            mRecyclerViewAdapter.swapItems(Arrays.asList(paginatedMovies.getResults()));
        }
    }

    @Override
    public void noMoreItemsToDisplay() {
        startDownloading = false;
        mRecyclerViewAdapter.removeNullObjectToDisableLoadMoreProgress();
    }

    @Override
    public void errorLoadingDisableLoading() {
        if(loadingLayout.getVisibility() == View.VISIBLE){
            showEmptyMessage();
        }
        disableSwipeRefreshing();
    }
}
