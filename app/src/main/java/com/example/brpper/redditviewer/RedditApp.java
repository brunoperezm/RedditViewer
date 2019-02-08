package com.example.brpper.redditviewer;

import android.app.Application;
import android.util.Log;

import com.example.brpper.redditviewer.DaoMaster;
import com.example.brpper.redditviewer.DaoSession;
import com.example.brpper.redditviewer.RedditPostDB;

public class RedditApp extends Application {
	private static final String TAG = "RedditApp";
	private DaoSession mDaoSession;

	@Override
	public void onCreate () {
		super.onCreate();
		mDaoSession = new DaoMaster(
				new DaoMaster.DevOpenHelper(this, "post_v3.db").getWritableDb()).newSession();

		if (mDaoSession.getRedditSubredditDBDao().loadAll().size() == 0)
		{
			RedditSubredditDB subredditDB = new RedditSubredditDB();
			subredditDB.setName("EarthPorn");
			mDaoSession.insert(subredditDB);


			RedditSubredditDB subredditDB2 = new RedditSubredditDB();
			subredditDB2 = new RedditSubredditDB();
			subredditDB2.setName("aww");
			mDaoSession.insert(subredditDB2);
		}
	}

	public DaoSession getDaoSession() {
		return mDaoSession;
	}
}
