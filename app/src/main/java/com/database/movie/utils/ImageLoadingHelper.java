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
package com.database.movie.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.database.movie.R;
import com.database.movie.di.modules.GlideApp;

import javax.inject.Inject;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 12th of January 2018
 */
public class ImageLoadingHelper {


    private static final String TAG = ImageLoadingHelper.class.getSimpleName();

    private Context mContext;

    @Inject
    public ImageLoadingHelper(Context context) {
        mContext = context;
    }


    public void load(String url, ImageView imageView){
        try {

            GlideApp.with(mContext)
                    .load(url)
                    .placeholder(R.drawable.grey_bg)
                    .error(R.drawable.error_loading_default_image)
                    .into(imageView);
        }catch(IllegalArgumentException e){
            Log.e(TAG, "ImageView Tag: " + String.valueOf(imageView.getTag()));
        }
    }
}
