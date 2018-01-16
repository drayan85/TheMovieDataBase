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

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 16th of January 2018
 */
public interface INowPlayingMovie extends IMovieID {

    String TABLE_NAME = "tbl_now_playing_movie";

    String CREATE_TABLE_NOW_PLAYING_MOVIES = CREATE_TABLE
            + TABLE_NAME + " ("
            + Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Columns.ID+ " REAL); ";

    String CREATE_NOW_PLAYING_MOVIES_INDEX_ID = CREATE_INDEX
            + CREATE_INDEX_BASED_ON
            + TABLE_NAME + "("
            + Columns.ID + ");";

    String DROP_TABLE_QUERY = DROP_TABLES + TABLE_NAME;
}
