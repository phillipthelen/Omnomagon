package net.pherth.mensa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 2;
	private static final String DAY_TABLE_NAME = "day";
	private static final String DICTIONARY_TABLE_CREATE =
	            "CREATE TABLE " + DAY_TABLE_NAME + " ( id INTEGER, date TEXT);";
	private static final String meal_TABLE_NAME = "meal";
	private static final String meal_TABLE_CREATE =
	            "CREATE TABLE " + meal_TABLE_NAME + " ( dayID INTEGER, id INTEGER, name TEXT, vegan BOOLEAN, " +
	            		"vegetarian BOOLEAN, msc BOOLEAN, bio BOOLEAN);";
	private static final String addition_TABLE_NAME = "additions";
	private static final String addition_TABLE_CREATE =
	            "CREATE TABLE " + addition_TABLE_NAME + " ( mealID INTEGER, name TEXT);";
	
	Database(Context context) {
	    super(context, "omnomagon", null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
	    db.execSQL(DICTIONARY_TABLE_CREATE);
	    db.execSQL(meal_TABLE_CREATE);
	    db.execSQL(addition_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		//super.onUpgrade(arg0, arg1, arg2);
		
	}
}