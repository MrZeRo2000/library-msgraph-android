package com.romanpulov.library.msgraph;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.microsoft.identity.client.IAuthenticationResult;

import org.json.JSONObject;

public class MSALListItemsAction extends MSAbstractAuthenticationRequiresAction<JSONObject> {
    private static final String TAG = MSALListItemsAction.class.getSimpleName();

    private final String mPath;

    public MSALListItemsAction(Context context, String path, OnMSActionListener<JSONObject> msActionListener) {
        super(context, OnMSActionListener.MSAL_ACTION_LIST_ITEMS, msActionListener);
        this.mPath = path;
    }

    @Override
    protected void executeWithAuthenticationResult(@NonNull IAuthenticationResult authenticationResult) {
        Log.d(TAG, "Executing Graph request");

        String path = mPath.equals("/") ? "" : ":" + mPath + ":";

        MSGraphRequestWrapper.callGraphAPIUsingVolley(
                mContext,
                "https://graph.microsoft.com/v1.0/me/drive/root" + path + "/children",
                authenticationResult.getAccessToken(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
