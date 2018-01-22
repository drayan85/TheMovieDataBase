/*
 *
 *  * Copyright (c) 2018 Ilanthirayan Paramanathan Open Source Project
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *  http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.database.movie.data_layer.database;

import android.support.test.runner.AndroidJUnit4;

import com.database.movie.Extras;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 22nd of January 2018
 */
@RunWith(AndroidJUnit4.class)
public class NowPlayingMoviesDBTest {

    private DBHelper database;

    @Before
    public void setUp() throws Exception {
        getTargetContext().deleteDatabase(Extras.DB_NAME);
        database = new DBHelper(getTargetContext());
    }

    @After
    public void tearDown() throws Exception {
        database.close();
    }

    @Test
    public void shouldAddExpenseType() throws Exception {

    }

}