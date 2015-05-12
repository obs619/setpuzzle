package com.matchagames.setmania.activities;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.matchagames.setmania.fragments.TutorialFragment;

public class HelpPagerAdapter extends FragmentStatePagerAdapter {

	FragmentManager oFragmentManager;
    ArrayList<Fragment> oPooledFragments;
    
	public HelpPagerAdapter(FragmentManager fm) {
		super(fm);
		oFragmentManager=fm;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0: return TutorialFragment.newInstance(1);
			case 1: return TutorialFragment.newInstance(2);
			case 2: return TutorialFragment.newInstance(3);
			case 3: return TutorialFragment.newInstance(4);
			case 4: return TutorialFragment.newInstance(5);
			default: return null;
		}
	}

	@Override
	public int getCount() {
		return 5;
	}
}