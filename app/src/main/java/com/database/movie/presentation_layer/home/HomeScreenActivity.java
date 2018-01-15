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
package com.database.movie.presentation_layer.home;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.database.movie.BaseActivity;
import com.database.movie.R;
import com.database.movie.databinding.HomeScreenBinding;
import com.database.movie.databinding.NavigationDrawerBinding;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 12th of January 2018
 */
public class HomeScreenActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{


    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    public static final String NAVIGATION_DRAWER_MENU_ITEM_ID = "navigation_drawer_menu_item_id";

    private HomeScreenBinding homeScreenBinding;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private final Handler mDrawerActionHandler = new Handler();
    private ActionBarDrawerToggle mDrawerToggle;

    public static void start(Context context) {
        Intent starter = new Intent(context, HomeScreenActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_screen);

        setSupportActionBarView();
        setNavigationDrawerViews();

        onViewReady(savedInstanceState, getIntent());
    }

    private void setSupportActionBarView() {
        mToolbar = homeScreenBinding.toolbarHomeScreen;
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);
        showHamburger();
    }

    private void setNavigationDrawerViews() {
        mDrawerLayout = homeScreenBinding.homeMainDrawerLayout;
        mNavigationView = homeScreenBinding.navView;
        NavigationDrawerBinding navigationDrawerBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.nav_header, mNavigationView, false);
        mNavigationView.addHeaderView(navigationDrawerBinding.getRoot());
        // set up the hamburger icon to open and close the drawer
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open,
                R.string.close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mNavigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        //initialise the HomeScreen Fragment to provide the Dagger HomeScreenModule constructor

        super.onViewReady(savedInstanceState, intent);
    }

    @Override
    protected void resolveDaggerDependency() {
        //TODO Dagger Component Init
    }

    @Override
    protected void onDestroy() {
        //TODO destroy presenter
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // allow some time after closing the drawer before performing real navigation
        // so the user can see what is happening
        mDrawerLayout.closeDrawer(GravityCompat.START);
        mDrawerActionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //TODO Navigation Logic
            }
        }, DRAWER_CLOSE_DELAY_MS);
        return true;

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        finish();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
