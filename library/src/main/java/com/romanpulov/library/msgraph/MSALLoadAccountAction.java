package com.romanpulov.library.msgraph;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.microsoft.identity.client.IAuthenticationResult;

public class MSALLoadAccountAction extends MSAbstractAuthenticationRequiresAction<Void> {
    private static final String TAG = MSALLoadAccountAction.class.getSimpleName();

    public MSALLoadAccountAction(
            Context context,
            OnMSActionListener<Void> msActionListener
    ) {
        super(context, OnMSActionListener.MSAL_ACTION_LOAD_ACCOUNT, msActionListener);
    }

    @Override
    protected void executeWithAuthenticationResult(@NonNull IAuthenticationResult authenticationResult) {
        Log.d(TAG, "Successfully obtained token: " + authenticationResult.getAccessToken());
        if (mMSActionListener != null) {
            mMSActionListener.onActionSuccess(mAction, null);
        }
    }
}
