package com.romanpulov.library.msgraph.testapp;

public class HrPickerItem {
    public static int ITEM_TYPE_FOLDER = 1;
    public static int ITEM_TYPE_FILE = 2;
    public static int ITEM_TYPE_PARENT = 3;
    public static int ITEM_TYPE_UNKNOWN = 4;

    public final int itemType;
    public final String name;

    public HrPickerItem(int itemType, String name) {
        this.itemType = itemType;
        this.name = name;
    }

    public static HrPickerItem createParentItem() {
        return new HrPickerItem(ITEM_TYPE_PARENT, null);
    }

    public static HrPickerItem createItem(int itemType, String name) {
        if (itemType == ITEM_TYPE_PARENT) {
            throw new RuntimeException("Error creating parent item, use a separate method for that");
        } else {
            return new HrPickerItem(itemType, name);
        }
    }
}
