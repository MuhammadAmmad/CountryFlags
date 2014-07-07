package com.example.countryflags;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class SplashActivity extends Activity{
	
	private static final String ENABLE_TIMER_KEY = "timer";
	protected static final int ACTIVITY_FINISH = 0;
	private static final long SLEEP_MILLISECONDS = 1000;
	private boolean enableTimer = true;
	private Timer timer;
	private Handler handler =
			new Handler() {
		        public void handleMessage(Message msg) {
		            if (msg.what == ACTIVITY_FINISH) {
		                startHomeScreen();
		            }
		        }
			};
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_splash);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.game_container);
        layout.setOnClickListener(clickListener);
        
        if (savedInstanceState != null) {
            enableTimer = savedInstanceState.getBoolean(ENABLE_TIMER_KEY);
        }

        if (enableTimer) {
            timer = new Timer();
            timer.schedule(
            		new TimerTask() {
		                @Override
		                public void run() {
		                    handler.sendEmptyMessage(ACTIVITY_FINISH);
		                }
            		}
            		, SLEEP_MILLISECONDS);
            enableTimer = false;
            }
            
	}

    private void startHomeScreen() {
        Intent homeScreen = new Intent(this, HomeActivity.class);
        startActivity(homeScreen);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(ENABLE_TIMER_KEY, enableTimer);
    }
    
    private OnClickListener clickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startHomeScreen();
			
		}
	};

}
