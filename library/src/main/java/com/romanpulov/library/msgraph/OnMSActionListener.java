package com.romanpulov.library.msgraph;

public interface OnMSActionListener<D> {
    int MSAL_ACTION_LOGIN = 0;
    int MSAL_ACTION_LOGOUT = 1;
    int MSAL_ACTION_CREATE_ACCOUNT = 2;
    int MSAL_ACTION_LOAD_ACCOUNT = 3;
    int MSAL_ACTION_INTERACTIVE_AUTHENTICATION = 4;
    int MSAL_ACTION_SILENT_AUTHENTICATION = 5;
    int MSAL_ACTION_LIST_ITEMS = 6;
    int MSAL_ACTION_SILENT_ACQUIRE_TOKEN = 7;
    int MSAL_ACTION_GET_BYTES_BY_PATH = 8;
    int MSAL_ACTION_PUT_BYTES_BY_PATH = 9;

    void onActionSuccess(int action, D data);
    void onActionFailure(int action, String errorMessage);
}
