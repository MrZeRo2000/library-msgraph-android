package com.romanpulov.library.msgraph;

import android.content.Context;

import androidx.annotation.NonNull;

import com.microsoft.identity.client.ISingleAccountPublicClientApplication;

public abstract class MSAbstractAccountAppRequiresAction<T> extends MSAbstractAction <T>{
    private final Context mContext;

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
            MSALCreateAccountAppAction createAccountAppAction = new MSALCreateAccountAppAction(
                    mContext,
                    new OnMSActionListener<ISingleAccountPublicClientApplication>() {
                        @Override
                        public void onActionSuccess(int action, ISingleAccountPublicClientApplication accountApp) {
                            executeWithAccountApp(accountApp);
                        }

                        @Override
                        public void onActionFailure(int action, String errorMessage) {
                            if (mMSActionListener != null) {
                                mMSActionListener.onActionFailure(mAction, errorMessage);
                            }
                        }
                    });
            createAccountAppAction.execute();
        } else {
            executeWithAccountApp(MSALData.mSingleAccountApp.get());
        }
    }
}
