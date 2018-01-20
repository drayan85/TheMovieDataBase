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

package com.database.movie.presentation_layer.main;

import android.content.Intent;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.database.movie.R;

import org.junit.After;
import org.junit.Before;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 19th of January 2018
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mainActivity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //this.setActivityIntent(createTargetIntent());//if you pass any values through intent
        this.mainActivity = getActivity();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testContainsHomeScreenFragment() {
        Fragment homeScreenFragment = mainActivity.getSupportFragmentManager().findFragmentById(R.id.content_frame);
        assertThat(homeScreenFragment, is(notNullValue()));
    }

    public void testRecyclerViewIsPresent() {
        Fragment homeScreenFragment = mainActivity.getSupportFragmentManager().findFragmentById(R.id.content_frame);
        assertThat(homeScreenFragment, is(notNullValue()));
        View viewById = homeScreenFragment.getView().findViewById(R.id.list_recycler_view);
        assertThat(viewById, notNullValue());
    }

    public void testNowPlayingMoviesTitle() {
        String actual_title = this.mainActivity.getTitle().toString().trim();
        assertThat(actual_title, is(mainActivity.getString(R.string.app_name)));
    }

    public void testNavigationDrawer_MenuClick() {
        onView(withId(R.id.home_main_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.home_main_drawer_layout)).check(matches(isOpen()));
        //below line will fail with -> This failed! “No views in hierarchy with id”.
        //onView(withId(R.id.nav_home)).perform(click());
        //Here's the difference
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_home));
    }

    public void testNavigationDrawerUserNameDisplayed(){
        onView(withId(R.id.home_main_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.user_name_txt)).check(matches(withText(mainActivity.getString(R.string.default_user_name))));
    }



    private Intent createTargetIntent() {
        Intent intent = MainActivity.getCallingIntent(getInstrumentation().getTargetContext());
        return intent;
    }
}