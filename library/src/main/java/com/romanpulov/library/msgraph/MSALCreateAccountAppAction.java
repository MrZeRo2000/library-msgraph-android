package com.romanpulov.library.msgraph;

import android.content.Context;
import android.util.Log;

import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;

public class MSALCreateAccountAppAction extends MSAbstractAction<ISingleAccountPublicClientApplication> {
    private static final String TAG = MSALCreateAccountAppAction.class.getSimpleName();

    private final Context context;

    public MSALCreateAccountAppAction(
            Context context,
            OnMSActionListener<ISingleAccountPublicClientApplication> msActionListener
    ) {
        super(OnMSActionListener.MSAL_ACTION_CREATE_ACCOUNT, msActionListener);
        this.context = context;
    }

    @Override
    public void execute() {
        PublicClientApplication.createSingleAccountPublicClientApplication(
                this.context.getApplicationContext(),
                MSALConfig.get().getConfigResId(),
                new IPublicClientApplication.ISingleAccountApplicationCreatedListener() {
                    @Override
                    public void onCreated(ISingleAccountPublicClientApplication application) {
                        /**
                         * This test app assumes that the app is only going to support one account.
                         * This requires "account_mode" : "SINGLE" in the config json file.
                         **/
                        Log.d(TAG, "singleAccountApp created");
                        MSALData.mSingleAccountApp.set(application);
                        if (mMSActionListener != null) {
                            mMSActionListener.onActionSuccess(mAction, application);
                        }
                    }

                    @Override
                    public void onError(MsalException exception) {
                        Log.d(TAG, "singleAccountApp not created:" + exception.getMessage());
                        MSALData.mSingleAccountApp.set(null);
                        if (mMSActionListener != null) {
                            mMSActionListener.onActionFailure(mAction, exception.getMessage());
                        }
                    }
                });
    }
}
