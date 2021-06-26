package com.romanpulov.library.msgraph;

import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;

public abstract class MSGraphBaseHelper {

    protected abstract int getConfigId();

    /* Azure AD Variables */
    private ISingleAccountPublicClientApplication mSingleAccountApp;
    private IAccount mAccount;

    
}
