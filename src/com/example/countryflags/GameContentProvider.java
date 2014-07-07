package com.example.countryflags;



import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class GameContentProvider extends ContentProvider {
	private static final String TAG = "ProvierContent";
	
	SQLiteDatabase database;
	DBHelper dbHelper;
	
	static final String AUTHORITY = "com.example.countryandflags.providers.Questions";
	
	static final String QUESTION_PATH = "questions";
	static final String RECORD_PATH = "records";
	
	public static final Uri QUESTION_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + QUESTION_PATH);
	public static final Uri RECORD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + RECORD_PATH);
	
	
	public static final String QUESTION_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + QUESTION_PATH;
	public static final String RECORD_CONTENT_TYPE ="vnd.android.cursor.dir/vnd." + AUTHORITY + "." + RECORD_PATH;
	
	
	public static final String QUESTION_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."	+ AUTHORITY + "." + QUESTION_PATH;
	public static final String RECORD_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."	+ AUTHORITY + "." + RECORD_PATH;
	
	private static final UriMatcher uriMatcher;

	static final int URI_QUESTIONS = 1;
	static final int URI_QUESTIONS_ID = 2
			;
	static final int URI_RECORDS = 3;
	static final int URI_RECORDS_ID = 4;

	
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, QUESTION_PATH, URI_QUESTIONS);
		uriMatcher.addURI(AUTHORITY, QUESTION_PATH + "/#", URI_QUESTIONS_ID);
		uriMatcher.addURI(AUTHORITY, RECORD_PATH, URI_RECORDS);
		uriMatcher.addURI(AUTHORITY, RECORD_PATH + "/#", URI_RECORDS_ID);
	}
	
	
	@Override
	public boolean onCreate() {
		dbHelper = new DBHelper(getContext());
		return true;
	}
	
	

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case URI_QUESTIONS:
			return QUESTION_CONTENT_TYPE;
		case URI_QUESTIONS_ID:
			return QUESTION_CONTENT_ITEM_TYPE;
		case URI_RECORDS:
			return RECORD_CONTENT_TYPE;
		case URI_RECORDS_ID:
			return RECORD_CONTENT_ITEM_TYPE;
		}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch (uriMatcher.match(uri)) {
		case URI_QUESTIONS:
			database = dbHelper.getWritableDatabase();
			long rowID = database.insertWithOnConflict(DBHelper.TABLE_COUNTRIES,
					null, values, SQLiteDatabase.CONFLICT_IGNORE);
			Uri resultUri = ContentUris.withAppendedId(QUESTION_CONTENT_URI, rowID);
			
			getContext().getContentResolver().notifyChange(resultUri, null);
			return resultUri;
		case URI_RECORDS:
			database = dbHelper.getWritableDatabase();
			rowID = database.insertWithOnConflict(DBHelper.TABLE_RECORDS,
					null, values, SQLiteDatabase.CONFLICT_REPLACE);
			resultUri = ContentUris.withAppendedId(RECORD_CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(resultUri, null);
			return resultUri;
		default:
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}
		
		
	}


	@Override
	public Cursor query(Uri uri, String[] projec, String selection, String[] selectionArgs,
			String sortOrder) {
		Log.d(TAG,"Uri: " + uri.toString());
		switch (uriMatcher.match(uri)) {
		case URI_QUESTIONS:
			return queryTable(DBHelper.TABLE_COUNTRIES,projec,selection,selectionArgs,DBHelper.KEY_ID,null);
		case URI_QUESTIONS_ID:
			String id = uri.getLastPathSegment();
			return queryTable(DBHelper.TABLE_COUNTRIES,projec,selection,selectionArgs,DBHelper.KEY_ID,id);
		case URI_RECORDS:
			return queryTable(DBHelper.TABLE_RECORDS,projec,selection,selectionArgs,DBHelper.KEY_POINTS + " DESC LIMIT 10",null);
		case URI_RECORDS_ID:
			id = uri.getLastPathSegment();
			return queryTable(DBHelper.TABLE_RECORDS,projec,selection,selectionArgs,sortOrder,id);
		default:
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}
		
	}
	
	private Cursor queryTable(String tableName,String[] projec, String selection, String[] selectionArgs,
			String sortOrder,String id){
		if (id!=null){
			selection = DBHelper.SelectId(selection,id);
		}
		database = dbHelper.getWritableDatabase();
		return database.query(tableName,projec,selection,selectionArgs,null, null, sortOrder);
	}
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}

}
