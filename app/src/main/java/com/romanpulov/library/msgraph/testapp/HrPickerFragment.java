package com.romanpulov.library.msgraph.testapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HrPickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HrPickerFragment extends Fragment implements HrPickerScreen.OnHrPickerScreenUpdateListener {
    private static final String TAG = HrPickerFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String INITIAL_PATH = "INITIAL_PATH";

    // TODO: Rename and change types of parameters
    private String mInitialPath;
    private boolean mIsEmpty = true;

    private HrPickerScreen mPickerScreen;

    public HrPickerScreen getPickerScreen() {
        return mPickerScreen;
    }

    public void setPickerScreen(HrPickerScreen mPickerScreen) {
        this.mPickerScreen = mPickerScreen;
    }

    @Override
    public void onUpdate(HrPickerScreen hrPickerScreen) {
        Log.d(TAG, "onUpdate");
        mIsEmpty = false;
    }

    public HrPickerFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static HrPickerFragment newInstance(String initialPath) {
        HrPickerFragment fragment = new HrPickerFragment();
        Bundle args = new Bundle();
        args.putString("InitialPath", initialPath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mInitialPath = getArguments().getString(INITIAL_PATH);
        }
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_hr_picker, container, false);

        mPickerScreen.setPickerScreenUpdateListener(this);

        if (mIsEmpty && getContext() != null) {
            Log.d(TAG, "The fragment is empty, navigating");
            mPickerScreen.navigate(getContext(), mPickerScreen.getCurrentPath(), null);
        }

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}