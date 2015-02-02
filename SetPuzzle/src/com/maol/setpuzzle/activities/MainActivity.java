package com.maol.setpuzzle.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.maol.setpuzzle.R;
import com.maol.setpuzzle.models.Answer;
import com.maol.setpuzzle.models.Picture;
import com.maol.setpuzzle.service.SetServiceImpl;


public class MainActivity extends ActionBarActivity {

	Activity activity;
	int selectedCounter = 0;
	Picture[] pictures;
	int[] answers = new int[3];
	SetServiceImpl ssi = new SetServiceImpl();
	
	TextView txtTimer;
	TextView txtTotalAnswers;
	TextView txtScore;

    Typeface tfGadugi;
    Typeface tfGadugib;
    Typeface tfPrototype;
	
	GridView listViewAnswersSolved;
	private int[] picturesList = {
			R.drawable.orange_bone_orange_selector,
			R.drawable.orange_bone_red_selector,
			R.drawable.orange_bone_yellow_selector,
			R.drawable.orange_cat_orange_selector,
			R.drawable.orange_cat_red_selector,
			R.drawable.orange_cat_yellow_selector,
			R.drawable.orange_mouse_orange_selector,
			R.drawable.orange_mouse_red_selector,
			R.drawable.orange_mouse_yellow_selector,
			
			R.drawable.red_bone_orange_selector,
			R.drawable.red_bone_red_selector,
			R.drawable.red_bone_yellow_selector,
			R.drawable.red_cat_orange_selector,
			R.drawable.red_cat_red_selector,
			R.drawable.red_cat_yellow_selector,
			R.drawable.red_mouse_orange_selector,
			R.drawable.red_mouse_red_selector,
			R.drawable.red_mouse_yellow_selector,
			
			R.drawable.yellow_bone_orange_selector,
			R.drawable.yellow_bone_red_selector,
			R.drawable.yellow_bone_yellow_selector,
			R.drawable.yellow_cat_orange_selector,
			R.drawable.yellow_cat_red_selector,
			R.drawable.yellow_cat_yellow_selector,
			R.drawable.yellow_mouse_orange_selector,
			R.drawable.yellow_mouse_red_selector,
			R.drawable.yellow_mouse_yellow_selector
	};

	List<Answer> lstAnswersSolved;
	GridViewAnswerAdapter answersSolvedAdapter;
	
	int score = 0;
	
	private long totaltime = 60;
	private boolean paused = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        
        setContentView(R.layout.activity_main);
       
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        
        txtTimer = (TextView)findViewById(R.id.txtTimer);
        txtTotalAnswers = (TextView)findViewById(R.id.txtTotalAnswers);
        txtScore = (TextView)findViewById(R.id.txtScore);
        listViewAnswersSolved = (GridView)findViewById(R.id.lstViewAnswersSolved);
        
        tfGadugi = Typeface.createFromAsset(getAssets(), "fonts/gadugi.ttf");
        tfGadugib = Typeface.createFromAsset(getAssets(), "fonts/gadugib.ttf");
        tfPrototype = Typeface.createFromAsset(getAssets(), "fonts/Prototype.ttf");
        
        txtTimer.setTypeface(tfPrototype);
        txtTotalAnswers.setTypeface(tfPrototype);
        txtScore.setTypeface(tfPrototype);
        
        lstAnswersSolved = new ArrayList<Answer>();
        
        answersSolvedAdapter = new GridViewAnswerAdapter(this, lstAnswersSolved);

        listViewAnswersSolved.setAdapter(answersSolvedAdapter);
        
        activity = this;
        pictures = new Picture[9];
        
        shuffleIntArray(picturesList);
        
        setOnClickListenersAndInitPictureList();
        ssi.initAllPossibleSets(pictures);
        
        txtTotalAnswers.setText("Answers Left: " + ssi.getCorrectAnswers().size()); 

         Timer timer = new Timer();
         timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if(!paused) {
					totaltime++;
					MainActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							txtTimer.setText("0:" + totaltime);
							
							if(totaltime == 0) {

					            paused = true;
								AlertDialog.Builder builder = new AlertDialog.Builder(activity);
								builder.setTitle("Result");
							    builder.setCancelable(false);
							    builder.setMessage("Congratulations! Score: " + score) ;
							    builder.setPositiveButton("ok", new OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										finish();
									}
								});
							
								AlertDialog alert = builder.create();
					            alert.show();
					            
							}
							
						}
					});
				}
				
			}
		}, 0, 1000);
    
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	paused = true;
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	paused = false;
    }
    
    
    public void clickCheat(View v) {
		String answers = "";
		for(Answer answer: ssi.getCorrectAnswers())
			answers += answer + " | ";
		Toast.makeText(this, answers, Toast.LENGTH_LONG).show();
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
            	            @Override
            	            public void run() {
            	            	Answer a = new Answer(getSelectedButtons());
                    			
                    			if(ssi.isCorrectAnswer(a)) {
                    				score++;
                    				lstAnswersSolved.add(a);
                        			answersSolvedAdapter.notifyDataSetChanged();
                        			unselectButtons();
                        			txtTotalAnswers.setText("Answers Left: " + ssi.getCorrectAnswers().size()); 
                        			txtScore.setText("Score: " + score);
                        			Toast.makeText(activity, "Correct!", Toast.LENGTH_LONG).show();
                        			
                        			if(ssi.getCorrectAnswers().size() == 0)
                        				refreshGame();
                        			
                    			}else {
                    				score--;
                    				unselectButtons();
                    				txtScore.setText("Score: " + score);
                    				Toast.makeText(activity, "Duplicate or wrong answer!", Toast.LENGTH_LONG).show();
                    			}
                    			
                    			answers = new int[3];
                    			selectedCounter = 0;
            	            }
            	        }, 200);
            			
            			
            		}
                		
                }
        }
    }
    
    
    private void refreshGame() {
    	pictures = new Picture[9];
        
        shuffleIntArray(picturesList);
        
        setOnClickListenersAndInitPictureList();
        ssi.initAllPossibleSets(pictures);
        
        selectedCounter = 0;
        answers = new int[3];
        
        lstAnswersSolved.clear();
		answersSolvedAdapter.notifyDataSetChanged();
        
        txtTotalAnswers.setText("Total Answers Left: " + ssi.getCorrectAnswers().size()); 
    }
}
