package com.example.brpper.redditviewer;

import java.util.ArrayList;
import java.util.List;

public class ListLoadedEvent {
	public final String message;
	private ArrayList<PostStructure> mListaPosts;
	private short subredditType;


	public ListLoadedEvent(String message, List<PostStructure> postList, short subredditType) {
		this.message = message;
		mListaPosts = (ArrayList<PostStructure>) postList;
		this.subredditType = subredditType;
	}

	public ArrayList<PostStructure> getmListaPosts () {
		return mListaPosts;
	}

	public short getSubredditType () {
		return subredditType;
	}
}
