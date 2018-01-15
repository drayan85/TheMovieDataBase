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
package com.database.movie.data_layer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import javax.inject.Singleton;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 15th of January 2018
 */
@Singleton
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "the_movie_database.db";
    private static final int DB_VERSION = 1;
    private static final String TAG = DBHelper.class.getSimpleName();

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "db onCreate");
        //CREATE TABLES

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.enableWriteAheadLogging();
        super.onConfigure(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "db onUpgrade oldVersion : " + oldVersion + " newVersion " + newVersion);
        dropTables(db);
        onCreate(db);
    }

    public void onClear(SQLiteDatabase db){

    }

    private void dropTables(SQLiteDatabase db){

    }
}
