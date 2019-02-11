package com.example.brpper.redditviewer.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.brpper.redditviewer.PostStructure;
import com.example.brpper.redditviewer.RedditModel.Child;
import com.example.brpper.redditviewer.UserConfigFragment;
import com.example.brpper.redditviewer.fragments.PostsFragment;

import java.util.List;

public class MainPagerAdaper extends FragmentPagerAdapter {
	private static final String TAG = "MainPagerAdaper";

	public static final int NUM_PAGES = 2;
	private List<PostStructure> postStructureList;

	public MainPagerAdaper (FragmentManager fm) {
		super(fm);
	}


	@Override
	public Fragment getItem (int position) {
		Log.d(TAG, "getItem: Se pide el item " + position);
		if (position == 2)
		{
			return UserConfigFragment.newInstance();
		}

		return (PostsFragment.newInstance(position));
	}

	@Nullable
	@Override
	public CharSequence getPageTitle (int position) {
		switch (position) {
			case 0:
				return "/r/EarthPorn";
			case 1:
				return "/r/aww";
			case 2:
				return "Configuración";
			default:
				return "";
		}
	}

	@Override
	public int getCount () {
		return NUM_PAGES;
	}

	public void setPostStructureList (List<PostStructure> postStructureList) {
		this.postStructureList = postStructureList;
	}

	public List<PostStructure> getPostStructureList () {
		return postStructureList;
	}
}
