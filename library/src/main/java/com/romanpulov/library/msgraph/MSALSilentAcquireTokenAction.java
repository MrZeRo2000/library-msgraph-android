package com.romanpulov.library.msgraph;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.SilentAuthenticationCallback;
import com.microsoft.identity.client.exception.MsalException;

public class MSALSilentAcquireTokenAction extends MSAbstractAccountRequiresAction <IAuthenticationResult>{
    private static final String TAG = MSALSilentAcquireTokenAction.class.getSimpleName();

    public MSALSilentAcquireTokenAction(Context context, OnMSActionListener<IAuthenticationResult> msActionListener) {
        super(context, OnMSActionListener.MSAL_ACTION_SILENT_ACQUIRE_TOKEN, msActionListener);
    }

    @Override
    protected void executeWithAccount(@NonNull IAccount account) {
        MSALData.mSingleAccountApp.get().acquireTokenSilentAsync(
                MSALConfig.get().getScopes(),
                account.getAuthority(),
                new SilentAuthenticationCallback() {
                    @Override
                    public void onSuccess(IAuthenticationResult authenticationResult) {
                        Log.d(TAG, "Successfully acquired token");
                        MSALData.mAuthenticationResult.set(authenticationResult);
                        if (mMSActionListener != null) {
                            mMSActionListener.onActionSuccess(mAction, authenticationResult);
                        }
                    }

                    @Override
                    public void onError(MsalException exception) {
                        Log.d(TAG, "Token acquire error: " + exception.getMessage());
                        if (mMSActionListener != null) {
                            mMSActionListener.onActionFailure(mAction, exception.getMessage());
                        }
                    }
                }
        );
    }
}
