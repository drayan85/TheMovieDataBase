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
package com.database.movie.data_layer.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Singleton;

/**
 * Helper class for Concurrent Database Access
 *
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 15th of January 2018
 */
@Singleton
public class DataBaseManager {

    private AtomicInteger mOpenWritableCounter = new AtomicInteger();
    private AtomicInteger mOpenReadableCounter = new AtomicInteger();

    private DBHelper mDBHelper;
    private SQLiteDatabase mWriteSQLiteDataBase;
    private SQLiteDatabase mReadSQLiteDataBase;

    private DataBaseManager() {}

    public DataBaseManager(DBHelper mDBHelper) {
        this.mDBHelper = mDBHelper;
    }

    /**
     * Get the SQLiteDatabase Connection with Writable permission
     *
     * @return {@link SQLiteDatabase}
     */
    public synchronized SQLiteDatabase openWritableDatabase() {
        if(mOpenWritableCounter.incrementAndGet() == 1) {
            // Opening new writable database
            mWriteSQLiteDataBase = mDBHelper.getWritableDatabase();
            mWriteSQLiteDataBase.enableWriteAheadLogging();
        }
        return mWriteSQLiteDataBase;
    }

    /**
     * Get the SQLiteDatabase Connection with Readable permission
     *
     * @return {@link SQLiteDatabase}
     */
    public synchronized SQLiteDatabase openReadableDatabase() {
        if(mOpenReadableCounter.incrementAndGet() == 1) {
            // Opening new readable database
            mReadSQLiteDataBase = mDBHelper.getReadableDatabase();
            mReadSQLiteDataBase.enableWriteAheadLogging();
        }
        return mReadSQLiteDataBase;
    }

    /**
     * Close the writable SQLiteDatabase connection
     */
    public synchronized void closeWritableSQLiteDatabase() {
        if(mOpenWritableCounter.decrementAndGet() == 0) {
            // Closing database
            mWriteSQLiteDataBase.close();
        }
    }

    /**
     * Close the readable SQLiteDatabase connection
     */
    public synchronized void closeReadableSQLiteDatabase() {
        if(mOpenReadableCounter.decrementAndGet() == 0) {
            // Closing database
            mReadSQLiteDataBase.close();
        }
    }

    public synchronized void onClearDataBase(SQLiteDatabase db){
        mDBHelper.onClear(db);
    }
}
