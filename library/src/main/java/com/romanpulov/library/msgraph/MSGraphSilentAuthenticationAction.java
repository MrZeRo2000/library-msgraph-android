package com.romanpulov.library.msgraph;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;

public class MSGraphSilentAuthenticationAction extends MSAbstractAccountAppRequiresAction<IAccount> {
    private static final String TAG = MSGraphSilentAuthenticationAction.class.getSimpleName();

    public MSGraphSilentAuthenticationAction(Context context, OnMSActionListener<IAccount> msActionListener) {
        super(context, OnMSActionListener.MSAL_ACTION_SILENT_AUTHENTICATION, msActionListener);
    }

    @Override
    protected void executeWithAccountApp(@NonNull ISingleAccountPublicClientApplication accountApp) {
        accountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback() {
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount) {
                MSALData.mAccount.set(activeAccount);
                Log.d(TAG, "AccountLoaded:" + (activeAccount == null ? "null" : activeAccount.toString()));
                if (mMSActionListener != null) {
                    mMSActionListener.onActionSuccess(mAction, activeAccount);
                }
            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount, IAccount currentAccount) {

            }

            @Override
            public void onError(@NonNull MsalException exception) {
                Log.d(TAG, "AccountLoad error:" + exception.getMessage());
                if (mMSActionListener != null) {
                    mMSActionListener.onActionFailure(mAction, "account load error:" + exception.getMessage());
                }
            }
        });
    }
}
