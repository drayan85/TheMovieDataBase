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

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.database.movie.BuildConfig;
import com.database.movie.utils.JsonFormatHttpLogging;

import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 12th of January 2018
 */
@GlideModule
public class TheMovieDBGlideModule extends AppGlideModule {

    // Size in bytes (20 MB)
    final int GLIDE_DISK_CACHE_SIZE = 1024 * 1024 * 20;

    @SuppressLint("CheckResult")
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Apply options to the builder here.
        //builder.setBitmapPool(new LruBitmapPool(GLIDE_DISK_CACHE_SIZE));
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, GLIDE_DISK_CACHE_SIZE * 2));

        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .build();
        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));

        // Apply options to the builder here.
        builder.setDefaultRequestOptions(
                new RequestOptions()
                        .format(DecodeFormat.PREFER_ARGB_8888));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new JsonFormatHttpLogging())
                .setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder =  new OkHttpClient.Builder();
        if(BuildConfig.DEBUG) {
            builder.addInterceptor(httpLoggingInterceptor);
        }
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(builder.build()));
        //super.registerComponents(context, glide, registry);
    }
}
