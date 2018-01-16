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

import com.database.movie.data_layer.database.entity.IMovie;
import com.google.gson.Gson;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 15th of January 2018
 */
public class Movie {

    //image_sizes = {w45, w92, w154, w185, w300, w500, original}
    private static String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/original";

    private double vote_average;
    private String backdrop_path;
    private boolean adult;
    private long id;
    private String title;
    private String overview;
    private String original_language;
    private int[] genre_ids;
    private String release_date;
    private String original_title;
    private int vote_count;
    private String poster_path;
    private boolean video;
    private double popularity;

    public double getVote_average() {
        return vote_average;
    }

    private void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    private String getBackdrop_path() {
        return backdrop_path;
    }

    public String getBackdrop_path_url(){
        return IMAGE_BASE_URL + backdrop_path;
    }

    private void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public boolean isAdult() {
        return adult;
    }

    private void setAdult(boolean adult) {
        this.adult = adult;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    private void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOriginal_language() {
        return original_language;
    }

    private void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public int[] getGenre_ids() {
        return genre_ids;
    }

    private void setGenre_ids(int[] genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getRelease_date() {
        return release_date;
    }

    private void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getOriginal_title() {
        return original_title;
    }

    private void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public int getVote_count() {
        return vote_count;
    }

    private void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    private String getPoster_path() {
        return poster_path;
    }

    public String getPoster_path_url(){
        return IMAGE_BASE_URL + poster_path;
    }

    private void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public boolean isVideo() {
        return video;
    }

    private void setVideo(boolean video) {
        this.video = video;
    }

    public double getPopularity() {
        return popularity;
    }

    private void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Movie) {
            Movie that = (Movie) object;
            return this.id == that.id
                    && this.title.equals(that.title); // for example
        }
        return false;
    }

    @Override
    public int hashCode() {
        return original_title.hashCode()
                + title.hashCode();
    }

    public Movie(Cursor cursor, Gson gson) {
        fromCursor(cursor, gson);
    }

    private void fromCursor(Cursor cursor, Gson gson) {
        int i = cursor.getColumnIndexOrThrow(IMovie.Columns.VOTE_COUNT);
        setVote_count(cursor.getInt(i));

        i = cursor.getColumnIndexOrThrow(IMovie.Columns.ID);
        setId(cursor.getLong(i));

        i = cursor.getColumnIndexOrThrow(IMovie.Columns.VIDEO);
        setVideo(Boolean.valueOf(cursor.getString(i)));

        i = cursor.getColumnIndexOrThrow(IMovie.Columns.VOTE_AVERAGE);
        setVote_average(cursor.getLong(i));

        i = cursor.getColumnIndexOrThrow(IMovie.Columns.TITLE);
        setTitle(cursor.getString(i));

        i = cursor.getColumnIndexOrThrow(IMovie.Columns.POPULARITY);
        setPopularity(cursor.getLong(i));

        i = cursor.getColumnIndexOrThrow(IMovie.Columns.POSTER_PATH);
        setPoster_path(cursor.getString(i));

        i = cursor.getColumnIndexOrThrow(IMovie.Columns.ORIGINAL_LANGUAGE);
        setOriginal_language(cursor.getString(i));

        i = cursor.getColumnIndexOrThrow(IMovie.Columns.ORIGINAL_TITLE);
        setOriginal_title(cursor.getString(i));

        i = cursor.getColumnIndexOrThrow(IMovie.Columns.GENRE_IDS);
        setGenre_ids(gson.fromJson(cursor.getString(i), int []. class));

        i = cursor.getColumnIndexOrThrow(IMovie.Columns.BACKDROP_PATH);
        setBackdrop_path(cursor.getString(i));

        i = cursor.getColumnIndexOrThrow(IMovie.Columns.ADULT);
        setAdult(Boolean.valueOf(cursor.getString(i)));

        i = cursor.getColumnIndexOrThrow(IMovie.Columns.OVERVIEW);
        setOverview(cursor.getString(i));

        i = cursor.getColumnIndexOrThrow(IMovie.Columns.RELEASE_DATE);
        setRelease_date(cursor.getString(i));
    }

    public ContentValues toContentValues(Gson gson){
        final ContentValues values = new ContentValues();
        values.put(IMovie.Columns.VOTE_COUNT, getVote_count());
        values.put(IMovie.Columns.ID, getId());
        values.put(IMovie.Columns.VIDEO, String.valueOf(isVideo()));
        values.put(IMovie.Columns.VOTE_AVERAGE, getVote_average());
        values.put(IMovie.Columns.TITLE, getTitle());
        values.put(IMovie.Columns.POPULARITY, getPopularity());
        values.put(IMovie.Columns.POSTER_PATH, getPoster_path());
        values.put(IMovie.Columns.ORIGINAL_LANGUAGE, getOriginal_language());
        values.put(IMovie.Columns.ORIGINAL_TITLE, getOriginal_title());
        values.put(IMovie.Columns.GENRE_IDS, gson.toJson(getGenre_ids()));
        values.put(IMovie.Columns.BACKDROP_PATH, getBackdrop_path());
        values.put(IMovie.Columns.ADULT, String.valueOf(isAdult()));
        values.put(IMovie.Columns.OVERVIEW, getOverview());
        values.put(IMovie.Columns.RELEASE_DATE, getRelease_date());
        return values;
    }
}