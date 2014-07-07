package com.example.countryflags;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "questoins11";
	public static final String TABLE_COUNTRIES = "questions";
	public static final String TABLE_RECORDS = "records";

	public static final int DB_VERSION = 1;
	
	public static final String KEY_ID = "_id";
	//Countries
	public static final String KEY_COUNTRY = "country";
	public static final String KEY_FLAG_PATH = "path";

	//Records
	public static final String KEY_NAME = "name";
	public static final String KEY_POINTS = "points";
	public static final String KEY_DATE = "date";

	
	private static final String CREATE_TABLE_COUNTRYES = "CREATE TABLE " + TABLE_COUNTRIES
			+ "(" + KEY_ID + " INTEGER primary key autoincrement, "
			+ KEY_COUNTRY + " TEXT , " + KEY_FLAG_PATH + " INTEGER ) ";
	
	private static final String CREATE_TABLE_RECORDS = "CREATE TABLE " + TABLE_RECORDS
			+ "(" + KEY_ID + " INTEGER primary key autoincrement, "
			+ KEY_NAME+ " TEXT , " + KEY_POINTS + " INTEGER ,"+KEY_DATE+ " TEXT" + ")";

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_COUNTRYES);
		db.execSQL(CREATE_TABLE_RECORDS);
		
		for (int i=0;i<10;i++){
			addRecord(db, "Player " + i,10+i,"3 Jule 2014");
		}
		
		addCountry(db, R.drawable.at, "Austria");
		addCountry(db, R.drawable.au, "Australia");
		addCountry(db, R.drawable.az, "Azerbaijan");
		addCountry(db, R.drawable.be, "Belgium");
		addCountry(db, R.drawable.bg, "Bulgaria");
		addCountry(db, R.drawable.br, "Brazil");
		addCountry(db, R.drawable.by, "Byelorussia");
		addCountry(db, R.drawable.de, "Germany");
		addCountry(db, R.drawable.gb, "United Kingdom");
		addCountry(db, R.drawable.ge, "Georgia");
		addCountry(db, R.drawable.ht, "Gaity");
		addCountry(db, R.drawable.id, "Indonesia");
		addCountry(db, R.drawable.um, "USA");
		addCountry(db, R.drawable.ar, "Argentina");
		addCountry(db, R.drawable.bh, "Bahrain");
		addCountry(db, R.drawable.eg, "Egypt");
		
	}

	private void addCountry(SQLiteDatabase db, int id_flag, String country) {

		ContentValues values = new ContentValues();
		values.put(KEY_FLAG_PATH, id_flag);
		values.put(KEY_COUNTRY, country);
		db.insert(TABLE_COUNTRIES, null, values);

	}
	
	private void addRecord(SQLiteDatabase db, String name, int points,String date){
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name);
		values.put(KEY_POINTS, points);
		values.put(KEY_DATE, date);
		db.insert(TABLE_RECORDS, null, values);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public static String SelectId(String selection, String id) {
		if (TextUtils.isEmpty(selection)) {
			return DBHelper.KEY_ID + " = " + id;
		} else {
			return selection + " AND " + DBHelper.KEY_ID + " = " + id;
		}
	}

}
