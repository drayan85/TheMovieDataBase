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
package com.database.movie.di.modules;

import com.database.movie.di.PerActivity;
import com.database.movie.presentation_layer.main.home.HomeScreenContractor;

import dagger.Module;
import dagger.Provides;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 15th of January 2018
 */
@Module
public class HomeScreenViewModule {

    private HomeScreenContractor.View mView;

    public HomeScreenViewModule(HomeScreenContractor.View mView) {
        this.mView = mView;
    }

    @PerActivity
    @Provides
    HomeScreenContractor.View provideHomeScreenView(){
        return mView;
    }
}
