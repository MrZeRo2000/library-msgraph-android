package com.romanpulov.library.msgraph;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.microsoft.identity.client.IAuthenticationResult;

public class MSALGetBytesByPathAction extends MSAbstractAuthenticationRequiresAction<byte[]> {
    private static final String TAG = MSALGetBytesByPathAction.class.getSimpleName();

    private final String mPath;

    public MSALGetBytesByPathAction(Context context, String path, OnMSActionListener<byte[]> msActionListener) {
        super(context, OnMSActionListener.MSAL_ACTION_GET_BYTES_BY_PATH, msActionListener);
        mPath = path;
    }

    @Override
    protected void executeWithAuthenticationResult(@NonNull IAuthenticationResult authenticationResult) {
        Log.d(TAG, "Executing Graph request");

        MSGraphRequestWrapper.callGraphAPIByteUsingVolley(
                mContext,
                "https://graph.microsoft.com/v1.0/me/drive/root:" + mPath + ":/content",
                authenticationResult.getAccessToken(),
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        Log.d(TAG, "Got response from Graph");
                        if (mMSActionListener != null) {
                            mMSActionListener.onActionSuccess(mAction, response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error response Graph: " + MSGraphRequestWrapper.getErrorResponseBody(error));
                        if (mMSActionListener != null) {
                            mMSActionListener.onActionFailure(mAction, MSGraphRequestWrapper.getErrorResponseBody(error));
                        }
                    }
                }
        );
    }
}
