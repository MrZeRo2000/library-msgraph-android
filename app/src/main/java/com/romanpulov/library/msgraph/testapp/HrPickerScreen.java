package com.romanpulov.library.msgraph.testapp;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HrPickerScreen implements HrPickerNavigationProcessor {
    public final static String TAG = HrPickerScreen.class.getSimpleName();

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

    private List<HrPickerItem> mItems = new ArrayList<>();

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

    public HrPickerScreen(String mCurrentPath) {
        this.mCurrentPath = mCurrentPath;
    }

    public void navigate(Context context, String path, HrPickerItem item) {
        if (mNavigator != null) {
            mErrorMessage = null;
            mStatus = HrPickerScreen.PICKER_SCREEN_STATUS_LOADING;
            mNavigator.onNavigate(context, combinePath(path, item), this);

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

        Log.d(TAG, "currentPath=" + mCurrentPath + ", parentPath=" + mParentPath);

        mItems.clear();
        if (!mParentPath.isEmpty()) {
            mItems.add(new HrPickerItem(HrPickerItem.ITEM_TYPE_PARENT, null));
        }
        mItems.addAll(items);
        Collections.sort(mItems, new HrPickerItemComparator());

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
        if ((iPath < 0) || path.equals("/")) {
            return "";
        } else if (iPath == 0) {
            return "/";
        } else {
            return path.substring(0, iPath);
        }
    }

    public static String combinePath(String path, HrPickerItem item) {
        return ((item == null) || (item.itemType == HrPickerItem.ITEM_TYPE_PARENT)) ?
                path :
                path + (path.endsWith("/") ? "" : "/") + item.name;
    }
}
