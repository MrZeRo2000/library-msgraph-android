package com.romanpulov.library.msgraph;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.microsoft.identity.client.IAuthenticationResult;

public class MSALPutBytesByPathAction extends MSAbstractAuthenticationRequiresAction<String> {
    private static final String TAG = MSALPutBytesByPathAction.class.getSimpleName();

    private final String mPath;
    private final byte[] mData;

    public MSALPutBytesByPathAction(Context context, String path, byte[] data, OnMSActionListener<String> msActionListener) {
        super(context, OnMSActionListener.MSAL_ACTION_PUT_BYTES_BY_PATH, msActionListener);
        mPath = path;
        mData = data;
    }

    @Override
    protected void executeWithAuthenticationResult(@NonNull IAuthenticationResult authenticationResult) {
        Log.d(TAG, "Executing Graph request");

        MSGraphRequestWrapper.callGraphAPIPutUsingVolley(
                mContext,
                "https://graph.microsoft.com/v1.0/me/drive/root:" + mPath + ":/content",
                authenticationResult.getAccessToken(),
                mData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
