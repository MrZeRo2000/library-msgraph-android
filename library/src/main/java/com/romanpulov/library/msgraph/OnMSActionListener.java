package com.romanpulov.library.msgraph;

public interface OnMSActionListener<D> {
    public static final int MSAL_ACTION_LOGIN = 0;
    public static final int MSAL_ACTION_LOGOUT = 1;
    public static final int MSAL_ACTION_CREATE_ACCOUNT = 2;
    public static final int MSAL_ACTION_LOAD_ACCOUNT = 3;
    public static final int MSAL_ACTION_INTERACTIVE_AUTHENTICATION = 4;
    public static final int MSAL_ACTION_SILENT_AUTHENTICATION = 5;
    public static final int MSAL_ACTION_LIST_ITEMS = 6;
    public static final int MSAL_ACTION_SILENT_ACQUIRE_TOKEN = 7;

    void onActionSuccess(int action, D data);
    void onActionFailure(int action, String errorMessage);
}
