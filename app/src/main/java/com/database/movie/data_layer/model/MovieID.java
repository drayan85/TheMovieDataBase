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
package com.database.movie.data_layer.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.database.movie.data_layer.database.entity.IMovieID;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 16th of January 2018
 */
public class MovieID {

    private long id;

    public MovieID(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MovieID(Cursor cursor) {
        fromCursor(cursor);
    }

    private void fromCursor(Cursor cursor) {
        int i = cursor.getColumnIndexOrThrow(IMovieID.Columns.ID);
        setId(cursor.getLong(i));
    }

    public ContentValues toContentValues() {
        final ContentValues values = new ContentValues();
        values.put(IMovieID.Columns.ID, getId());
        return values;
    }
}
