package com.romanpulov.library.msgraph.testapp;

public class HrPickerItem {
    public static int ITEM_TYPE_FOLDER = 1;
    public static int ITEM_TYPE_FILE = 2;
    public static int ITEM_TYPE_PARENT = 3;

    public final int itemType;
    public final String name;

    public HrPickerItem(int itemType, String name) {
        this.itemType = itemType;
        this.name = name;
    }
}
