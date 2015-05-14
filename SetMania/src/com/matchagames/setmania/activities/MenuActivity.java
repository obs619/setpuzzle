package com.matchagames.setmania.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.matchagames.setmania.R;
import com.matchagames.setmania.activities.SetMania.TrackerName;


public class MenuActivity extends Activity 
	implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

	LinearLayout layoutButtonPlay;
	ImageButton btnPlay;
	ImageButton btnHighScore;
	ImageButton btnHelp;
	ImageButton btnMusic;
	ImageButton btnSound;
	public static GoogleApiClient mGoogleApiClient;
	
	private static int RC_SIGN_IN = 9001;

	private boolean mResolvingConnectionFailure = false;
	private boolean mAutoStartSignInFlow = true;
	private boolean mSignInClicked = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.activity_menu);
		
		AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
		
		btnPlay = (ImageButton) findViewById(R.id.btn_play);
		btnHighScore = (ImageButton) findViewById(R.id.btn_highscore);
		btnHelp = (ImageButton) findViewById(R.id.btn_help);
		btnMusic = (ImageButton) findViewById(R.id.btn_music);
		btnSound = (ImageButton) findViewById(R.id.btn_sound);
		
		mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(Games.API).addScope(Games.SCOPE_GAMES)
        // add other APIs and scopes here as needed
        .build();
		
		SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
		int music = sp.getInt("music", 1);
		
		if(music == 1) {
			btnMusic.setSelected(false);
		}else {
			btnMusic.setSelected(true);
		}
		
		SharedPreferences sp1 = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
		int sound = sp1.getInt("sound", 1);
		
		if(sound == 1) {
			btnSound.setSelected(false);
		}else {
			btnSound.setSelected(true);
		}
		
		Tracker t = ((SetMania) getApplication()).getTracker(TrackerName.APP_TRACKER);
			
		t.setScreenName("MenuActivity");		
		t.send(new HitBuilders.AppViewBuilder().build());
		
		t.enableAdvertisingIdCollection(true);
	}
	
	public void clickRate(View v) {
		String str ="https://play.google.com/store/apps/details?id=com.matchagames.setmania";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
	}
	
	public void clickPlay(View v) {
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	
	public void clickHighScore(View v) {
		startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
		        getString(R.string.leaderboard_id)), 0);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	
	public void clickHelp(View v) {
		Intent i = new Intent(this, HelpActivity.class);
		startActivity(i);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	
	public void clickMusic(View v) {
		btnMusic.setSelected(!((ImageButton)v).isSelected());
	    if (!btnMusic.isSelected()) {
	    	SharedPreferences sp2 = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp2.edit();
			editor.putInt("music", 1);
			editor.commit();
	    } else {
	    	SharedPreferences sp2 = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp2.edit();
			editor.putInt("music", 0);
			editor.commit();
	   }
	}
	
	public void clickSound(View v) {
		btnSound.setSelected(!((ImageButton)v).isSelected());
	    if (!btnSound.isSelected()) {
	    	SharedPreferences sp2 = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp2.edit();
			editor.putInt("sound", 1);
			editor.commit();
	    } else {
	    	SharedPreferences sp2 = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp2.edit();
			editor.putInt("sound", 0);
			editor.commit();
	   }
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (mResolvingConnectionFailure) {
	        // already resolving
	        return;
	    }

	    // if the sign-in button was clicked or if auto sign-in is enabled,
	    // launch the sign-in flow
	    if (mSignInClicked || mAutoStartSignInFlow) {
	        mAutoStartSignInFlow = false;
	        mSignInClicked = false;
	        mResolvingConnectionFailure = true;

	        // Attempt to resolve the connection failure using BaseGameUtils.
	        // The R.string.signin_other_error value should reference a generic
	        // error string in your strings.xml file, such as "There was
	        // an issue with sign-in, please try again later."
	        if (!BaseGameUtils.resolveConnectionFailure(this,
	                mGoogleApiClient, connectionResult,
	                RC_SIGN_IN, getString(R.string.signin_other_error))) {
	            mResolvingConnectionFailure = false;
	        }
	    }

	    // Put code here to display the sign-in button
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onStart() {
	    super.onStart();
	    mGoogleApiClient.connect();
	    GoogleAnalytics.getInstance(this).reportActivityStart(this);

	}

	@Override
	protected void onStop() {
	    super.onStop();
	//    mGoogleApiClient.disconnect();
	    GoogleAnalytics.getInstance(this).reportActivityStop(this);

	}
	
	
	protected void onActivityResult(int requestCode, int resultCode,
	        Intent intent) {
	    if (requestCode == RC_SIGN_IN) {
	        mSignInClicked = false;
	        mResolvingConnectionFailure = false;
	        if (resultCode == RESULT_OK) {
	            mGoogleApiClient.connect();
	        } else {
	            // Bring up an error dialog to alert the user that sign-in
	            // failed. The R.string.signin_failure should reference an error
	            // string in your strings.xml file that tells the user they
	            // could not be signed in, such as "Unable to sign in."
	            BaseGameUtils.showActivityResultError(this,
	                requestCode, resultCode, R.string.signin_failure);
	        }
	    }
	}
	
}
