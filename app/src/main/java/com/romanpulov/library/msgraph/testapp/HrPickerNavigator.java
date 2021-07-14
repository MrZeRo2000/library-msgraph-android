package com.romanpulov.library.msgraph.testapp;

import android.content.Context;

public interface HrPickerNavigator {
    void onNavigate(Context context, String path, HrPickerNavigationProcessor processor);
}
