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


public class MainActivity extends ActionBarActivity {

	Activity activity;
	int selectedCounter = 0;
	
	private int[] picturesList = {
			R.drawable.dog_black_blue_selector,
			R.drawable.cat_black_blue_selector,
			R.drawable.goat_black_blue_selector
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        setOnClickListeners();
    }


    private void setOnClickListeners() {
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        for(int i = 0; i < tableLayout.getChildCount(); i++) {
            if(tableLayout.getChildAt(i) instanceof TableRow) {
                TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
                for(int x = 0; x < tableRow.getChildCount(); x++) {
                    Button button = (Button) tableRow.getChildAt(x);                   
                    button.setBackgroundResource(picturesList[x]);
                    button.setOnClickListener(new PictureClick(picturesList[x]));
                }
            }
        }
    }
    
    private class PictureClick implements View.OnClickListener {
    	 
        private int btnText;
     
        public PictureClick(int btnText) {
            this.btnText = btnText;
        }
     
        @Override
        public void onClick(View view) {
                Button button = (Button) view;
                if(button.isSelected())
                	button.setSelected(false);
                else
                	button.setSelected(true);
                
                Toast.makeText(activity, "Clicked: " + activity.getResources().getResourceEntryName(btnText), Toast.LENGTH_LONG).show();
        }
    }
    
}
