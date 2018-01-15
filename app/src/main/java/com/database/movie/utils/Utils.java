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
package com.database.movie.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.TypedValue;

/**
 * Utility class.
 *
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 12th of January 2018
 */
public class Utils {

    /**
     * Checks whether there's connection to Internet.
     *
     * @return true if there's connection or false otherwise
     */
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    public static int convertDPToPX(float pixel, Context mContext){
        Resources r = mContext.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixel, r.getDisplayMetrics());
        return  (int) px;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static  String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    public static String getUserAgentString(Context context){
        StringBuilder res = new StringBuilder(64);
        res.append("Dalvik/");
        res.append(System.getProperty("java.vm.version"));// such as 1.1.0
        res.append(" (Linux; U; Android )");
        res.append(Build.VERSION.RELEASE);//1.0
        //add the model for the release build
        if("REL".equals(Build.VERSION.CODENAME)){
            if(Build.MODEL.length() > 0 ){
                res.append("; ");
                res.append(Build.VERSION.CODENAME);
            }
        }
        // "MASTER" or "M4-rc20"
        if(Build.ID.length() > 0 ){
            res.append(" Build/");
            res.append(Build.ID);
        }
        res.append(";" + Utils.getDeviceName());
        res.append(")");
        res.append(" TheMovieDB-Android/");
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            res.append(pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return res.toString();
    }
}
