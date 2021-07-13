package com.romanpulov.library.msgraph;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.microsoft.identity.client.IAuthenticationResult;
import com.romanpulov.jutilscore.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MSALPutFilesAction extends MSAbstractAuthenticationRequiredAction<Void> {
    private static final String TAG = MSALPutFilesAction.class.getSimpleName();

    private final String mPath;
    private final File[] mFiles;

    public MSALPutFilesAction(Context context, String path, File[] files, OnMSActionListener<Void> msActionListener) {
        super(context, OnMSActionListener.MSAL_ACTION_PUT_FILES, msActionListener);
        mPath = path;
        mFiles = files;
    }

    @Override
    protected void executeWithAuthenticationResult(@NonNull IAuthenticationResult authenticationResult) {
        Log.d(TAG, "Executing Graph request");

        if ((mFiles != null) && (mFiles.length > 1)) {
            final AtomicInteger fileCounter = new AtomicInteger(mFiles.length);
            final AtomicBoolean errorFlag = new AtomicBoolean(false);

            for (File f: mFiles) {
                Log.d(TAG, "Processing file: " + f.getName());

                try (
                        InputStream inputStream = new FileInputStream(f);
                        ByteArrayOutputStream outputStreamStream = new ByteArrayOutputStream();
                ) {

                    FileUtils.copyStream(inputStream, outputStreamStream);

                    String url = "https://graph.microsoft.com/v1.0/me/drive/root:" + mPath + "/" + f.getName() + ":/content";
                    Log.d(TAG, "Getting url: " + url);

                    MSGraphRequestWrapper.callGraphAPIPutUsingVolley(
                            mContext,
                            url,
                            authenticationResult.getAccessToken(),
                            outputStreamStream.toByteArray(),
                            new Response.Listener<String>() {
                                @Override
                                synchronized public void onResponse(String response) {
                                    Log.d(TAG, "Got response from Graph, counter=" + fileCounter.get());
                                    if ((fileCounter.decrementAndGet() == 0) && (mMSActionListener != null) && (!errorFlag.get())) {
                                        Log.d(TAG, "Invoking success");
                                        mMSActionListener.onActionSuccess(mAction, null);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                synchronized public void onErrorResponse(VolleyError error) {
                                    Log.d(TAG, "Error response Graph: " + MSGraphRequestWrapper.getErrorResponseBody(error));
                                    if ((!errorFlag.getAndSet(true)) && (mMSActionListener != null)) {
                                        Log.d(TAG, "Invoking error failure");
                                        mMSActionListener.onActionFailure(mAction, MSGraphRequestWrapper.getErrorResponseBody(error));
                                    }
                                }
                            }
                    );
                } catch (IOException e) {
                    if ((!errorFlag.getAndSet(true)) && (mMSActionListener != null)) {
                        Log.d(TAG, "IOException: Invoking error failure");
                        mMSActionListener.onActionFailure(mAction, e.getMessage());
                    }
                }
            }

        } else {
            if (mMSActionListener != null) {
                mMSActionListener.onActionSuccess(mAction, null);
            }
        }
    }
}
