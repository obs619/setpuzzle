package com.maol.setmania.activities;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.maol.setmania.R;

public class MenuActivity extends Activity{

	LinearLayout layoutButtonPlay;
	Button btnPlay;
	Button btnHighScore;
	Button btnHelp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.activity_menu);
		
		layoutButtonPlay = (LinearLayout) findViewById(R.id.layoutButtonPlay);
		btnPlay = (Button) findViewById(R.id.btn_play);
		btnHighScore = (Button) findViewById(R.id.btn_highscore);
		btnHelp = (Button) findViewById(R.id.btn_help);
		
		AnimatorSet anim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.drawable.rotate_button);
		anim.setTarget(btnHelp);
		anim.start();
		
		anim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.drawable.rotate_button);
		anim.setTarget(btnHighScore);
		anim.start();
		
		anim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.drawable.rotate_button);
		anim.setTarget(layoutButtonPlay);
		anim.start();

	}
	
	public void clickPlay(View v) {
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	
	
}
