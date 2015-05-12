package com.matchagames.setmania.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.matchagames.setmania.R;

public class TutorialFragment extends Fragment {
	
	ImageView imgTutorial;
	int tutorialNumber = 0;
	
	
	public static TutorialFragment newInstance(int tutorialNumber) {
		TutorialFragment tutoFragment = new TutorialFragment();
		
		Bundle args = new Bundle();
        args.putInt("tuto_num", tutorialNumber);
        tutoFragment.setArguments(args);
		
		return tutoFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tutorial, container, false);
		
		tutorialNumber = getArguments().getInt("tuto_num", 0);
		
		imgTutorial = (ImageView) view.findViewById(R.id.imgTutorial);
		
		if(tutorialNumber == 1)
			imgTutorial.setImageResource(R.drawable.tutorial_1);
		else if (tutorialNumber == 2)
			imgTutorial.setImageResource(R.drawable.tutorial_2);
		else if(tutorialNumber == 3)
			imgTutorial.setImageResource(R.drawable.tutorial_3);
		else if(tutorialNumber == 4)
			imgTutorial.setImageResource(R.drawable.tutorial_4);
		else if(tutorialNumber == 5)
			imgTutorial.setImageResource(R.drawable.tutorial_5);
		
		
		return view;
	}
	
}

