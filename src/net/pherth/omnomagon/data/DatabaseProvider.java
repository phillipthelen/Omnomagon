package net.pherth.omnomagon.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;
import net.pherth.omnomagon.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DatabaseProvider {

	// Database fields
	private SQLiteDatabase database;
	private Database dbHelper;

	public DatabaseProvider(Context context) {
		if(Util.getDebuggable(context)) Log.i("Dataprovider", "initializing Dataprovider");
		dbHelper = new Database(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Boolean newData(List<Day> data) {
		dbHelper.clearDatabase(database);
		for (final Day day : data) {
			List<Pair<String, String>> daydata = new ArrayList<Pair<String, String>>();
			daydata.add(new Pair<String, String>("date", day.date.toString()));
			long dayId = insert("day", daydata);
			final Map<MealGroup, List<Meal>> meals = day.getMeals();
			for (final Entry<MealGroup, List<Meal>> entry: meals.entrySet()) {
				for (final Meal meal : entry.getValue()) {
					ContentValues mealValues = new ContentValues();
					mealValues.put("dayID", dayId);
					mealValues.put("groupString", entry.getKey().name());
					mealValues.put("name", meal.getName());
					mealValues.put("vegan", meal.getVegan());
					mealValues.put("vegetarian", meal.getVegetarian());
					mealValues.put("msc", meal.getMsc());
					mealValues.put("bio", meal.getBio());
					Float[] prices = meal.getPrices();
					mealValues.put("price1", prices[0]);
					mealValues.put("price2", prices[1]);
					mealValues.put("price3", prices[2]);
					long mealID = database.insert("meal", null, mealValues);

					List<String> additions = meal.getAdditions();
					for (final String addition1 : additions) {
						ContentValues additiondata = new ContentValues();
						additiondata.put("mealid", mealID);
						additiondata.put("name", addition1);
						database.insert("additions", null, additiondata);
					}
				}
			}
		}
		return true;
	}

	public long insert(String table, List<Pair<String, String>> data) {
		ContentValues values = new ContentValues();
		for (final Pair<String, String> line : data) {
			values.put(line.first, line.second);
		}
		return database.insert(table, null, values);
	}

	public List<Day> getData() {
		List<Day> data = new ArrayList<Day>();
		String[] daycolumns = {"date", "_id"};
		String[] mealcolumns = {"_id", "name", "dayID", "groupID", "groupString", "price1", "price2", "price3", "vegan", "vegetarian", "msc", "bio"};
		String[] additioncolumns = {"name"};
		Cursor cursor = database.query("day", daycolumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			data.add(new Day(cursor.getString(0), cursor.getString(1)));
			cursor.moveToNext();
		}
		cursor.close();

		for (final Day aData : data) {
			String[] dayarg = {aData.id};
			cursor = database.query("meal", mealcolumns, "dayID = ?", dayarg, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Meal meal = new Meal(cursor.getString(1));
				Float[] prices = {cursor.getFloat(5), cursor.getFloat(6), cursor.getFloat(7)};
				meal.setPrices(prices);
				meal.setBio((cursor.getInt(11) == 1));
				meal.setMsc((cursor.getInt(10) == 1));
				meal.setVegetarian((cursor.getInt(9) == 1));
				meal.setVegan((cursor.getInt(8) == 1));
				String[] mealarg = {cursor.getString(0)};
				Cursor acursor = database.query("additions", additioncolumns, "mealID = ?", mealarg, null, null, null);
				acursor.moveToFirst();
				while (!acursor.isAfterLast()) {
					meal.addAddition(acursor.getString(0));
					acursor.moveToNext();
				}
				acursor.close();
				aData.addMeal(MealGroup.valueOf(cursor.getString(4)), meal);
				cursor.moveToNext();
			}
			cursor.close();

		}
		
		return data;
	}
}