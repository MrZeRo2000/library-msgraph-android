package com.romanpulov.library.msgraph;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.microsoft.identity.client.ISingleAccountPublicClientApplication;

public abstract class MSAbstractAccountAppRequiresAction<T> extends MSAbstractAction <T>{
    private static final String TAG = MSAbstractAccountAppRequiresAction.class.getSimpleName();

    protected final Context mContext;

    public MSAbstractAccountAppRequiresAction(
            Context context,
            int action,
            OnMSActionListener<T> msActionListener
    ) {
        super(action, msActionListener);
        this.mContext = context;
    }

    protected abstract void executeWithAccountApp(@NonNull ISingleAccountPublicClientApplication accountApp);

    @Override
    public final void execute() {
        if (MSALData.mSingleAccountApp.get() == null) {
            Log.d(TAG, "Account app does not exist, creating");
            MSALCreateAccountAppAction createAccountAppAction = new MSALCreateAccountAppAction(
                    mContext,
                    new OnMSActionListener<ISingleAccountPublicClientApplication>() {
                        @Override
                        public void onActionSuccess(int action, ISingleAccountPublicClientApplication accountApp) {
                            Log.d(TAG, "Account app created successfully, executing action");
                            executeWithAccountApp(accountApp);
                        }

                        @Override
                        public void onActionFailure(int action, String errorMessage) {
                            Log.d(TAG, "Error creating account app: " + errorMessage);
                            if (mMSActionListener != null) {
                                mMSActionListener.onActionFailure(mAction, errorMessage);
                            }
                        }
                    });
            createAccountAppAction.execute();
        } else {
            Log.d(TAG, "Account app already exists executing action");
            executeWithAccountApp(MSALData.mSingleAccountApp.get());
        }
    }
}
