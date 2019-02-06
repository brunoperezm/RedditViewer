package com.example.brpper.redditviewer.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.brpper.redditviewer.fragments.PostsFragment;

public class MainPagerAdaper extends FragmentPagerAdapter {
	private static final String TAG = "MainPagerAdaper";
	public static final int NUM_PAGES = 3;

	public MainPagerAdaper (FragmentManager fm) {
		super(fm);
	}


	@Override
	public Fragment getItem (int position) {
		Log.d(TAG, "getItem: Se pide el item " + position);
		return (PostsFragment.newInstance(position));
	}

	@Nullable
	@Override
	public CharSequence getPageTitle (int position) {
		return "Titulo de pagina " + position;
	}

	@Override
	public int getCount () {
		return NUM_PAGES;
	}
}
