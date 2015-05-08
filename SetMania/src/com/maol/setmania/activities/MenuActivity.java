package com.maol.setmania.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.maol.setmania.R;


public class MenuActivity extends Activity 
	implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

	LinearLayout layoutButtonPlay;
	Button btnPlay;
	Button btnHighScore;
	Button btnHelp;
	ToggleButton btnMusic;
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
		
		btnPlay = (Button) findViewById(R.id.btn_play);
		btnHighScore = (Button) findViewById(R.id.btn_highscore);
		btnHelp = (Button) findViewById(R.id.btn_help);
		btnMusic = (ToggleButton) findViewById(R.id.btn_music);
		
		mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(Games.API).addScope(Games.SCOPE_GAMES)
        // add other APIs and scopes here as needed
        .build();
		
		
		SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
		int music = sp.getInt("music", 1);
		
		if(music == 1) {
			btnMusic.setChecked(true);
		} else {
			btnMusic.setChecked(false);
		}
		
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
		boolean on = ((ToggleButton) v).isChecked();
	    if (on) {
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
	}

	@Override
	protected void onStop() {
	    super.onStop();
	//    mGoogleApiClient.disconnect();
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
