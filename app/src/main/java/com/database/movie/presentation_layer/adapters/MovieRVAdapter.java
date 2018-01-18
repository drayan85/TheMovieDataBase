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
package com.database.movie.presentation_layer.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.database.movie.BR;
import com.database.movie.R;
import com.database.movie.data_layer.model.Movie;
import com.database.movie.databinding.MovieAdapterBinding;
import com.database.movie.utils.ImageLoadingHelper;
import com.database.movie.utils.MovieListDiffCallback;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * This specifies the contract between the view and the presenter.
 *
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 15th of January 2018
 */
public class MovieRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Movie> mMovieList = new ArrayList<>();
    private Handler handler = new Handler();
    public CompositeDisposable mDisposables;
    private ImageLoadingHelper mImageLoadingHelper;
    private MovieRecyclerViewAdapterCallback mAdapterCallback;

    private final int VIEW_PROG = 0;
    private final int VIEW_ITEM = 1;

    public MovieRVAdapter(List<Movie> mMovieList, ImageLoadingHelper mImageLoadingHelper, MovieRecyclerViewAdapterCallback mAdapterCallback) {
        this.mMovieList.addAll(mMovieList);
        this.mImageLoadingHelper = mImageLoadingHelper;
        this.mAdapterCallback = mAdapterCallback;
        mDisposables = new CompositeDisposable();
    }

    public interface MovieRecyclerViewAdapterCallback{

        void onClickItem(Movie movie);
    }

    public void swapItems(final List<Movie> newMovies){
        Observable<DiffUtil.DiffResult> mObservable = Observable.just(mMovieList)
                .flatMap(new Function<List<Movie>, ObservableSource<DiffUtil.DiffResult>>() {
                    @Override
                    public ObservableSource<DiffUtil.DiffResult> apply(@NonNull List<Movie> oldMovie) throws Exception {
                        return Observable.just(DiffUtil .calculateDiff(new MovieListDiffCallback(oldMovie, newMovies), true));
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
        mDisposables.add(mObservable.subscribeWith(new SwapItemsObserver(this, newMovies)));
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mMovieList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        if(viewType== VIEW_ITEM) {
            // create a new view
            MovieAdapterBinding viewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_movie_details, viewGroup, false);
            viewHolder = new MovieDetailsViewHolder(viewDataBinding);
        }else{
            View view = LayoutInflater.from(viewGroup.getContext()) .inflate(R.layout.progress_item, viewGroup, false);
            viewHolder = new ProgressViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ProgressViewHolder){
            ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }else{
            ((MovieDetailsViewHolder)holder).bind(holder);
        }
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public class MovieDetailsViewHolder extends RecyclerView.ViewHolder{

        private final ViewDataBinding binding;
        private final ImageView moviePosterImg;

        public MovieDetailsViewHolder(MovieAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            moviePosterImg = binding.moviePosterImg;
        }

        public void bind(final RecyclerView.ViewHolder holder){
            final Movie movie = mMovieList.get(holder.getAdapterPosition());
            binding.setVariable(BR.movie, movie);
            binding.executePendingBindings();

            //SET THE BANNER IMAGE
            if(!TextUtils.isEmpty(movie.getPoster_path_url())){
                mImageLoadingHelper.load(movie.getPoster_path_url(), moviePosterImg);
            }else if(!TextUtils.isEmpty(movie.getBackdrop_path_url())){
                mImageLoadingHelper.load(movie.getBackdrop_path_url(), moviePosterImg);
            }

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapterCallback.onClickItem(movie);
                }
            });
        }
    }

    public void removeNullObjectToDisableLoadMoreProgress() {
        Runnable r = new Runnable() {
            public void run() {
                if(mMovieList.get(mMovieList.size() - 1) == null){
                    mMovieList.remove(mMovieList.size() - 1);
                    notifyItemRemoved(mMovieList.size());
                }
            }
        };
        handler.post(r);
    }

    public void addMoreItemsAtTheBottomOfTheList(final List<Movie> movieList) {
        Runnable r = new Runnable() {
            public void run() {
                for (Movie movie : movieList) {
                    mMovieList.add(movie);
                    notifyItemInserted(mMovieList.size());
                }
            }
        };
        handler.post(r);
    }

    public void addNullObjectToEnableLoadMoreProgress() {
        Runnable r = new Runnable() {
            public void run() {
                //add null , so the adapterVideoDetails will check view_type and show progress bar at bottom
                mMovieList.add(null);
                notifyItemInserted(mMovieList.size() - 1);
            }
        };
        handler.post(r);
    }

    private final class SwapItemsObserver extends DisposableObserver<DiffUtil.DiffResult> {

        MovieRVAdapter adapter;
        List<Movie> newMovieList;

        public SwapItemsObserver(MovieRVAdapter adapter, List<Movie> newMovieList) {
            this.adapter = adapter;
            this.newMovieList = newMovieList;
        }

        @Override
        public void onNext(DiffUtil.DiffResult diffResult) {
            adapter.mMovieList.clear();
            adapter.mMovieList.addAll(newMovieList);
            diffResult.dispatchUpdatesTo(adapter);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }
}
