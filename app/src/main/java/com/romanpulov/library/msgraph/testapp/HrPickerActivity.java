package com.romanpulov.library.msgraph.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import android.os.Bundle;
import android.util.Log;

public class HrPickerActivity extends AppCompatActivity {
    public static final String TAG = HrPickerActivity.class.getSimpleName();

    public static final String PICKER_INITIAL_PATH = "ChooserInitialPath";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();

        fm.setFragmentResultListener(
                HrPickerFragment.RESULT_KEY,
                this,
                new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        if (requestKey.equals(HrPickerFragment.RESULT_KEY)) {
                            String result = bundle.getString(HrPickerFragment.RESULT_VALUE_KEY);
                            Log.d(TAG, "Obtained result: " + result);
                        }
                    }
                });

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