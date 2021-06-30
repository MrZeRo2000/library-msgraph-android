package com.romanpulov.library.msgraph.testapp;

import com.romanpulov.library.msgraph.MSALConfig;
import com.romanpulov.library.msgraph.MSGraphBaseHelper;

public class MSGraphHelper extends MSGraphBaseHelper {
    @Override
    protected void configure() {
        MSALConfig.configure(R.raw.auth_config_single_account, "Files.ReadWrite.All".toLowerCase().split(" "));
    }

    private static MSGraphHelper instance;

    public static MSGraphHelper getInstance() {
        if (instance == null) {
            instance = new MSGraphHelper();
            instance.configure();
        }

        return instance;
    }

    private MSGraphHelper() {

    }

}
