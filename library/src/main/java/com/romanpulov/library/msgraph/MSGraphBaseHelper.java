package com.romanpulov.library.msgraph;

import android.app.Activity;
import android.content.Context;

import org.json.JSONObject;

import java.io.InputStream;

public abstract class MSGraphBaseHelper {

    protected abstract void configure();

    public void login(Activity activity, OnMSActionListener<String> callback) {
        (new MSALInteractiveAuthenticationAction(activity, callback)).execute();
    }

    public void logout(Context context, OnMSActionListener<Void> callback) {
        (new MSALSignOutAction(context, callback)).execute();
    }

    public void listItems(Context context, String path, OnMSActionListener<JSONObject> callback) {
        (new MSALListItemsAction(context, path, callback)).execute();
    }

    public void getBytesByPath(Context context, String path, OnMSActionListener<byte[]> callback) {
        (new MSALGetBytesByPathAction(context, path, callback)).execute();
    }

    public JSONObject listItemsSync(Context context) throws MSActionException {
        return MSActionExecutor.executeSync(new MSALListItemsAction(context, "path", null));
    }
}
