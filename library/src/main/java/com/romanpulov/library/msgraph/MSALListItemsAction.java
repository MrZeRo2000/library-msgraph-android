package com.romanpulov.library.msgraph;

import android.content.Context;

import androidx.annotation.NonNull;

import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;

public class MSALListItemsAction extends MSAbstractAuthenticationRequiredAction<String> {
    public MSALListItemsAction(Context context, int action, OnMSActionListener<String> msActionListener) {
        super(context, OnMSActionListener.MSAL_ACTION_LIST_ITEMS, msActionListener);
    }

    @Override
    protected void executeWithAuthenticationResult(@NonNull IAuthenticationResult authenticationResult) {

    }
}
