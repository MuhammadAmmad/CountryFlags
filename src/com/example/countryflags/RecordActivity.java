package com.example.countryflags;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

public class RecordActivity extends ListActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {
	private static final String TAG = "RecordActivity";
	private SimpleCursorAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_records);
		fillData();

	}

	private void fillData() {
		Log.d(TAG, "fillDate");
		String[] from = new String[] { DBHelper.KEY_NAME, DBHelper.KEY_POINTS,
				DBHelper.KEY_DATE };
		int[] to = new int[] { R.id.game_record_name, R.id.game_record_points,
				R.id.game_record_date };

		adapter = new SimpleCursorAdapter(this, R.layout.i_record, null, from,
				to, 0);
		setListAdapter(adapter);
		getLoaderManager().initLoader(0, null, this);

	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		Log.d(TAG, "createLoader");
		String[] projection = { DBHelper.KEY_NAME, DBHelper.KEY_DATE,
				DBHelper.KEY_POINTS, DBHelper.KEY_ID };

		CursorLoader cursorLoader = new CursorLoader(this,
				GameContentProvider.RECORD_CONTENT_URI, projection, null, null,
				null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.d(TAG, "loadFinish");
		adapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		Log.d(TAG, "loaderReset");
		adapter.swapCursor(null);
	}

}
