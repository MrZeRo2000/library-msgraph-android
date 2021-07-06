package com.romanpulov.library.msgraph;

import android.app.Activity;
import android.content.Context;

import com.microsoft.identity.client.IAuthenticationResult;

public abstract class MSGraphBaseHelper {

    protected abstract void configure();

    public void login(Activity activity, OnMSActionListener<String> callback) {
        (new MSALInteractiveAuthenticationAction(activity, callback)).execute();
    }

    public void logout(Context context, OnMSActionListener<Void> callback) {
        (new MSALSignOutAction(context, callback)).execute();
    }
}
