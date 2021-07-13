package com.romanpulov.library.msgraph.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class HrPickerActivity extends AppCompatActivity {
    public static final String PICKER_INITIAL_PATH = "ChooserInitialPath";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(android.R.id.content);
        if (fragment == null) {
            String initialPath = getIntent().getStringExtra(PICKER_INITIAL_PATH);

            HrPickerFragment hrPickerFragment = HrPickerFragment.newInstance("/");

            HrPickerScreen oneDrivePickerScreen = new HrPickerScreen("/");
            oneDrivePickerScreen.setNavigator(MSGraphHelper.getInstance());

            hrPickerFragment.setPickerScreen(oneDrivePickerScreen);

            if (hrPickerFragment != null) {
                fm.beginTransaction().add(android.R.id.content, hrPickerFragment).commit();
            }
        }

    }
}