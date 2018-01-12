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
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.database.movie.BaseActivity;
import com.database.movie.R;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 12th of January 2018
 */
public class HomeScreenActivity extends BaseActivity {


    public static void start(Context context) {
        Intent starter = new Intent(context, HomeScreenActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO DataBinding
        setContentView(R.layout.activity_home_screen);

        onViewReady(savedInstanceState, getIntent());
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
}
