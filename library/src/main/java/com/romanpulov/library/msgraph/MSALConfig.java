package com.romanpulov.library.msgraph;

public class MSALConfig {
    private int mConfigResId;
    private String[] mScopes;

    public int getConfigResId() {
        return mConfigResId;
    }

    public String[] getScopes() {
        return mScopes;
    }

    private static MSALConfig instance;

    private MSALConfig(int mConfigResId, String[] mScopes) {
        this.mConfigResId = mConfigResId;
        this.mScopes = mScopes;
    }

    public static MSALConfig configure(int configResId, String[] scopes) {
        if (instance == null) {
            instance = new MSALConfig(configResId, scopes);
        } else {
            instance.mConfigResId = configResId;
            instance.mScopes = scopes;
        }

        return instance;
    }

    public static MSALConfig get() {
        if (instance == null) {
            throw new RuntimeException("MSAL configuration not complete. Please, configure first");
        }
        return instance;
    }
}
