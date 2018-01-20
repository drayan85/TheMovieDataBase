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
package com.database.movie.domain_layer.interactor;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 18th of January 2018
 */
public class PaginatedParams {

    private final int current_page;
    private final int per_page;
    private final boolean isInternetAvailable;

    public PaginatedParams(int current_page, int per_page, boolean isInternetAvailable) {
        this.current_page = current_page;
        this.per_page = per_page;
        this.isInternetAvailable = isInternetAvailable;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public int getPer_page() {
        return per_page;
    }

    public boolean isInternetAvailable() {
        return isInternetAvailable;
    }
}
