package com.matchagames.setmania.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.games.Games;
import com.matchagames.setmania.R;
import com.matchagames.setmania.activities.SetMania.TrackerName;
import com.matchagames.setmania.models.Answer;
import com.matchagames.setmania.models.Picture;
import com.matchagames.setmania.service.SetServiceImpl;


public class MainActivity extends Activity {

	Activity activity;
	int selectedCounter = 0;
	Picture[] pictures;
	int[] answers = new int[3];
	SetServiceImpl ssi = new SetServiceImpl();
	
	TextView txtTimer;
	TextView txtScore;
	TextView txtBonusTime;
	

    Typeface tfGadugi;
    Typeface tfGadugib;
    Typeface tfPrototype;
	
    TableLayout tableLayout;
    
    Boolean isGamePaused = false;
    
	GridView listViewAnswersSolved;
	private int[] picturesList = {
			R.drawable.blue_bone_blue_selector,
			R.drawable.blue_bone_orange_selector,
			R.drawable.blue_bone_pink_selector,
			R.drawable.blue_cat_blue_selector,
			R.drawable.blue_cat_orange_selector,
			R.drawable.blue_cat_pink_selector,
			R.drawable.blue_mouse_blue_selector,
			R.drawable.blue_mouse_orange_selector,
			R.drawable.blue_mouse_pink_selector,
			
			R.drawable.orange_bone_blue_selector,
			R.drawable.orange_bone_orange_selector,
			R.drawable.orange_bone_pink_selector,
			R.drawable.orange_cat_blue_selector,
			R.drawable.orange_cat_orange_selector,
			R.drawable.orange_cat_pink_selector,
			R.drawable.orange_mouse_blue_selector,
			R.drawable.orange_mouse_orange_selector,
			R.drawable.orange_mouse_pink_selector,
			
			R.drawable.pink_bone_blue_selector,
			R.drawable.pink_bone_orange_selector,
			R.drawable.pink_bone_pink_selector,
			R.drawable.pink_cat_blue_selector,
			R.drawable.pink_cat_orange_selector,
			R.drawable.pink_cat_pink_selector,
			R.drawable.pink_mouse_blue_selector,
			R.drawable.pink_mouse_orange_selector,
			R.drawable.pink_mouse_pink_selector
	};

	List<Answer> lstAnswersSolved;
	GridViewAnswerAdapter answersSolvedAdapter;
	
	int score = 0;
	
	CountDownTimer countdownTimer;
	long initialTimeStart = 1 * 61000;
	long totalTimeLeft = 0;
	boolean startedTimeOnCreate = false;
	
	MediaPlayer mpCorrect;
	MediaPlayer mpWrong;
	
	MediaPlayer mpMusic;
	int medialength = 0;

	MediaPlayer mpGoodjob;
	
	InterstitialAd interstitialAD;
	
