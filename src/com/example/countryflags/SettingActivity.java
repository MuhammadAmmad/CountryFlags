package com.example.countryflags;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;

public class SettingActivity extends Activity {
	public static final String KEY_GAME_MODE = "game_mode";
	public static final String SETTING_APP = "setting";

	public static final int GAME_EASY = 1;
	public static final int GAME_NORMAL = 2;
	public static final int GAME_HARD = 3;

	private RadioGroup radioGroup;
	private int gameMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_setting);
		findView();

	}

	private void findView() {
		radioGroup = (RadioGroup) findViewById(R.id.game_setting_radioGroup);
		radioGroup.setOnCheckedChangeListener(listener);
		Button saveButton = (Button) findViewById(R.id.game_setting_saveButton);
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences preferences = getSharedPreferences(
						SETTING_APP, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putInt(KEY_GAME_MODE, gameMode).commit();
				finish();
			}
		});
		SharedPreferences preferences = getSharedPreferences(SETTING_APP,
				Context.MODE_PRIVATE);
		int id = preferences.getInt(KEY_GAME_MODE, GAME_HARD);
		switch (id) {
		case GAME_EASY:
			radioGroup.check(R.id.game_setting_5_RadioButton);
			break;
		case GAME_HARD:
			radioGroup.check(R.id.game_setting_15_RadioButton);
			break;
		case GAME_NORMAL:
		default:
			radioGroup.check(R.id.game_setting_10_RadioButton);
			break;
		}
	}

	RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.game_setting_5_RadioButton:
				gameMode = GAME_EASY;
				break;
			case R.id.game_setting_10_RadioButton:
				gameMode = GAME_NORMAL;
				break;
			case R.id.game_setting_15_RadioButton:
				gameMode = GAME_HARD;
				break;

			default:
				break;
			}
		}
	};

}
