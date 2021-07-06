package com.romanpulov.library.msgraph;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;

public class MSALSignOutAction extends MSAbstractAccountAppRequiresAction<Void>{
    private static final String TAG = MSALSignOutAction.class.getSimpleName();

    public MSALSignOutAction(Context context, OnMSActionListener<Void> msActionListener) {
        super(context, OnMSActionListener.MSAL_ACTION_LOGOUT, msActionListener);
    }

    @Override
    protected void executeWithAccountApp(@NonNull ISingleAccountPublicClientApplication accountApp) {
        accountApp.signOut(new ISingleAccountPublicClientApplication.SignOutCallback() {
            @Override
            public void onSignOut() {
                Log.d(TAG, "Successfully signed out");
                MSALData.mAccount.set(null);
                if (mMSActionListener != null) {
                    mMSActionListener.onActionSuccess(mAction, null);
                }
            }

            @Override
            public void onError(@NonNull MsalException exception) {
                Log.d(TAG, "Sign out failed: " + exception.toString());
                if (mMSActionListener != null) {
                    mMSActionListener.onActionFailure(mAction, "Sign out: " + exception.toString());
                }
            }
        });
    }
}
