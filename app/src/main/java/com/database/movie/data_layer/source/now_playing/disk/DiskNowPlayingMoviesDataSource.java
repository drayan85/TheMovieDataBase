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
package com.database.movie.data_layer.source.now_playing.disk;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.database.movie.data_layer.api.response.PaginatedMovies;
import com.database.movie.data_layer.database.DataBaseManager;
import com.database.movie.data_layer.database.entity.IMovie;
import com.database.movie.data_layer.database.entity.INowPlayingMovie;
import com.database.movie.data_layer.model.Movie;
import com.database.movie.data_layer.source.now_playing.NowPlayingMoviesDataSource;
import com.google.gson.Gson;

import io.reactivex.Observable;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 15th of January 2018
 */
public class DiskNowPlayingMoviesDataSource implements NowPlayingMoviesDataSource {

    private static final String TAG = "DiskNowPlayingMoviesDat";

    private DataBaseManager mDataBaseManager;
    private Gson mGson;

    //prevent direct instantiation.
    private DiskNowPlayingMoviesDataSource() {}

    public DiskNowPlayingMoviesDataSource(DataBaseManager dataBaseManager, Gson gson) {
        this.mDataBaseManager = dataBaseManager;
        this.mGson = gson;
    }

    /**
     * Get {@link PaginatedMovies} from SQLite {@link INowPlayingMovie} table (Local)
     *
     * @param current_page
     * @param per_page
     * @return {@link Observable}<{@link PaginatedMovies}>
     */
    @Override
    public Observable getNowPlayingMovies(int current_page, int per_page) {
        return Observable.just(new PaginatedMovies());
    }

    @Override
    public Observable saveNowPlayingMovies(PaginatedMovies paginatedMovies) {
        final SQLiteDatabase db = mDataBaseManager.openWritableDatabase();
        final String whereClause = IMovie.Columns.ID + " = ?  ";
        try {
            db.beginTransaction();
            for (Movie movie : paginatedMovies.getResults()) {
                final String[] whereArgs = {String.valueOf(movie.getId())};

                boolean isUpdated = db.update(IMovie.TABLE_NAME, movie.toContentValues(mGson), whereClause, whereArgs) > 0;
                if (isUpdated) {
                    Log.d(TAG, "Updated a entry from IMovie.TABLE_NAME");
                } else {
                    Log.d(TAG, "Not Updated any entry from IMovie.TABLE_NAME");
                    //NEED INSERT IF RECORDS IS NOT EXIST
                    boolean isInserted = db.insert(IMovie.TABLE_NAME, null, movie.toContentValues(mGson)) > 0;
                    if (isInserted) {
                        Log.d(TAG, "Successfully inserted in to IMovie.TABLE_NAME.");
                    } else {
                        Log.d(TAG, "Not inserted in to IMovie.TABLE_NAME.");
                    }
                }

                //TODO insert in to INowPlayingMovies Table
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            mDataBaseManager.closeWritableSQLiteDatabase();
        }
        return Observable.just(true);
    }
}
