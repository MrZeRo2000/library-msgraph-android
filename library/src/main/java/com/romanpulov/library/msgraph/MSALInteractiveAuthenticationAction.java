package com.romanpulov.library.msgraph;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.exception.MsalClientException;
import com.microsoft.identity.client.exception.MsalException;
import com.microsoft.identity.client.exception.MsalServiceException;

public class MSALInteractiveAuthenticationAction extends MSAbstractAccountAppRequiresAction<IAuthenticationResult> {
    private static final String TAG = MSALInteractiveAuthenticationAction.class.getSimpleName();

    private final Activity mActivity;

    public MSALInteractiveAuthenticationAction(
            Activity activity,
            ISingleAccountPublicClientApplication singleAccountApp,
            OnMSActionListener<IAuthenticationResult> msActionListener
    ) {
        super(activity.getApplicationContext(), OnMSActionListener.MSAL_ACTION_INTERACTIVE_AUTHENTICATION, msActionListener);
        this.mActivity = activity;
    }

    @Override
    protected void executeWithAccountApp(@NonNull ISingleAccountPublicClientApplication accountApp) {
        accountApp.signIn(mActivity, null, MSALConfig.get().getScopes(), new AuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                Log.d(TAG, "Successfully authenticated");
                MSALData.mAccount.set(authenticationResult.getAccount());
                if (mMSActionListener != null) {
                    mMSActionListener.onActionSuccess(mAction, authenticationResult);
                }
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());
                if (mMSActionListener != null) {
                    mMSActionListener.onActionFailure(mAction, "Authentication failed: " + exception.toString());
                }

                if (exception instanceof MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                    Log.d(TAG, "Exception inside MSAL ");
                } else if (exception instanceof MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                    Log.d(TAG, "Exception when communicating with the STS, likely config issue");
                }
            }

            @Override
            public void onCancel() {
                /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.");
                if (mMSActionListener != null) {
                    mMSActionListener.onActionFailure(mAction, "User cancelled login.");
                }
            }
        });
    }
}
