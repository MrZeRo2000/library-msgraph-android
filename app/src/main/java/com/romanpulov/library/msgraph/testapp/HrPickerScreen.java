package com.romanpulov.library.msgraph.testapp;

import java.util.List;

public class HrPickerScreen implements HrPickerNavigationProcessor {
    public final static int PICKER_SCREEN_STATUS_READY = 0;
    public final static int PICKER_SCREEN_STATUS_LOADING = 1;
    public final static int PICKER_SCREEN_STATUS_ERROR = 2;

    public interface OnHrPickerScreenUpdateListener {
        void onUpdate(HrPickerScreen hrPickerScreen);
    }

    private int mStatus;

    public int getStatus() {
        return mStatus;
    }

    private String mCurrentPath;

    public String getCurrentPath() {
        return mCurrentPath;
    }

    private String mParentPath;

    public String getParentPath() {
        return mParentPath;
    }

    private List<HrPickerItem> mItems;

    public List<HrPickerItem> getItems() {
        return mItems;
    }

    private String mErrorMessage;

    public String getErrorMessage() {
        return mErrorMessage;
    }

    private HrPickerNavigator mNavigator;

    public HrPickerNavigator getNavigator() {
        return mNavigator;
    }

    public void setNavigator(HrPickerNavigator mNavigator) {
        this.mNavigator = mNavigator;
    }

    private OnHrPickerScreenUpdateListener mPickerScreenUpdateListener;

    public OnHrPickerScreenUpdateListener getPickerScreenUpdateListener() {
        return mPickerScreenUpdateListener;
    }

    public void setPickerScreenUpdateListener(OnHrPickerScreenUpdateListener mPickerScreenUpdateListener) {
        this.mPickerScreenUpdateListener = mPickerScreenUpdateListener;
    }

    public void navigate(String path, HrPickerItem item) {
        if (mNavigator != null) {
            mErrorMessage = null;
            mStatus = HrPickerScreen.PICKER_SCREEN_STATUS_LOADING;
            mNavigator.onNavigate(path, item, this);

            if (mPickerScreenUpdateListener != null) {
                mPickerScreenUpdateListener.onUpdate(this);
            }
        }
    }

    @Override
    public void onNavigationSuccess(String path, List<HrPickerItem> items) {
        mStatus = HrPickerScreen.PICKER_SCREEN_STATUS_READY;
        mErrorMessage = null;
        mCurrentPath = path;
        mParentPath = getParentFromPath(mCurrentPath);
        mItems = items;

        if (mPickerScreenUpdateListener != null) {
            mPickerScreenUpdateListener.onUpdate(this);
        }
    }

    @Override
    public void onNavigationFailure(String path, String errorMessage) {
        mStatus = HrPickerScreen.PICKER_SCREEN_STATUS_ERROR;
        mErrorMessage = errorMessage;

        if (mPickerScreenUpdateListener != null) {
            mPickerScreenUpdateListener.onUpdate(this);
        }
    }

    public static String getParentFromPath(String path) {
        int iPath = path.lastIndexOf("/");
        if (iPath < 0) {
            return "";
        } else if (iPath == 0) {
            return "/";
        } else {
            return path.substring(0, iPath);
        }
    }
}
