package com.romanpulov.library.msgraph;

import android.app.Activity;
import android.content.Context;

import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;

public abstract class MSGraphBaseHelper {

    protected abstract void configure();

    public void login(Activity activity, OnMSActionListener<String> callback) {
        MSActionExecutor.execute((new MSALInteractiveAuthenticationAction(activity, callback)));
    }

    public void logout(Context context, OnMSActionListener<Void> callback) {
        MSActionExecutor.execute((new MSALSignOutAction(context, callback)));
    }

    public void listItems(Context context, String path, OnMSActionListener<JSONObject> callback) {
        MSActionExecutor.execute((new MSALListItemsAction(context, path, callback)));
    }

    public void getBytesByPath(Context context, String path, OnMSActionListener<byte[]> callback) {
        MSActionExecutor.execute((new MSALGetBytesByPathAction(context, path, callback)));
    }

    public void putBytesByPath(Context context, String path, byte[] data, OnMSActionListener<String> callback) {
        MSActionExecutor.execute((new MSALPutBytesByPathAction(context, path, data, callback)));
    }

    public void putFiles(Context context, String path, File[] files, OnMSActionListener<Void> callback) {
        MSActionExecutor.execute((new MSALPutFilesAction(context, path, files, callback)));
    }

    public JSONObject listItemsSync(Context context) throws MSActionException {
        return MSActionExecutor.executeSync(new MSALListItemsAction(context, "path", null));
    }
}
