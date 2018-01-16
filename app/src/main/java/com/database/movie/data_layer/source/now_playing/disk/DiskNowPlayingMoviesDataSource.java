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

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.database.movie.data_layer.api.response.PaginatedMovies;
import com.database.movie.data_layer.database.DataBaseManager;
import com.database.movie.data_layer.database.entity.IMovie;
import com.database.movie.data_layer.database.entity.IMovieID;
import com.database.movie.data_layer.database.entity.INowPlayingMovie;
import com.database.movie.data_layer.model.Movie;
import com.database.movie.data_layer.model.MovieID;
import com.database.movie.data_layer.source.now_playing.NowPlayingMoviesDataSource;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

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
     * Get {@link PaginatedMovies} from SQLite {@link IMovieID} table (Local)
     *
     * @param current_page
     * @param per_page
     * @return {@link Observable}<{@link PaginatedMovies}>
     */
    @Override
    public Observable getNowPlayingMovies(int current_page, int per_page) {
        SQLiteDatabase db = mDataBaseManager.openReadableDatabase();
        List<MovieID> nowPlayingMovieIDList = new ArrayList<>();
        List<Movie> nowPlayingMovieList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM " + INowPlayingMovie.TABLE_NAME + " LIMIT " + per_page + " OFFSET " + current_page * per_page;
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    nowPlayingMovieIDList.add(new MovieID(cursor));
                }
            }
            cursor.close();

            final String selections = IMovie.Columns.ID + " = ? ";
            for (MovieID movieID : nowPlayingMovieIDList) {
                final String[] selectionArgs = {String.valueOf(movieID.getId())};
                Cursor cursorMovie = db.query(IMovie.TABLE_NAME, null, selections, selectionArgs, null, null, null);
                //check is if Movie exist
                if(cursorMovie.moveToFirst()){
                    nowPlayingMovieList.add(new Movie(cursorMovie, mGson));
                }
                cursorMovie.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mDataBaseManager.closeReadableSQLiteDatabase();
        }
        PaginatedMovies paginatedMovies = new PaginatedMovies();
        paginatedMovies.setPage(current_page);
        int total_movies = countNowPlayingMoviesLocally();
        paginatedMovies.setTotal_results(total_movies);
        int reminder = total_movies % per_page;
        int total_pages = total_movies / per_page;
        if(reminder > 0){
            paginatedMovies.setTotal_pages(total_pages + 1);
        }else{
            paginatedMovies.setTotal_pages(total_pages);
        }
        paginatedMovies.setResults(nowPlayingMovieList.toArray(new Movie[nowPlayingMovieList.size()]));
        return Observable.just(paginatedMovies);
    }

    private int countNowPlayingMoviesLocally() {
        int count = 0;
        SQLiteDatabase db = mDataBaseManager.openReadableDatabase();
        try {
            count = (int) DatabaseUtils.queryNumEntries(db, INowPlayingMovie.TABLE_NAME, null, null);
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            mDataBaseManager.closeReadableSQLiteDatabase();
        }
        return count;
    }

    @Override
    public Observable saveNowPlayingMovies(PaginatedMovies paginatedMovies) {
        final SQLiteDatabase db = mDataBaseManager.openWritableDatabase();
        final String whereClause = IMovie.Columns.ID + " = ?  ";
        final String whereClauseNowPlaying = INowPlayingMovie.Columns.ID + " = ?  ";
        try {
            db.beginTransaction();
            if(paginatedMovies.getPage() == 1){//can't ascending or descending order by ID because ID not in order
                //clear the table and then insert
                db.delete(INowPlayingMovie.TABLE_NAME, null, null);
            }
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

                //insert in to INowPlayingMovies Table
                final MovieID movieID = new MovieID(movie.getId());
                final String[] whereArgsNowPlaying = {String.valueOf(movieID.getId())};
                boolean isNowPlayingUpdated = db.update(INowPlayingMovie.TABLE_NAME, movieID.toContentValues(), whereClauseNowPlaying, whereArgsNowPlaying) > 0;
                if (isNowPlayingUpdated) {
                    Log.d(TAG, "Updated a entry from INowPlayingMovie.TABLE_NAME");
                } else {
                    Log.d(TAG, "Not Updated any entry from INowPlayingMovie.TABLE_NAME");
                    //NEED INSERT IF RECORDS IS NOT EXIST
                    boolean isInserted = db.insert(INowPlayingMovie.TABLE_NAME, null, movieID.toContentValues()) > 0;
                    if (isInserted) {
                        Log.d(TAG, "Successfully inserted in to INowPlayingMovie.TABLE_NAME.");
                    } else {
                        Log.d(TAG, "Not inserted in to INowPlayingMovie.TABLE_NAME.");
                    }
                }
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
