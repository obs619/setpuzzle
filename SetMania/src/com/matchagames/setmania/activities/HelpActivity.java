package com.matchagames.setmania.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.matchagames.setmania.R;
import com.viewpagerindicator.CirclePageIndicator;

public class HelpActivity extends FragmentActivity{

	
	HelpPagerAdapter helpPagerAdapter;
	ViewPager helpPager;
	CirclePageIndicator circleIndicator;
	
	Typeface tfPrototype;
	
	TextView txtTutorial;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_help);
        
        helpPagerAdapter = new HelpPagerAdapter(getSupportFragmentManager());

        helpPager = (ViewPager)findViewById(R.id.pager);
        helpPager.setAdapter(helpPagerAdapter);

        circleIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        circleIndicator.setViewPager(helpPager);
        
        circleIndicator.setPageColor(Color.parseColor("#bfbfbf"));
        circleIndicator.setFillColor(Color.parseColor("#ff1654"));
		
        tfPrototype = Typeface.createFromAsset(getAssets(), "fonts/Prototype.ttf");
        txtTutorial = (TextView) findViewById(R.id.txtTutorial);
        
        txtTutorial.setTypeface(tfPrototype);
        
        
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
