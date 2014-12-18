package net.pherth.omnomagon.settings;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.View;

public class PopupMenuHelper {

    private PopupMenuHelper() { }

    public static void configurePopupMenu(@NonNull View buttonView, @ArrayRes int values, @NonNull PopupMenu.OnMenuItemClickListener popupMenuListener) {
        final Context context = buttonView.getContext();
        final PopupMenu popupMenu = new PopupMenu(context, buttonView);
        popupMenu.setOnMenuItemClickListener(popupMenuListener);
        final Menu menu = popupMenu.getMenu();
        addMenuItems(context, menu, values);
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });
    }

    private static void addMenuItems(@NonNull Context context, @NonNull Menu popupMenu, @ArrayRes int valueResource) {
        final Resources resources = context.getResources();
        final TypedArray typedArray = resources.obtainTypedArray(valueResource);
        final int length = typedArray.length();
        for (int i = 0; i<length; i++) {
            final int id = typedArray.getResourceId(i, 0);
            final String value = typedArray.getString(i);
            popupMenu.add(0, id, i, value);
        }
    }
}
