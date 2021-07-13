package com.romanpulov.library.msgraph.testapp;

import java.util.List;

public interface HrPickerNavigationProcessor {
    void onNavigationSuccess(String path, List<HrPickerItem> items);
    void onNavigationFailure(String path, String errorMessage);
}
