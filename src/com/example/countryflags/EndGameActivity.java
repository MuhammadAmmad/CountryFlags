package com.example.countryflags;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndGameActivity extends Activity implements LoaderCallbacks<Cursor> {
	protected static final String DIALOG_SAVE = "save dialog";
	public static final String EXTRA_POINS = null;
	private int points;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_end_game);
		points = getIntent().getIntExtra(EXTRA_POINS, 0);
		getLoaderManager().initLoader(0,null, this).forceLoad();
		TextView scoreTextView = (TextView) findViewById(R.id.game_scoreTextView);
		scoreTextView.setText(String.valueOf(points));

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(this,GameContentProvider.RECORD_CONTENT_URI,null,null,null,null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor mngCursor) {
		int minPoints = 0;
		if (mngCursor.moveToPosition(9)) {
			minPoints = mngCursor.getInt(mngCursor
					.getColumnIndex(DBHelper.KEY_POINTS));
		}
		if (points > minPoints) {
			Button saveRecordButton = (Button) findViewById(R.id.game_saveRecordButton);
			TextView labelRecord = (TextView) findViewById(R.id.data_labelRecordTextView);
			
			saveRecordButton.setVisibility(View.VISIBLE);
			labelRecord.setVisibility(View.VISIBLE);
			
	
			saveRecordButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					DialogFragment saveDialog = new SaveRecordDialog();
					Bundle args = new Bundle();
					args.putInt(SaveRecordDialog.EXTRA_POINTS, points);
					saveDialog.setArguments(args);
					saveDialog.setRetainInstance(true);
					saveDialog.show(getFragmentManager(), DIALOG_SAVE);
				}
			});
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {}

}
