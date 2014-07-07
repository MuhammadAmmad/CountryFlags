package com.example.countryflags;

import java.util.ArrayList;
import java.util.Random;



import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

public class ItemQuestionLoader extends AsyncTaskLoader<ItemQuestion> {
	public static final String EXTRA_ID = "id";
	int id;
	public ItemQuestionLoader(Context context,Bundle data) {
		super(context);
		id = data.getInt(EXTRA_ID);
		
	}

	@Override
	public ItemQuestion loadInBackground() {
		Question question = getQuesion(id);
		ArrayList<String> countries = getCountries(question);
		Bitmap flagImage = BitmapFactory.decodeResource(getContext()
					.getResources(), question.getFlagResId());
		return new ItemQuestion(question, countries,flagImage);
		
	}
	
	private ArrayList<String> getCountries(Question question) {
		ArrayList<String> countries = new ArrayList<String>();
		Uri uri = GameContentProvider.QUESTION_CONTENT_URI;
		uri = uri.buildUpon().appendQueryParameter("limit", "3").build();
		Cursor cursor = getContext().getContentResolver().query(uri,
				new String[] { DBHelper.KEY_COUNTRY, DBHelper.KEY_FLAG_PATH },
				DBHelper.KEY_ID + " != ?",
				new String[] { String.valueOf(question.getId()) }, null);
		if (!cursor.moveToFirst())
			return countries;

		countries.add(question.getCountry());
		String country;
		Random rand = new Random();
		int index;
		for (int i = 0; i < 3; i++) {
			do {
				index = rand.nextInt(cursor.getCount());
				cursor.moveToPosition(index);
				country = cursor.getString(cursor
						.getColumnIndex(DBHelper.KEY_COUNTRY));
			} while (countries.contains(country));
			countries.add(country);
		}
		return countries;
	}

	private Question getQuesion(int id) {
		Uri uri = Uri.parse(GameContentProvider.QUESTION_CONTENT_URI + "/"
				+ id);
		Cursor cursor = getContext().getContentResolver().query(uri,
				new String[] { DBHelper.KEY_COUNTRY, DBHelper.KEY_FLAG_PATH },
				null, null, null);
		if (!cursor.moveToFirst())
			return null;
		String country = cursor.getString(cursor
				.getColumnIndex(DBHelper.KEY_COUNTRY));
		int resId = cursor
				.getInt(cursor.getColumnIndex(DBHelper.KEY_FLAG_PATH));

		cursor.close();
		return new Question(country, resId, id);
	}
	
	
}
