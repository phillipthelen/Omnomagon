package net.pherth.omnomagon.data;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import net.pherth.omnomagon.R;

public enum PriceGroup {
    students(0), employees(1), guests(2);

    private final int _typeInteger;

    PriceGroup(int typeInteger) {
        _typeInteger = typeInteger;
    }

    public int getTypeInteger() {
        return _typeInteger;
    }

    @NonNull
    public static PriceGroup findGroupForId(@StringRes int id) {
        final PriceGroup priceGroup;
        if (id == R.string.price_students) {
            priceGroup = students;
        } else if (id == R.string.price_employees) {
            priceGroup = employees;
        } else {
            priceGroup = guests;
        }
        return priceGroup;
    }
}
