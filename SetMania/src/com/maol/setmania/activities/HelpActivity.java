package com.maol.setmania.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.maol.setmania.R;

public class HelpActivity extends Activity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_help);
		
	}
	
	@Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);   
    }
	
	
	public void clickBack(View v) {
		onBackPressed();
	}
	
}
