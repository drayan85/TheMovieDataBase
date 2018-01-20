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

package com.database.movie;

import android.content.pm.PackageInfo;
import android.test.ApplicationTestCase;
import android.test.MoreAsserts;

import org.junit.Before;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 19th of January 2018
 */
public class TheMovieDBApplicationTest extends ApplicationTestCase<TheMovieDBApplication>{

    private TheMovieDBApplication theMovieDBApplication;

    public TheMovieDBApplicationTest() {
        super(TheMovieDBApplication.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        createApplication();
        theMovieDBApplication = getApplication();
    }

    public void testCorrectionVersion() throws Exception{
        PackageInfo info = theMovieDBApplication.getPackageManager().getPackageInfo(theMovieDBApplication.getPackageName(), 0);
        assertNotNull(info);
        MoreAsserts.assertMatchesRegex("\\d\\.\\d.\\d", info.versionName);
    }

}