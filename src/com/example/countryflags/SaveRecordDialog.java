package com.example.countryflags;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class SaveRecordDialog extends DialogFragment {

	public static final String EXTRA_POINTS = "points";
	private EditText nameET;
	private int points;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		points = getArguments().getInt(EXTRA_POINTS);
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.d_save_record, null);
		nameET = (EditText) view.findViewById(R.id.game_diglag_nameEditText);
		return new AlertDialog.Builder(getActivity()).setView(view)
				.setTitle(R.string.records)
				.setPositiveButton(android.R.string.ok, new buttonClick())
				.create();
	}

	private class buttonClick implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialogInterface, int i) {
			String name = nameET.getText().toString();
			if (!TextUtils.isEmpty(name)) {
				addRecord(name, points);
			} else {
				addRecord("Player", points);
			}
			Intent intent = new Intent(getActivity(), RecordActivity.class);
			startActivity(intent);
			getActivity().finish();
		}

	}

	private void addRecord(String name, int points) {
		Uri uri = GameContentProvider.RECORD_CONTENT_URI;

		SimpleDateFormat dataFormat = new SimpleDateFormat("d MMMM y",
				java.util.Locale.getDefault());
		String data = dataFormat.format(Calendar.getInstance().getTime());

		ContentValues values = new ContentValues();
		values.put(DBHelper.KEY_NAME, name);
		values.put(DBHelper.KEY_POINTS, points);
		values.put(DBHelper.KEY_DATE, data);

		getActivity().getContentResolver().insert(uri, values);

	}
}