package net.pherth.omnomagon;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class Database extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 3;
	private static final String DAY_TABLE_NAME = "day";
	private static final String DAY_TABLE_CREATE =
	            "CREATE TABLE " + DAY_TABLE_NAME + " ( _id integer primary key autoincrement, date TEXT);";
	private static final String meal_TABLE_NAME = "meal";
	private static final String meal_TABLE_CREATE =
	            "CREATE TABLE " + meal_TABLE_NAME + " ( _id integer primary key autoincrement, dayID INTEGER, groupID INTEGER, name TEXT, price1 FLOAT, price2 FLOAT, price3 FLOAT, vegan BOOLEAN, " +
	            		"vegetarian BOOLEAN, msc BOOLEAN, bio BOOLEAN);";
	private static final String addition_TABLE_NAME = "additions";
	private static final String addition_TABLE_CREATE =
	            "CREATE TABLE " + addition_TABLE_NAME + " ( mealID INTEGER, name TEXT);";
	
	private static String DB_PATH = "/data/data/net.pherth.omnomagon/databases/";
	private static String DB_NAME = "omnomagon.db";
	
	Database(Context context) {
		super(context,DB_NAME, null, DATABASE_VERSION);
		Log.i("Database", "Database initialized");
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("Database", "create database");
	    db.execSQL(DAY_TABLE_CREATE);
	    db.execSQL(meal_TABLE_CREATE);
	    db.execSQL(addition_TABLE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TRIGGER IF EXISTS fk_empdept_deptid");
		db.execSQL("DROP TABLE IF EXISTS "+ DAY_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS "+ meal_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS "+ addition_TABLE_NAME);
		onCreate(db);
	}
	
	public void clearDatabase(SQLiteDatabase db) {
		db.execSQL("DELETE FROM '" + DAY_TABLE_NAME + "';");
		db.execSQL("DELETE FROM '" + meal_TABLE_NAME + "';");
		db.execSQL("DELETE FROM '" + addition_TABLE_NAME + "';");
	}
}