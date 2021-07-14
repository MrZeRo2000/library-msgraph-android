package com.romanpulov.library.msgraph;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;

public abstract class MSAbstractAuthenticationRequiresAction<T> extends MSAbstractAccountRequiresAction<T> {
    private static final String TAG = MSAbstractAuthenticationRequiresAction.class.getSimpleName();

    public MSAbstractAuthenticationRequiresAction(Context context, int action, OnMSActionListener<T> msActionListener) {
        super(context, action, msActionListener);
    }

    protected abstract void executeWithAuthenticationResult(@NonNull IAuthenticationResult authenticationResult);

    @Override
    protected final void executeWithAccount(@NonNull IAccount account) {
        if (MSALData.mAuthenticationResult.get() == null) {
            Log.d(TAG, "Token does not exist, acquiring");
            MSALSilentAcquireTokenAction acquireTokenAction = new MSALSilentAcquireTokenAction(
                    mContext,
                    new OnMSActionListener<IAuthenticationResult>() {
                        @Override
                        public void onActionSuccess(int action, IAuthenticationResult data) {
                            Log.d(TAG, "Successfully obtained token, executing action");
                            MSALData.mAuthenticationResult.set(data);
                            executeWithAuthenticationResult(data);
                        }

                        @Override
                        public void onActionFailure(int action, String errorMessage) {
                            Log.d(TAG, "Error obtaining token: " + errorMessage);
                            if (mMSActionListener != null) {
                                mMSActionListener.onActionFailure(mAction, errorMessage);
                            }
                        }
                    }
            );
            acquireTokenAction.execute();
        } else {
            Log.d(TAG, "Token already exists, executing action");
            executeWithAuthenticationResult(MSALData.mAuthenticationResult.get());
        }
    };
}