	Dialog gameOverDialog;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_main);
       
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        
        interstitialAD = new InterstitialAd(this);
        interstitialAD.setAdUnitId("ca-app-pub-4942808954389708/6161900877");

        AdRequest adRequest2 = new AdRequest.Builder().build();

        interstitialAD.loadAd(adRequest2);
        
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        
        txtTimer = (TextView)findViewById(R.id.txtTimer);
        txtScore = (TextView)findViewById(R.id.txtScore);
        txtBonusTime = (TextView)findViewById(R.id.txtBonusTime);
        listViewAnswersSolved = (GridView)findViewById(R.id.lstViewAnswersSolved);
        
        tfGadugi = Typeface.createFromAsset(getAssets(), "fonts/gadugi.ttf");
        tfGadugib = Typeface.createFromAsset(getAssets(), "fonts/gadugib.ttf");
        tfPrototype = Typeface.createFromAsset(getAssets(), "fonts/Prototype.ttf");
        
        txtTimer.setTypeface(tfPrototype);
        txtScore.setTypeface(tfPrototype);
        txtBonusTime.setTypeface(tfGadugi);
        
        mpCorrect = MediaPlayer.create(this, R.raw.correct);
        mpWrong = MediaPlayer.create(this, R.raw.wrong);
        mpMusic = MediaPlayer.create(this, R.raw.music);
        mpMusic.setLooping(true);
        mpGoodjob = MediaPlayer.create(this, R.raw.goodjob);
        
		SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
		int music = sp.getInt("music", 1);
		
		if(music == 1)
			mpMusic.start();
        
        lstAnswersSolved = new ArrayList<Answer>();
        
        answersSolvedAdapter = new GridViewAnswerAdapter(this, lstAnswersSolved);

        listViewAnswersSolved.setAdapter(answersSolvedAdapter);
        
        activity = this;
        pictures = new Picture[9];
        
        shuffleIntArray(picturesList);
        
        setOnClickListenersAndInitPictureList();
        ssi.initAllPossibleSets(pictures);
       
        for(Answer ans : ssi.getCorrectAnswers()) {
        	ans.setSolved(false);
        	lstAnswersSolved.add(ans);
			
        }
        
        Collections.shuffle(lstAnswersSolved);
        answersSolvedAdapter.notifyDataSetChanged();
        
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
            	createCountDownTimer();
            	startedTimeOnCreate = true;
            }
        }, 100);
        
        Tracker t = ((SetMania) getApplication()).getTracker(TrackerName.APP_TRACKER);
		
		t.setScreenName("MainActivity");		
		t.send(new HitBuilders.AppViewBuilder().build());
		
		t.enableAdvertisingIdCollection(true);
        
    }
    
    @Override
	protected void onStart() {
	    super.onStart();
	    GoogleAnalytics.getInstance(this).reportActivityStart(this);

	}

	@Override
	protected void onStop() {
	    super.onStop();
	    GoogleAnalytics.getInstance(this).reportActivityStop(this);
	}
    
    public void createCountDownTimer() {
    	
    	countdownTimer = new CountDownTimer(initialTimeStart, 250) {

            public void onTick(long millisUntilFinished) {

            	if (Math.round((float)millisUntilFinished / 1000.0f) != totalTimeLeft)
                {  
            		totalTimeLeft = Math.round((float)millisUntilFinished / 1000.0f);
            		Log.e("e",totalTimeLeft + "");
            		String minute=""+(millisUntilFinished/1000)/60;
                    String second=""+(millisUntilFinished/1000)%60;
                    
                    if((millisUntilFinished/1000)/60<10)
                        minute="0"+(millisUntilFinished/1000)/60;
                    
                    if((millisUntilFinished/1000)%60<10)
                        second="0"+(millisUntilFinished/1000)%60;
                    
                    txtTimer.setText(minute+":"+second);
                    
                }
            	
            }
            public void onFinish() {
            	txtTimer.setText("00:00");

            	showGameOverDialog();
            	
            }
         }.start();
    }

    public void showGameOverDialog() {
    	gameOverDialog = new Dialog(this);
    	gameOverDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	gameOverDialog.setContentView(R.layout.dialog_gameover);
    	gameOverDialog.setCancelable(false);
		
		TextView txtGameOver = (TextView) gameOverDialog.findViewById(R.id.txtGameOver); 
		TextView txtScore = (TextView) gameOverDialog.findViewById(R.id.Score); 
		TextView txtBest = (TextView) gameOverDialog.findViewById(R.id.Best); 
		
		txtGameOver.setTypeface(tfPrototype);
		txtScore.setTypeface(tfGadugi);
		txtBest.setTypeface(tfGadugi);
		
		
		TextView txtActualScore = (TextView) gameOverDialog.findViewById(R.id.txtActualScore); 
		TextView txtBestScore = (TextView) gameOverDialog.findViewById(R.id.txtBest);

		txtActualScore.setTypeface(tfGadugi);
		txtBestScore.setTypeface(tfGadugi);
		
		txtActualScore.setText(score + "");
		
		SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
		int bestScore = sp.getInt("best_score", score);
		
		
		if(score > bestScore) {
			SharedPreferences sp2 = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp2.edit();
			editor.putInt("best_score", score);
			editor.commit();
			
			txtBestScore.setText(score + "");
		}else if(score == bestScore) {
			SharedPreferences sp2 = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp2.edit();
			editor.putInt("best_score", score);
			editor.commit();
			
			txtBestScore.setText(score + "");
		}else if(score < bestScore) {
			txtBestScore.setText(bestScore + "");
		}
		
		if(MenuActivity.mGoogleApiClient.isConnected())
			Games.Leaderboards.submitScore(MenuActivity.mGoogleApiClient, getString(R.string.leaderboard_id), score);
		
		ImageButton btnHome = (ImageButton) gameOverDialog.findViewById(R.id.btnHome);
		ImageButton btnLeader = (ImageButton) gameOverDialog.findViewById(R.id.btnLeader);
		ImageButton btnReplay = (ImageButton) gameOverDialog.findViewById(R.id.btnPlayAgain);
		
		
		btnHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
            	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            	finish();
			}
		});
		
		btnLeader.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				startActivityForResult(Games.Leaderboards.getLeaderboardIntent(MenuActivity.mGoogleApiClient,
				        getString(R.string.leaderboard_id)), 0);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
		});
		
		btnReplay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
            	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            	finish();
            	startActivity(getIntent());
			}
		});
		
		gameOverDialog.show();
		
		int number = generateRandomNumber();
		
		if(number >= 1 && number <= 100)
			if (interstitialAD.isLoaded()) {
				interstitialAD.show();
	        }
		
    }
    
    public void showPauseDialog() {
    	final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_pause);
		dialog.setCancelable(false);
		
		TextView txtPause = (TextView) dialog.findViewById(R.id.txtPause); 
		TextView txtScore = (TextView) dialog.findViewById(R.id.Score); 
		TextView txtBest = (TextView) dialog.findViewById(R.id.Best); 
		
		txtPause.setTypeface(tfPrototype);
		txtScore.setTypeface(tfGadugi);
		txtBest.setTypeface(tfGadugi);
		
		
		TextView txtActualScore = (TextView) dialog.findViewById(R.id.txtActualScore); 
		TextView txtBestScore = (TextView) dialog.findViewById(R.id.txtBest);

		txtActualScore.setTypeface(tfGadugi);
		txtBestScore.setTypeface(tfGadugi);
		
		txtActualScore.setText(score + "");
		
		SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
		int bestScore = sp.getInt("best_score", score);
		
		txtBestScore.setText(bestScore + "");
		
		
		ImageButton btnHome = (ImageButton) dialog.findViewById(R.id.btnHome);
		ImageButton btnResume = (ImageButton) dialog.findViewById(R.id.btnResume);
		ImageButton btnReplay = (ImageButton) dialog.findViewById(R.id.btnPlayAgain);
		
		
		btnHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
            	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            	finish();
			}
		});
		
		btnResume.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				dialog.cancel();

		    	createCountDownTimerOnResume();
		    	tableLayout.setVisibility(View.VISIBLE);
		    	isGamePaused = false;
		    	
    	        tableLayout.setBackgroundColor(Color.WHITE);
			}
		});
		
		btnReplay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
            	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            	finish();
            	startActivity(getIntent());
			}
		});
		
		dialog.show();
    }
       
    public void createCountDownTimerOnResume() {
    	
    		countdownTimer = new CountDownTimer(totalTimeLeft * 1000, 250) {

                public void onTick(long millisUntilFinished) {

                	if (Math.round((float)millisUntilFinished / 1000.0f) != totalTimeLeft)
                    {  
                		totalTimeLeft = Math.round((float)millisUntilFinished / 1000.0f);
                		
                		String minute=""+(millisUntilFinished/1000)/60;
                        String second=""+(millisUntilFinished/1000)%60;
                        
                        if((millisUntilFinished/1000)/60<10)
                            minute="0"+(millisUntilFinished/1000)/60;
                        
                        if((millisUntilFinished/1000)%60<10)
                            second="0"+(millisUntilFinished/1000)%60;
                        
                        txtTimer.setText(minute+":"+second);
                    }
                }
                public void onFinish() {
                	txtTimer.setText("00:00");
                	
                	showGameOverDialog();
                }
             }.start();
    }
    
    public void createCountDownTimerOnNewPuzzle() {
    	
		countdownTimer = new CountDownTimer(totalTimeLeft * 1000 + 60000, 250) {

            public void onTick(long millisUntilFinished) {

            	if (Math.round((float)millisUntilFinished / 1000.0f) != totalTimeLeft)
                {  
            		if(gameOverDialog != null && gameOverDialog.isShowing())
    					gameOverDialog.cancel();
            		totalTimeLeft = Math.round((float)millisUntilFinished / 1000.0f);
            		
            		String minute=""+(millisUntilFinished/1000)/60;
                    String second=""+(millisUntilFinished/1000)%60;
                    
                    if((millisUntilFinished/1000)/60<10)
                        minute="0"+(millisUntilFinished/1000)/60;
                    
                    if((millisUntilFinished/1000)%60<10)
                        second="0"+(millisUntilFinished/1000)%60;
                    
                    txtTimer.setText(minute+":"+second);
                }
            }
            public void onFinish() {
            	txtTimer.setText("00:00");
            	
            	showGameOverDialog();
            }
         }.start();
}
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);  
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	if(!isGamePaused)
	    	if(countdownTimer != null)
	    		countdownTimer.cancel();
    	
    	if(mpMusic.isPlaying()) {
    		mpMusic.pause();
			medialength = mpMusic.getCurrentPosition();
		}
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	if(!isGamePaused)
	    	if(startedTimeOnCreate) 
	    		createCountDownTimerOnResume();
    	
    	SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
		int music = sp.getInt("music", 1);
		
		if(music == 1) {
			mpMusic.seekTo(medialength);
			mpMusic.start();
		}
    }
    
    public void clickCheat(View v) {
		String answers = "";
		for(Answer answer: ssi.getCorrectAnswers())
			answers += answer + " | ";
		Toast.makeText(this, answers, Toast.LENGTH_LONG).show();
	}

    public void clickPause(View v) {
    	if(!isGamePaused) {
    		tableLayout.setVisibility(View.INVISIBLE);
    		
    		countdownTimer.cancel();
    		isGamePaused = true;
        	showPauseDialog();
    	}
		
	}
    
	private void setOnClickListenersAndInitPictureList() {
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        for(int i = 0; i < tableLayout.getChildCount(); i++) {
            if(tableLayout.getChildAt(i) instanceof TableRow) {
            	if(i == 0) {
            		TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
                    for(int x = 0; x < tableRow.getChildCount(); x++) {
                        Button button = (Button) tableRow.getChildAt(x);                   
                        button.setBackgroundResource(picturesList[x]);
                        button.setOnClickListener(new PictureClick(x));
                        button.setTypeface(tfGadugi);
                        pictures[x] = getPicture(picturesList[x]);
                    }
            	}else if(i == 1) {
            		TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
                    for(int x = 0; x < tableRow.getChildCount(); x++) {
                        Button button = (Button) tableRow.getChildAt(x);                   
                        button.setBackgroundResource(picturesList[x + 3]);
                        button.setOnClickListener(new PictureClick( x + 3));
                        button.setTypeface(tfGadugi);
                        pictures[x + 3] = getPicture(picturesList[x + 3]);
                    }
            	}else if(i == 2) {
            		TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
                    for(int x = 0; x < tableRow.getChildCount(); x++) {
                        Button button = (Button) tableRow.getChildAt(x);                   
                        button.setBackgroundResource(picturesList[x + 6]);
                        button.setOnClickListener(new PictureClick( x + 6));
                        button.setTypeface(tfGadugi);
                        pictures[x + 6] = getPicture(picturesList[x + 6]);
                    }
            	}               
            }
        }
    }
    
	private void unselectButtons() {
		TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        for(int i = 0; i < tableLayout.getChildCount(); i++) {
            if(tableLayout.getChildAt(i) instanceof TableRow) {
        		TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
                for(int x = 0; x < tableRow.getChildCount(); x++) {
                    Button button = (Button) tableRow.getChildAt(x);  
                    if(button.isSelected())
                    	button.setSelected(false);
                }            	            
            }
        }
	}
	
	private int[] getSelectedButtons() {
		int index = 0;
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        for(int i = 0; i < tableLayout.getChildCount(); i++) {
            if(tableLayout.getChildAt(i) instanceof TableRow) {
            	if(i == 0) {
            		TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
                    for(int x = 0; x < tableRow.getChildCount(); x++) {
                        Button button = (Button) tableRow.getChildAt(x);                   
                        if(button.isSelected()) {
                        	answers[index] = x;
                        	index++;
                        }
                    }
            	}else if(i == 1) {
            		TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
                    for(int x = 0; x < tableRow.getChildCount(); x++) {
                        Button button = (Button) tableRow.getChildAt(x);                   
                        if(button.isSelected()) {
                        	answers[index] = x + 3;
                        	index++;
                        }
                    }
            	}else if(i == 2) {
            		TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
                    for(int x = 0; x < tableRow.getChildCount(); x++) {
                        Button button = (Button) tableRow.getChildAt(x);                   
                        if(button.isSelected()) {
                        	answers[index] = x + 6;
                        	index++;
                        }
                    }
            	}               
            }
        }
        
        return answers;
    }
	
	private void shuffleIntArray(int[] array)
	{
	    int index, temp;
	    Random random = new Random();
	    for (int i = array.length - 1; i > 0; i--)
	    {
	        index = random.nextInt(i + 1);
	        temp = array[index];
	        array[index] = array[i];
	        array[i] = temp;
	    }
	}
	
    private Picture getPicture(int resourceDrawableID) {
    	String selectorName = activity.getResources().getResourceEntryName(resourceDrawableID);
    	String[] parts = selectorName.split("_");
    	
    	Picture picture = new Picture();
    	picture.setBackground(parts[0]);
    	picture.setAnimal(parts[1]);
    	picture.setAnimalColor(parts[2]);
   
    	return picture;
    }
    
    private class PictureClick implements View.OnClickListener {
    	 
        private int btnIndex;
     
        public PictureClick(int btnIndex) {
            this.btnIndex = btnIndex;
        }
     
        @Override
        public void onClick(View view) {
                Button button = (Button) view;
                if(button.isSelected()) {
                	selectedCounter--;
                	button.setSelected(false);
                }
                else {
                	selectedCounter++;
                	button.setSelected(true);
                	
            		if(selectedCounter == 3) {
            			new Handler().postDelayed(new Runnable(){
            	            @SuppressLint("NewApi")
							@Override
            	            public void run() {
            	            	Answer a = new Answer(getSelectedButtons());
                    			
                    			if(ssi.isCorrectAnswer(a)) {
                    				SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
                    				int sound = sp.getInt("sound", 1);
                    				
                    				if(sound == 1) {
                        				mpCorrect.start();
                    				}
                    				score++;
                    				for(Answer answer : lstAnswersSolved) {
                    					if(answer.toString().equals(a.toString()))
                    						answer.setSolved(true);
                    				}
                        			answersSolvedAdapter.notifyDataSetChanged();
                        			unselectButtons();
                        			txtScore.setText("Score: " + score);
                        			
                        			if(ssi.getCorrectAnswers().size() == 0) {
                        				popBonusTime();
                        				refreshGame();
                        			}
                        			
                    			}else {
                    				SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
                    				int sound = sp.getInt("sound", 1);
                    				
                    				if(sound == 1) {
                        				mpWrong.start();
                    				}
                    				score--;
                    				unselectButtons();
                    				txtScore.setText("Score: " + score);
                    				
                    				ColorDrawable f = new ColorDrawable(new Color().parseColor("#C80000"));
                    				ColorDrawable f1 = new ColorDrawable(Color.WHITE);

                    	            AnimationDrawable ad = new AnimationDrawable();
                    	            ad.addFrame(f, 200);
                    	            ad.addFrame(f1, 200);
                    	            ad.setOneShot(true);
                    				
                    	            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        	            tableLayout.setBackground(ad);
                    	            }else {
                    	        	    tableLayout.setBackgroundDrawable(ad);
                    	            }
                    	            
                    	            ad.start();
                    				
                    			}
                    			
                    			answers = new int[3];
                    			selectedCounter = 0;
            	            }
            	        }, 100);
            		}else if(selectedCounter > 3) {
            			unselectButtons();
            		}
                		
                }
        }
    }
    
    private void refreshGame() {
    	
    	countdownTimer.cancel();
    	createCountDownTimerOnNewPuzzle();
        	
    	pictures = new Picture[9];
    	
        shuffleIntArray(picturesList);
        
        setOnClickListenersAndInitPictureList();
        ssi.initAllPossibleSets(pictures);
        
        selectedCounter = 0;
        answers = new int[3];
        
        lstAnswersSolved.clear();
		answersSolvedAdapter.notifyDataSetChanged();
         
        for(Answer ans : ssi.getCorrectAnswers()) {
        	ans.setSolved(false);
        	lstAnswersSolved.add(ans);
        }
        
        Collections.shuffle(lstAnswersSolved);
        answersSolvedAdapter.notifyDataSetChanged();
        
        unselectButtons();
        
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
            	if(ssi.getCorrectAnswers().size() == 0) {
                	refreshGameAgain();
                }
            }
        }, 500);
        
    }
    
    public void refreshGameAgain() {
    	
    	pictures = new Picture[9];
    	
        shuffleIntArray(picturesList);
        
        setOnClickListenersAndInitPictureList();
        ssi.initAllPossibleSets(pictures);
        
        selectedCounter = 0;
        answers = new int[3];
        
        lstAnswersSolved.clear();
		answersSolvedAdapter.notifyDataSetChanged();
         
        for(Answer ans : ssi.getCorrectAnswers()) {
        	ans.setSolved(false);
        	lstAnswersSolved.add(ans);
        }
        
        Collections.shuffle(lstAnswersSolved);
        answersSolvedAdapter.notifyDataSetChanged();
        
        unselectButtons();
    	
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
            	if(ssi.getCorrectAnswers().size() == 0) {
                	refreshGameAgain();
                }
            }
        }, 500);
        
    }
    
    public int generateRandomNumber(){
    	int min = 1;
    	int max = 100;

    	Random r = new Random();
    	int intvalue = r.nextInt(max - min + 1) + min;
    	return intvalue;
    }
    
    public void popBonusTime() {
    	SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
		int sound = sp.getInt("sound", 1);
		
		if(sound == 1) {
			mpGoodjob.start();
		}
    	AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f ) ; 
        AlphaAnimation fadeOut = new AlphaAnimation( 1.0f , 0.0f ) ; 
        txtBonusTime.startAnimation(fadeIn);
        txtBonusTime.startAnimation(fadeOut);
        fadeIn.setDuration(500);
        fadeIn.setFillAfter(true);
        fadeOut.setDuration(500);
        fadeOut.setFillAfter(true);
        fadeOut.setStartOffset(500+fadeIn.getStartOffset());
    }
    
}
