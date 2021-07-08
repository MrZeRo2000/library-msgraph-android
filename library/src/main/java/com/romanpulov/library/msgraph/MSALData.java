package com.romanpulov.library.msgraph;

import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;

import java.util.concurrent.atomic.AtomicReference;

public class MSALData {
    final static AtomicReference<ISingleAccountPublicClientApplication> mSingleAccountApp = new AtomicReference<>();
    final static AtomicReference<IAccount> mAccount = new AtomicReference<>();
    final static AtomicReference<IAuthenticationResult> mAuthenticationResult = new AtomicReference<>();
}
