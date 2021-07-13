package com.romanpulov.library.msgraph.testapp;

import android.content.Context;
import android.util.Log;

import com.romanpulov.library.msgraph.MSALConfig;
import com.romanpulov.library.msgraph.MSGraphBaseHelper;
import com.romanpulov.library.msgraph.OnMSActionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MSGraphHelper extends MSGraphBaseHelper implements HrPickerNavigator {
    private static final String TAG = MSGraphHelper.class.getSimpleName();

    @Override
    protected void configure() {
        MSALConfig.configure(R.raw.auth_config_single_account, "Files.ReadWrite.All".toLowerCase().split(" "));
    }

    private static MSGraphHelper instance;

    private AtomicBoolean mNavigating = new AtomicBoolean(false);

    public static MSGraphHelper getInstance() {
        if (instance == null) {
            instance = new MSGraphHelper();
            instance.configure();
        }

        return instance;
    }

    private MSGraphHelper() {

    }

    private static List<HrPickerItem> parseJSONObject(JSONObject data) throws JSONException {
        List<HrPickerItem> result = new ArrayList<>();

        JSONArray jsonItems = data.getJSONArray("value");
        for (int i = 0; i < jsonItems.length(); i ++) {
            JSONObject o = (JSONObject) jsonItems.get(i);

            int itemType;

            if (!o.isNull("folder")) {
                itemType = HrPickerItem.ITEM_TYPE_FOLDER;
            } else if (!o.isNull("file")) {
                itemType = HrPickerItem.ITEM_TYPE_FILE;
            } else {
                itemType = HrPickerItem.ITEM_TYPE_UNKNOWN;
            }

            result.add(HrPickerItem.createItem(itemType, o.getString("name")));
        }

        return result;
    }

    @Override
    synchronized public void onNavigate(Context context, String path, HrPickerItem item, HrPickerNavigationProcessor processor) {
        if (!mNavigating.get()) {
            mNavigating.set(true);

            String navigationPath = path + (item == null ? "" : item.name);
            Log.d(TAG, "Navigating to path: " + navigationPath);

            listItems(
                    context,
                    navigationPath,
                    new OnMSActionListener<JSONObject>() {
                        @Override
                        public void onActionSuccess(int action, JSONObject data) {
                            Log.d(TAG, "Obtained data:" + data.toString());
                            try {
                                processor.onNavigationSuccess(path, parseJSONObject(data));
                            } catch (JSONException e) {
                                processor.onNavigationFailure(path, "Error parsing response: " + e.getMessage());
                            }
                        }

                        @Override
                        public void onActionFailure(int action, String errorMessage) {
                            Log.d(TAG, "Navigation error, passing error message: " + errorMessage);
                            processor.onNavigationFailure(path, errorMessage);
                        }
                    });
        }
    }
}
