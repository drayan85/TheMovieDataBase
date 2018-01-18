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
package com.database.movie.presentation_layer.main;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 15th of January 2018
 */
public class NavigationFragment extends Fragment {


    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected MainActivity mMainActivity;

    public void disableSwipeRefreshing(){
        if(mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()){
            // Stop refresh animation
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public boolean isInternetAvailable() {
        if(mMainActivity != null) {
            return mMainActivity.isInternetAvailable();
        } else {
            return false;
        }
    }

    public void showToastMessage(String message) {
        if(mMainActivity != null){
            mMainActivity.displayToastMessage(message);
        }
    }
}
