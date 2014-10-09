package com.maol.setpuzzle.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
	TextView txtTotalAnswers;
	ListView listViewAnswersSolved;
	private int[] picturesList = {
			R.drawable.blue_cat_blue_selector,
			R.drawable.blue_cat_red_selector,
			R.drawable.blue_cat_yellow_selector,
			R.drawable.blue_dog_blue_selector,
			R.drawable.blue_dog_red_selector,
			R.drawable.blue_dog_yellow_selector,
			R.drawable.blue_mouse_blue_selector,
			R.drawable.blue_mouse_red_selector,
			R.drawable.blue_mouse_yellow_selector,
			
			R.drawable.red_cat_blue_selector,
			R.drawable.red_cat_red_selector,
			R.drawable.red_cat_yellow_selector,
			R.drawable.red_dog_blue_selector,
			R.drawable.red_dog_red_selector,
			R.drawable.red_dog_yellow_selector,
			R.drawable.red_mouse_blue_selector,
			R.drawable.red_mouse_red_selector,
			R.drawable.red_mouse_yellow_selector,
			
			R.drawable.yellow_cat_blue_selector,
			R.drawable.yellow_cat_red_selector,
			R.drawable.yellow_cat_yellow_selector,
			R.drawable.yellow_dog_blue_selector,
			R.drawable.yellow_dog_red_selector,
			R.drawable.yellow_dog_yellow_selector,
			R.drawable.yellow_mouse_blue_selector,
			R.drawable.yellow_mouse_red_selector,
			R.drawable.yellow_mouse_yellow_selector
	};

	List<String> lstAnswersSolved;
	ArrayAdapter<String> answersSolvedAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtTotalAnswers = (TextView)findViewById(R.id.txtTotalAnswers);
        listViewAnswersSolved = (ListView)findViewById(R.id.lstViewAnswersSolved);
        
        lstAnswersSolved = new ArrayList<String>();
        
        answersSolvedAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lstAnswersSolved);

        listViewAnswersSolved.setAdapter(answersSolvedAdapter);
        
        activity = this;
        pictures = new Picture[9];
        
        shuffleIntArray(picturesList);
        
        setOnClickListenersAndInitPictureList();
        ssi.initAllPossibleSets(pictures);
        
        txtTotalAnswers.setText("Total Answers: " + ssi.getCorrectAnswers().size()); 
        
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
                        pictures[x] = getPicture(picturesList[x]);
                    }
            	}else if(i == 1) {
            		TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
                    for(int x = 0; x < tableRow.getChildCount(); x++) {
                        Button button = (Button) tableRow.getChildAt(x);                   
                        button.setBackgroundResource(picturesList[x + 3]);
                        button.setOnClickListener(new PictureClick( x + 3));
                        pictures[x + 3] = getPicture(picturesList[x + 3]);
                    }
            	}else if(i == 2) {
            		TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
                    for(int x = 0; x < tableRow.getChildCount(); x++) {
                        Button button = (Button) tableRow.getChildAt(x);                   
                        button.setBackgroundResource(picturesList[x + 6]);
                        button.setOnClickListener(new PictureClick( x + 6));
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
    	picture.setAnimal(parts[0]);
    	picture.setAnimalColor(parts[1]);
    	picture.setBackground(parts[2]);
    	
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
            			Answer a = new Answer(getSelectedButtons());
            			
            			if(ssi.isCorrectAnswer(a)) {
            				lstAnswersSolved.add(a.toString());
                			answersSolvedAdapter.notifyDataSetChanged();
                			unselectButtons();
                			Toast.makeText(activity, "Correct!", Toast.LENGTH_LONG).show();
            			}else {
            				unselectButtons();
            				Toast.makeText(activity, "Duplicate or wrong answer!", Toast.LENGTH_LONG).show();
            			}
            			
            			answers = new int[3];
            			selectedCounter = 0;
            		}
                		
                }
        }
    }
    
}
