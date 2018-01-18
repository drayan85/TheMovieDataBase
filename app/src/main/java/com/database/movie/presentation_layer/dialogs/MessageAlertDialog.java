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
package com.database.movie.presentation_layer.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.database.movie.R;

/**
 * Display the Alert Dialog with the message that has been passed as argument.
 *
 * @author Ilanthirayan Paramanathan <theebankala@gmail.com>
 * @version 1.0.0
 * @since 12th of January 2018
 */
public class MessageAlertDialog extends DialogFragment {


    public static final String TAG = MessageAlertDialog.class.getSimpleName();

    private MessageAlertDialogCallback mMessageAlertDialogCallback;
    private String mMessage;

    public static MessageAlertDialog getInstance(String message, MessageAlertDialogCallback dialogCallback){
        MessageAlertDialog messageAlertDialog = new MessageAlertDialog();
        messageAlertDialog.mMessage = message;
        messageAlertDialog.mMessageAlertDialogCallback = dialogCallback;
        return messageAlertDialog;
    }

    public interface MessageAlertDialogCallback {

        void userClickedConfirmed();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getActivity());
        dlgAlert.setMessage(mMessage);
        dlgAlert.setTitle(getResources().getString(R.string.app_name));
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton(getResources().getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mMessageAlertDialogCallback.userClickedConfirmed();
                    }
                });
        return dlgAlert.create();
    }
}
