package net.pherth.omnomagon.data;

import android.support.annotation.NonNull;

import java.util.*;

public class MealGroupBasedSorter {

    private MealGroupBasedSorter() { }

    @NonNull
    public static List<Day> sort(@NonNull List<Day> data) {
        for (final Day day : data) {
            sortDataForDay(day);
        }
        return data;
    }

    private static void sortDataForDay(@NonNull Day day) {
        final Map<MealGroup, List<Meal>> sortedMeals = new LinkedHashMap<MealGroup, List<Meal>>();
        final Map<MealGroup, List<Meal>> originalMeals = day.getMeals();
        insertSortedGroup(originalMeals, sortedMeals, MealGroup.Aktionsstand);
        insertSortedGroup(originalMeals, sortedMeals, MealGroup.Vorspeisen);
        insertSortedGroup(originalMeals, sortedMeals, MealGroup.Suppen);
        insertSortedGroup(originalMeals, sortedMeals, MealGroup.Salate);
        insertSortedGroup(originalMeals, sortedMeals, MealGroup.Essen);
        insertSortedGroup(originalMeals, sortedMeals, MealGroup.Desserts);
        insertSortedGroup(originalMeals, sortedMeals, MealGroup.Beilagen);
        day.setAllMeals(sortedMeals);
    }

    private static void insertSortedGroup(@NonNull Map<MealGroup, List<Meal>> source, @NonNull Map<MealGroup, List<Meal>> target, @NonNull MealGroup mealGroup) {
        final List<Meal> meals = source.get(mealGroup);
        if (meals != null && !meals.isEmpty()) {
            Collections.sort(meals, MealComparator.getInstance());
            target.put(mealGroup, meals);
        }
    }

    private static class MealComparator implements Comparator<Meal> {

        private static MealComparator COMPARATOR = new MealComparator();

        private MealComparator() { }

        @NonNull
        public static MealComparator getInstance() {
            return COMPARATOR;
        }

        @Override
        public int compare(Meal lhs, Meal rhs) {
            return lhs.getName().compareToIgnoreCase(rhs.getName());
        }
    }
}
