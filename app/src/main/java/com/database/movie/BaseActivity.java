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
package com.database.movie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.database.movie.di.components.ApplicationComponent;
import com.database.movie.presentation_layer.dialogs.MessageAlertDialog;
import com.database.movie.utils.Utils;

/**
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 12th of January 2018
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    @CallSuper
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        resolveDaggerDependency();
        //To be used by child activities
    }

    protected void resolveDaggerDependency() { }

    protected void showBackArrow(){
        ActionBar supportActionBar = getSupportActionBar();
        if(supportActionBar != null){
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
    }

    protected void showHamburger(){
        ActionBar supportActionBar = getSupportActionBar();
        if(supportActionBar != null){
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
    }

    public void showDialog(String message){
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    public void hideDialog(){
        if(mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }


    public ApplicationComponent getApplicationComponent(){
        return ((TheMovieDBApplication)getApplication()).getApplicationComponent();
    }

    public boolean isInternetAvailable() {
        return Utils.isInternetAvailable(this);
    }


    public void displayErrorAlertBox(String message) {
        hideDialog();
        MessageAlertDialog.getInstance(message, new MessageAlertDialog.MessageAlertDialogCallback() {
            @Override
            public void userClickedConfirmed() {

            }
        }).show(getSupportFragmentManager(), MessageAlertDialog.TAG);
    }

    public void displayToastMessage(String message) {
        hideDialog();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
