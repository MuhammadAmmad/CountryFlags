package com.example.countryflags;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeActivity extends Activity implements LoaderCallbacks<Cursor> {
	private static final String TAG = "HomeActivity";
	private static final String KEY_COUNT = "mode";
	public static final String KEY_SAVE_GAME = "save";
	public static final String SAVE_GAME_PREF = "preference";
	private static final int DEFAULT_COUNT = 10;

	private int gameMode = 5;
	private static SparseIntArray mapLevel = new SparseIntArray();
	static {
		mapLevel.put(SettingActivity.GAME_EASY, 5);
		mapLevel.put(SettingActivity.GAME_NORMAL, 10);
		mapLevel.put(SettingActivity.GAME_HARD, 15);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_home);
		Button buttonStartGame = (Button) findViewById(R.id.game_startButton);
		buttonStartGame.setOnClickListener(clickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.m_home, menu);
		return true;
	}

	private void loadAndStartSaveGame() {
		SharedPreferences preferences = getSharedPreferences(SAVE_GAME_PREF,
				Context.MODE_PRIVATE);
		int count = preferences.getInt(KEY_COUNT,DEFAULT_COUNT);
		ArrayList<Integer> flagsId = new ArrayList<Integer>();
		for (int i = 0; i < count; i++) {
			flagsId.add(preferences.getInt("State_" + i, i));
		}
		startGameActivity(flagsId);

	}

	private boolean isSaveGame() {
		SharedPreferences preferences = getApplicationContext()
				.getSharedPreferences(SAVE_GAME_PREF, Context.MODE_PRIVATE);
		return (preferences.getBoolean(KEY_SAVE_GAME, false));

	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(this,SettingActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_records:
			intent = new Intent(this,RecordActivity.class);
			startActivity(intent);
		default:
			return false;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void startNewGame() {
		SharedPreferences preferences = getApplicationContext()
				.getSharedPreferences(SettingActivity.SETTING_APP,
						Context.MODE_PRIVATE);
		gameMode = preferences.getInt(SettingActivity.KEY_GAME_MODE,
				SettingActivity.GAME_NORMAL);
		getLoaderManager().initLoader(0, null, this);

	}

	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (isSaveGame()) {
				loadAndStartSaveGame();
			} else
				startNewGame();
		}
	};

	private void startGameActivity(ArrayList<Integer> idFlags) {
		Intent intent = new Intent(this, QuestionPageActivity.class);
		intent.putExtra(QuestionPageActivity.EXTRA_QUESTIONS_ID, idFlags);
		startActivity(intent);

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(this, GameContentProvider.QUESTION_CONTENT_URI,
				new String[] { DBHelper.KEY_ID }, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		ArrayList<Integer> idFlags = getIdFlags(cursor);
		SaveStartState(idFlags);
		startGameActivity(idFlags);

	}

	private void SaveStartState(ArrayList<Integer> questions_id) {
		Editor editor = getApplicationContext().getSharedPreferences(
				SAVE_GAME_PREF, Context.MODE_PRIVATE).edit();
		editor.putBoolean(KEY_SAVE_GAME, true);
		editor.putInt(KEY_COUNT, questions_id.size());
		for (int i = 0; i < questions_id.size(); i++)
			editor.putInt("State_" + i, questions_id.get(i));
		editor.commit();

	}

	private ArrayList<Integer> getIdFlags(Cursor mCursor) {
		if (mCursor.moveToFirst()) {
			int[] allId = new int[mCursor.getCount()];
			int i = 0;
			do {
				try {
					allId[i] = mCursor.getInt(mCursor
							.getColumnIndex(DBHelper.KEY_ID));
					i++;
				} catch (CursorIndexOutOfBoundsException e) {

				}
			} while (mCursor.moveToNext());

			ArrayList<Integer> idFlags = new ArrayList<Integer>();
			Random rand = new Random();
			int count = mapLevel.get(gameMode);
			for (i = 0; i < count; i++) {
				int id;
				do {
					id = allId[rand.nextInt(allId.length)];
				} while (idFlags.contains(id));
				idFlags.add(id);
			}
			Log.d(TAG, "Flags[] = " + idFlags.toString());
			return idFlags;
		}
		return new ArrayList<Integer>();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
	}
}
