package com.maol.setpuzzle.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.maol.setpuzzle.R;

public class GameOverActivity extends Activity{
	
	TextView txtActualScore;
	TextView txtBestScore;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_gameover);
		
		txtActualScore = (TextView) findViewById(R.id.txtActualScore);
		txtBestScore = (TextView) findViewById(R.id.txtBest);
		
		int playerScore = getIntent().getIntExtra("playerscore", 0);
		
		txtActualScore.setText(playerScore + "");
		
		SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
		int bestScore = sp.getInt("best_score", 0);
		
		txtBestScore.setText(bestScore + "");
		
		if(playerScore > bestScore) {
			SharedPreferences sp2 = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp2.edit();
			editor.putInt("best_score", playerScore);
			editor.commit();
			
			txtBestScore.setText(playerScore + "");
		}
		
	}
	
	@Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);   
    }
	
	public void clickHome(View v) {
		onBackPressed();
	}
	
	public void clickPlayAgain(View v) {
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);		
		finish();
	}
	
	
}
