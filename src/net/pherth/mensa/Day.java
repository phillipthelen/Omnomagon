package net.pherth.mensa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.util.Pair;

public class Day {
	List<Pair<String, List<Meal>>> allMeals = new ArrayList<Pair<String, List<Meal>>>();
	Date date;
	
	public Day(Date currentDay) {
		date = currentDay;
	}
	
	public List<Pair<String, List<Meal>>> getMeals() {
		return allMeals;
	}
	
	public void addMealGroup(String groupname) {
		allMeals.add(new Pair<String, List<Meal>>(groupname, new ArrayList<Meal>()));
	}
	
	public void addMealGroup(String groupname, List<Meal> meals) {
		allMeals.add(new Pair<String, List<Meal>>(groupname, meals));
	}
	
	public void addMealGroup(Pair<String, List<Meal>> meals) {
		allMeals.add(meals);
	}
	
	public void addMeal(String groupname, Meal meal) {
		
	}
}
