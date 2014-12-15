package net.pherth.omnomagon;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;

public class Dataprovider {

	// Database fields
	private SQLiteDatabase database;
	private Database dbHelper;

	public Dataprovider(Context context) {
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
			List<Pair<Pair<Integer, String>, List<Meal>>> meals = day.getMeals();
			for (final Pair<Pair<Integer, String>, List<Meal>> meal1 : meals) {
				List<Meal> meallist = meal1.second;
				Integer groupID = meal1.first.first;
				String groupStr = meal1.first.second;
				for (final Meal meal : meallist) {
					ContentValues mealValues = new ContentValues();
					mealValues.put("dayID", dayId);
					mealValues.put("groupID", groupID);
					mealValues.put("groupString", groupStr);
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
				aData.addMeal(cursor.getInt(3), cursor.getString(4), meal);
				cursor.moveToNext();
			}
			cursor.close();

		}
		
		return data;
	}
}