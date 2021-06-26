package com.romanpulov.library.msgraph;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;

public class MSALLoadAccountAction extends MSAbstractAccountAppRequiresAction<IAccount> {
    private static final String TAG = MSALLoadAccountAction.class.getSimpleName();

    public MSALLoadAccountAction(
            Context context,
            OnMSActionListener<IAccount> msActionListener
    ) {
        super(context, OnMSActionListener.MSAL_ACTION_LOAD_ACCOUNT, msActionListener);
    }

    @Override
    protected void executeWithAccountApp(@NonNull ISingleAccountPublicClientApplication singleAccountApp) {
        singleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback() {
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount) {
                // You can use the account data to update your UI or your app database.
                Log.d(TAG, "AccountLoaded:" + (activeAccount == null ? "null" : activeAccount.toString()));
                //updateUI();
                MSALData.mAccount.set(activeAccount);
                if (mMSActionListener != null) {
                    mMSActionListener.onActionSuccess(mAction, activeAccount);
                }
            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount, @Nullable IAccount currentAccount) {
                if (currentAccount == null) {
                    // Perform a cleanup task as the signed-in account changed.
                    //showToastOnSignOut();
                    MSALData.mAccount.set(null);
                }
            }

            @Override
            public void onError(@NonNull MsalException exception) {
                //displayError(exception);
                Log.d(TAG, "AccountLoad error:" + exception.getMessage());
                if (mMSActionListener != null) {
                    mMSActionListener.onActionFailure(mAction, exception.getMessage());
                }
            }
        });
    }
}
