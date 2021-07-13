package com.romanpulov.library.msgraph.testapp;

import java.util.Comparator;

public class HrPickerItemComparator implements Comparator<HrPickerItem> {
    @Override
    public int compare(HrPickerItem o1, HrPickerItem o2) {
        if (o1 == o2)
            return 0;
        if (o1 == null)
            return -1;
        if (o2 == null)
            return 1;
        if (o1.itemType > o2.itemType)
            return 1;
        if (o1.itemType < o2.itemType)
            return -1;
        return o1.name.compareTo(o2.name);
    }
}
