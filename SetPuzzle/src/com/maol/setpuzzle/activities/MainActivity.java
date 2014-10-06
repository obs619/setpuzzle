package com.maol.setpuzzle.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
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
	
	private int[] picturesList = {
			R.drawable.dog_black_blue_selector,
			R.drawable.cat_black_blue_selector,
			R.drawable.goat_black_blue_selector,
			R.drawable.dog_black_blue_selector,
			R.drawable.cat_black_blue_selector,
			R.drawable.goat_black_blue_selector,
			R.drawable.dog_black_blue_selector,
			R.drawable.cat_black_blue_selector,
			R.drawable.goat_black_blue_selector
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        pictures = new Picture[9];
        setOnClickListenersAndInitPictureList();
        ssi.initAllPossibleSets(pictures);
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
                if(button.isSelected())
                	button.setSelected(false);
                else {
                	selectedCounter++;
                	
                	if(selectedCounter <= 3) {
                		answers[selectedCounter - 1] = btnIndex;
                		
                		if(selectedCounter == 3) {
                			Answer a = new Answer(answers);
                			boolean isCorrect = ssi.isCorrectAnswer(a);
                			
                			Toast.makeText(activity, isCorrect + "", Toast.LENGTH_LONG).show();
                		}
                    }
                	button.setSelected(true);
                }
                	
              
        }
    }
    
}
