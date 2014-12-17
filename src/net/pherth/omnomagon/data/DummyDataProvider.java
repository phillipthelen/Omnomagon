package net.pherth.omnomagon.data;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

public class DummyDataProvider {

    @NonNull
    public static List<Day> generateDummyData() {
        return Arrays.asList(monday(), tuesday(), wednesday(), thursday(), friday());
    }

    @NonNull
    private static Day monday() {
        final Day day = new Day("15.12.2014");
        day.addMealGroup(MealGroup.Vorspeisen, Arrays.asList(
                Meal.create("Bunter Kartoffelsalat", 2.45f, 2.45f, 2.45f, false, true, false, false, "Vegan", "Sellerie", "Senf")
        ));
        day.addMealGroup(MealGroup.Salate, Arrays.asList(
                Meal.create("Großer Salat", 1.65f, 2.65f, 3.45f, false, true, false, false, "Vegan", "Sellerie"),
                Meal.create("Kleiner Salat", 0.6f, 0.95f, 1.25f, false, true, false, false, "Vegan", "Sellerie")
        ));
        day.addMealGroup(MealGroup.Suppen, Arrays.asList(
                Meal.create("Curryrahmsuppe", 0.55f, 0.9f, 1.15f, true, false, false, false, "Vegetarisch", "Sellerie", "Milch")
        ));
        return day;
    }

    @NonNull
    private static Day tuesday() {
        final Day day = new Day("16.12.2014");
        day.addMealGroup(MealGroup.Vorspeisen, Arrays.asList(
                Meal.create("Anti pasti", 2.45f, 2.45f, 2.45f, false, true, false, false, "geschwärzt", "Vegan", "Sellerie")
        ));
        day.addMealGroup(MealGroup.Salate, Arrays.asList(
                Meal.create("Großer Salat", 1.65f, 2.65f, 3.45f, false, true, false, false, "Vegan", "Sellerie"),
                Meal.create("Kleiner Salat", 0.6f, 0.95f, 1.25f, false, true, false, false, "Vegan", "Sellerie")
        ));
        day.addMealGroup(MealGroup.Suppen, Arrays.asList(
                Meal.create("Staudenselleriesuppe", 0.55f, 0.9f, 1.15f, true, false, false, false, "Vegetarisch", "geschwärzt", "Sellerie", "Milch")
        ));
        day.addMealGroup(MealGroup.Aktionsstand, Arrays.asList(
                Meal.create("Gemüse-Tofuragout mit Vollkornpasta", 3.95f, 3.95f, 3.95f, false, true, false, false, "konserviert", "Vegan", "Glutenhlt. Getreide", "Sellerie", "Soja")
        ));
        return day;
    }

    @NonNull
    private static Day wednesday() {
        final Day day = new Day("17.12.2014");
        day.addMealGroup(MealGroup.Salate, Arrays.asList(
                Meal.create("Großer Salat", 1.65f, 2.65f, 3.45f, false, true, false, false, "Vegan", "Sellerie"),
                Meal.create("Kleiner Salat", 0.6f, 0.95f, 1.25f, false, true, false, false, "Vegan", "Sellerie")
        ));
        day.addMealGroup(MealGroup.Suppen, Arrays.asList(
                Meal.create("Kürbissuppe mit Zimt", 0.55f, 0.9f, 1.15f, true, false, false, false, "Vegetarisch", "Sellerie", "Milch")
        ));
        return day;
    }

    @NonNull
    private static Day thursday() {
        final Day day = new Day("18.12.2014");
        day.addMealGroup(MealGroup.Vorspeisen, Arrays.asList(
                Meal.create("Mit Reis gefüllte Weinblätter und Knoblauchdip", 1.75f, 1.75f, 1.75f, true, false, false, false, "Vegetarisch", "konserviert", "Antioxidationsmittel", "Glutenhlt. Getreide", "Eier", "Sellerie", "Milch", "Hefe")
        ));
        day.addMealGroup(MealGroup.Salate, Arrays.asList(
                Meal.create("Kleiner Salat", 0.6f, 0.95f, 1.25f, false, true, false, false, "Vegan", "Sellerie")
        ));
        day.addMealGroup(MealGroup.Suppen, Arrays.asList(
                Meal.create("weisse Bohnensuppe", 0.55f, 0.9f, 1.15f, true, false, false, false, "Vegetarisch", "Sellerie", "Milch")
        ));
        day.addMealGroup(MealGroup.Aktionsstand, Arrays.asList(
                Meal.create("Fünf Nudelkissen mit Spinat- Käsefüllung an Gorgonzolasauce", 3.95f, 3.95f, 3.95f, true, false, false, false, "Vegetarisch", "Farbstoff", "Glutenhlt. Getreide", "Eier", "Sellerie", "Milch")
        ));
        return day;
    }

    @NonNull
    private static Day friday() {
        final Day day = new Day("19.12.2014");
        day.addMealGroup(MealGroup.Vorspeisen, Arrays.asList(
                Meal.create("Peruanischer Linsensalat", 1.45f, 1.45f, 1.45f, false, true, false, false, "Vegan", "Sellerie")
        ));
        day.addMealGroup(MealGroup.Salate, Arrays.asList(
                Meal.create("Großer Salat", 1.65f, 2.65f, 3.45f, false, true, false, false, "Vegan", "Sellerie"),
                Meal.create("Kleiner Salat", 0.6f, 0.95f, 1.25f, false, true, false, false, "Vegan", "Sellerie")
        ));
        day.addMealGroup(MealGroup.Suppen, Arrays.asList(
                Meal.create("Schwarzwurzelcremesuppe", 0.55f, 0.9f, 1.15f, true, false, false, false, "Vegetarisch", "Sellerie", "Milch")
        ));
        return day;
    }
}
