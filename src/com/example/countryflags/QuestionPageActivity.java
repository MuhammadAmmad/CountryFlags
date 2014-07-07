package com.example.countryflags;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class QuestionPageActivity extends FragmentActivity implements LoaderCallbacks<ItemQuestion>{

	public static final String EXTRA_QUESTIONS_ID = "array";
	public static final String EXTRA_CURRENT_ITEM = "c_item";

	private static final int DEFAULT_ADD__POINT = 10;

	private static final String KEY_CURRENT_ITEM = "current_item";
	private static final String KEY_FACTOR = "factor";
	private static final String KEY_POINS = "points";
	private static final String KEY_ITEM = "item";
	
	protected static final int NEXT = 0;
	private static final String TAG = "PagesQuest";
	

	private TextView pointsTextView;
	private TextView rightTextView;
	Button[] buttons = new Button[4];
	private ImageView flagImageView;
	
	private int points = 0;
	private int factor = 0;
	private int countQuesions = 1;
	private boolean enableTimer = false;
	private ArrayList<Integer> questions_id;
	private int current_item;
	private boolean sendAnswer = false;
	ItemQuestion itemQuestion;
	
	
	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_pager_question);
		findViews();
		loadInstance(savedInstanceState);
	}
	
	

	@Override
	protected void onResume() {
		super.onResume();
		if (current_item >= countQuesions) {
			endGame();
		}
	}



	private void loadInstance(Bundle savedInstanceState) {
		questions_id = getIntent().getIntegerArrayListExtra(EXTRA_QUESTIONS_ID);
		countQuesions = questions_id.size();
		if (savedInstanceState != null)
			loadSaveInstance(savedInstanceState);
		else
			loadPreferenceInstance();

	}

	private void loadPreferenceInstance() {
		Log.d(TAG,"Load Pref");
		SharedPreferences sharedPref = getSharedPreferences("local", Context.MODE_PRIVATE);
		factor = sharedPref.getInt(KEY_FACTOR, 0);
		points = sharedPref.getInt(KEY_POINS, 0);
		current_item = sharedPref.getInt(KEY_CURRENT_ITEM, 0);	
		resetFields();
		loadQuestion();
	}

	private void loadSaveInstance(Bundle savedInstanceState) {
		Log.d(TAG,"Load Saved Instance");
		factor = savedInstanceState.getInt(KEY_FACTOR);
		points = savedInstanceState.getInt(KEY_POINS);
		current_item = savedInstanceState.getInt(KEY_CURRENT_ITEM);
		itemQuestion = (ItemQuestion) savedInstanceState.getSerializable(KEY_ITEM);
		resetFields();
		setFields(itemQuestion);
	}
	
	//Run async load Quesiotn from Base
	private void loadQuestion() {
		if (current_item >= countQuesions) return;
		Log.d(TAG,"Load Question id:" + questions_id.get(current_item));
		Bundle data = new Bundle();
		data.putInt(ItemQuestionLoader.EXTRA_ID, questions_id.get(current_item));
		if (getSupportLoaderManager().getLoader(questions_id.get(current_item))==null)
			getSupportLoaderManager().initLoader(questions_id.get(current_item), data, this).forceLoad();
		else {
			getSupportLoaderManager().restartLoader(questions_id.get(current_item), data, this).forceLoad();
		}
		
	}

	//Call before finish load question 
	public void setFields(ItemQuestion qItem) {
		for (Button button:buttons){
			button.setText("");
			button.setBackgroundColor(getResources().getColor(R.color.dark_slate_gray));
		}
		Random rand = new Random();
		for (int i = 0; i < 4; i++) {
			int index = 0;
			do {
				index = rand.nextInt(4);
			} while (!TextUtils.isEmpty(buttons[index].getText()));

			buttons[index].setText(qItem.getCountries().get(i));

		}

		flagImageView.setImageBitmap(qItem.getFLagBitmap());

	}

	//Call before send answer
	private void nextQuestion() {
		Log.d(TAG,"next Question");
		current_item++;
		resetFields();
		loadQuestion();
		enableTimer = true;
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (current_item >= countQuesions) {
					endGame();
				} else {
					setFields(itemQuestion);
				}
				enableTimer = false;
				sendAnswer = false;
			}
		}, 1000);
		
	}

	public void findViews() {
		pointsTextView = (TextView) findViewById(R.id.game_pointsTextView);
		rightTextView = (TextView) findViewById(R.id.game_rightTextView);
		
		buttons[0] = (Button) findViewById(R.id.game_var1Button);
		buttons[1] = (Button) findViewById(R.id.game_var2Button);
		buttons[2] = (Button) findViewById(R.id.game_var3Button);
		buttons[3] = (Button) findViewById(R.id.game_var4Button);
		for (Button button : buttons) {
			button.setOnClickListener(clickButton);
		}
		flagImageView = (ImageView)findViewById(R.id.game_flagImageView);
	}

	public void addRight() {
		factor++;
		addPoints(factor);
	}

	private void addPoints(int factor) {
		points += DEFAULT_ADD__POINT * factor;

	}

	private void endGame() {
		Log.d(TAG,"End Game");
		SharedPreferences sharedPref = getApplicationContext()
				.getSharedPreferences(HomeActivity.SAVE_GAME_PREF, Context.MODE_PRIVATE);
		sharedPref.edit().putBoolean(HomeActivity.KEY_SAVE_GAME, false)
				.commit();
		Intent intent = new Intent(this, EndGameActivity.class);
		intent.putExtra(EndGameActivity.EXTRA_POINS, points);
		startActivity(intent);
		points = 0;
		factor = 0;
		current_item = 0;
		finish();
	}

	public void resetFields() {
		pointsTextView.setText(String.valueOf(points));
		rightTextView.setText(current_item + " / " + countQuesions);
		
		
	}

	public void addFalse() {
		factor = 0;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler = null;
		saveState();
		
	}

	private void saveState() {
		SharedPreferences sharedPrefr = getSharedPreferences("local", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPrefr.edit();
		editor.putInt(KEY_FACTOR, factor);
		editor.putInt(KEY_POINS, points);
		editor.putInt(KEY_CURRENT_ITEM, current_item);
		editor.commit();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_FACTOR, factor);
		outState.putInt(KEY_POINS, points);
		outState.putInt(KEY_CURRENT_ITEM, current_item);
		outState.putSerializable(KEY_ITEM, itemQuestion);
	}
	
	private View.OnClickListener clickButton = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			if (!sendAnswer) {
				if (isRightButton(v,itemQuestion)) {
					v.setBackgroundColor(getResources().getColor(R.color.green));
					addRight();

				} else {
					v.setBackgroundColor(getResources().getColor(
							R.color.red));
					for (Button btn : buttons) {
						// Ищем правильный ответ
						if (isRightButton(btn,itemQuestion)) {
							btn.setBackgroundColor(getResources().getColor(R.color.green));
							break;
						}

					}
					addFalse();
					
				}
				sendAnswer = true;
				nextQuestion();
			}

		}
	};

	private boolean isRightButton(View button,ItemQuestion iQuestion){
		return itemQuestion.getQuestion().isRight(((Button) button).getText().toString());
	}
	
	@Override
	public android.support.v4.content.Loader<ItemQuestion> onCreateLoader(int id, Bundle args) {
		return new ItemQuestionLoader(this, args);
	}

	


	@Override
	public void onLoadFinished(
			android.support.v4.content.Loader<ItemQuestion> arg0,
			ItemQuestion arg1) {
		itemQuestion = arg1;
		if (!enableTimer)
			setFields(itemQuestion);
		
	}



	@Override
	public void onLoaderReset(
			android.support.v4.content.Loader<ItemQuestion> arg0) {
		
	}


}
