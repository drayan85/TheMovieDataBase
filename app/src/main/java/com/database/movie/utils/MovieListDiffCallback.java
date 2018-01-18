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
package com.database.movie.utils;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.text.TextUtils;

import com.database.movie.data_layer.model.Movie;

import java.util.List;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 16th of January 2018
 */
public class MovieListDiffCallback extends DiffUtil.Callback {

    private List<Movie> oldMovieList;
    private List<Movie> newMovieList;

    public MovieListDiffCallback(List<Movie> oldMovieList, List<Movie> newMovieList) {
        this.oldMovieList = oldMovieList;
        this.newMovieList = newMovieList;
    }


    @Override
    public int getOldListSize() {
        return oldMovieList.size();
    }

    @Override
    public int getNewListSize() {
        return newMovieList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldMovieList.get(oldItemPosition).getId() == newMovieList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Movie newMovie = newMovieList.get(newItemPosition);
        Movie oldMovie = oldMovieList.get(oldItemPosition);
        return oldMovie.getId() == (newMovie.getId())
                && TextUtils.equals(oldMovie.getTitle(), newMovie.getTitle());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
