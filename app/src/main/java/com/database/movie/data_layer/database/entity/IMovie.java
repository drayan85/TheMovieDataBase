/*
 * Copyright (c) 2018 Ilanthirayan Paramanathan
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
package com.database.movie.data_layer.database.entity;

import android.provider.BaseColumns;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 15th of January 2018
 */
public interface IMovie extends IBaseTable {

    interface Columns extends BaseColumns {
        String VOTE_AVERAGE = "vote_average";
        String BACKDROP_PATH = "backdrop_path";
        String ADULT = "adult";
        String ID = "id";
        String TITLE = "title";
        String OVERVIEW = "overview";
        String ORIGINAL_LANGUAGE = "original_language";
        String GENRE_IDS = "genre_ids";
        String RELEASE_DATE = "release_date";
        String ORIGINAL_TITLE = "original_title";
        String VOTE_COUNT = "vote_count";
        String POSTER_PATH = "poster_path";
        String VIDEO = "video";
        String POPULARITY = "popularity";
    }

    String TABLE_NAME = "tbl_movie";

    String CREATE_TABLE_MOVIES = CREATE_TABLE
            + TABLE_NAME + " ("
            + Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Columns.VOTE_AVERAGE + " REAL, "
            + Columns.BACKDROP_PATH + " TEXT, "
            + Columns.ADULT + " TEXT, "
            + Columns.ID + " INTEGER, "
            + Columns.TITLE + " TEXT, "
            + Columns.OVERVIEW + " TEXT, "
            + Columns.ORIGINAL_LANGUAGE + " TEXT, "
            + Columns.GENRE_IDS + " TEXT, "
            + Columns.RELEASE_DATE + " TEXT, "
            + Columns.ORIGINAL_TITLE + " TEXT, "
            + Columns.VOTE_COUNT + " INTEGER, "
            + Columns.POSTER_PATH + " TEXT, "
            + Columns.VIDEO + " TEXT, "
            + Columns.POPULARITY+ " REAL); ";

    String CREATE_MOVIES_INDEX_ID = CREATE_INDEX
            + CREATE_INDEX_BASED_ON
            + TABLE_NAME + "("
            + Columns.ID + ");";

    String DROP_TABLE_QUERY = DROP_TABLES + TABLE_NAME;
}
