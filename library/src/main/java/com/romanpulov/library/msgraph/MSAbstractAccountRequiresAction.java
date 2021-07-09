package com.romanpulov.library.msgraph;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;

public abstract class MSAbstractAccountRequiresAction<T> extends MSAbstractAccountAppRequiresAction<T> {
    private static final String TAG = MSAbstractAccountRequiresAction.class.getSimpleName();

    public MSAbstractAccountRequiresAction(Context context, int action, OnMSActionListener<T> msActionListener) {
        super(context, action, msActionListener);
    }

    protected abstract void executeWithAccount(@NonNull IAccount account);

    @Override
    protected final void executeWithAccountApp(@NonNull ISingleAccountPublicClientApplication accountApp) {
        if (MSALData.mAccount.get() == null) {
            Log.d(TAG, "Account does not exist, silently authenticating");
            MSGraphSilentAuthenticationAction authenticationAction = new MSGraphSilentAuthenticationAction(
                    mContext,
                    new OnMSActionListener<IAccount>() {
                        @Override
                        public void onActionSuccess(int action, IAccount data) {
                            if (data == null) {
                                Log.d(TAG, "Account load failure, authentication required");
                                if (mMSActionListener != null) {
                                    mMSActionListener.onActionFailure(mAction, "Authentication required");
                                }
                            } else {
                                Log.d(TAG, "Account obtained successfully, executing action");
                                executeWithAccount(data);
                            }
                        }

                        @Override
                        public void onActionFailure(int action, String errorMessage) {
                            Log.d(TAG, "Error obtaining account: " + errorMessage);
                            if (mMSActionListener != null) {
                                mMSActionListener.onActionFailure(mAction, errorMessage);
                            }
                        }
            });
            authenticationAction.execute();
        } else {
            Log.d(TAG, "Account already exists, executing action");
            executeWithAccount(MSALData.mAccount.get());
        }
    }
}
