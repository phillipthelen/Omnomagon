package net.pherth.omnomagon.data;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import net.pherth.omnomagon.R;

public enum MealGroup {

    Aktionsstand, Beilagen, Desserts, Essen, Salate, Suppen, Vorspeisen;

    @DrawableRes
    public static int getImageResourceFor(@NonNull MealGroup mealGroup) {
        final int imageResourceId;
        if (mealGroup == Aktionsstand) {
            imageResourceId = R.drawable.ic_list_special;
        } else if (mealGroup == Beilagen) {
            imageResourceId = R.drawable.ic_list_supplement;
        } else if (mealGroup == Desserts) {
            imageResourceId = R.drawable.ic_list_dessert;
        } else if (mealGroup == Essen) {
            imageResourceId = R.drawable.ic_list_meal;
        } else if (mealGroup == Salate) {
            imageResourceId = R.drawable.ic_list_salad;
        } else if (mealGroup == Suppen) {
            imageResourceId = R.drawable.ic_list_soup;
        } else if (mealGroup == Vorspeisen) {
            imageResourceId = R.drawable.ic_list_appetizer;
        } else {
            imageResourceId = R.drawable.ic_list_meal;
        }
        return imageResourceId;
    }

    @StringRes
    public static int getTextResourceFor(@NonNull MealGroup mealGroup) {
        final int textResourceId;
        if (mealGroup == Aktionsstand) {
            textResourceId = R.string.meal_group_special;
        } else if (mealGroup == Beilagen) {
            textResourceId = R.string.meal_group_supplement;
        } else if (mealGroup == Desserts) {
            textResourceId = R.string.meal_group_dessert;
        } else if (mealGroup == Essen) {
            textResourceId = R.string.meal_group_meal;
        } else if (mealGroup == Salate) {
            textResourceId = R.string.meal_group_salad;
        } else if (mealGroup == Suppen) {
            textResourceId = R.string.meal_group_soup;
        } else if (mealGroup == Vorspeisen) {
            textResourceId = R.string.meal_group_appetizer;
        } else {
            textResourceId = R.string.meal_group_meal;
        }
        return textResourceId;
    }
}
