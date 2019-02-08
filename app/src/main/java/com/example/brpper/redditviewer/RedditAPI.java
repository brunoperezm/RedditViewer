package com.example.brpper.redditviewer;

import android.util.Log;

import com.example.brpper.redditviewer.RedditModel.Child;
import com.example.brpper.redditviewer.RedditModel.RedditRoot;
import com.example.brpper.redditviewer.adapters.ItemListAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.Callback;
import retrofit2.http.Path;

public final class RedditAPI implements Callback<RedditRoot> {
	private static final String TAG = "RedditAPI";
	public static final String API_URL = "https://www.reddit.com";

	private int mNumberOfPosts = 0;
	private ItemListAdapter mRVadapter;
	private List<Child> mList = null;
	ArrayList<PostStructure> mLista = new ArrayList<>();
	private short mSubredditType;

	public static final short EARTH = 0;
	public static final short ANIMALS = 1;
	DaoSession mDaoSession;
	public RedditAPI() {}
	public RedditAPI(DaoSession daoSession) {mDaoSession = daoSession;}

	public void get(short subreddit) {
		this.mSubredditType = subreddit;
		Gson gson = new GsonBuilder().setLenient().create();

		Retrofit retrofit  = new Retrofit.Builder()
				.baseUrl(API_URL)
				.addConverterFactory(GsonConverterFactory.create(gson))
				.build();
		RedditAPIInterface rapi = retrofit.create(RedditAPIInterface.class);

		switch (subreddit) {
			case EARTH:
				Log.d(TAG, "get: Se ha pedido hacer un request de subreddit EARTH" + mSubredditType);
				rapi.getAPI("EarthPorn").enqueue(this);
				break;
			case ANIMALS:
				Log.d(TAG, "get: Se ha pedido hacer un request de subreddit ANIMALS" + mSubredditType);
				rapi.getAPI("aww").enqueue(this);
				break;
			default:
				break;
		}

	}



	public void setRvAdapter (ItemListAdapter adapter) {
		Log.d(TAG, "setRvAdapter: Seteando el adapter");
		mRVadapter = adapter;
	}

	public int getmNumberOfPosts () {
		return mNumberOfPosts;
	}

	public List<Child> getmList () {
		return mList;
	}

	public void setmNumberOfPosts (int mNumberOfPosts) {
		this.mNumberOfPosts = mNumberOfPosts;
	}

	public void setmList (List<Child> mList) {
		this.mList = mList;
	}

	public ArrayList<PostStructure> getmLista () {
		return mLista;
	}

	public interface RedditAPIInterface {
		@GET("/r/{subreddit}/new.json?sort=new&limit=25")
		Call<RedditRoot> getAPI(@Path("subreddit") String subreddit);
	}

	@Override
	public void onResponse (Call<RedditRoot> call, Response<RedditRoot> response) {
		Log.d(TAG, "onResponse: Cant de posts" + response.body().getData().getChildren().size());
		mNumberOfPosts = response.body().getData().getChildren().size();
		mList = response.body().getData().getChildren();
		List<Child> mFilteredList = new ArrayList<>();
		Log.d(TAG, "onResponse: se procede a ver si hay que guardar en la DB");


		for (Child resultItem:
			 mList) {
			if (resultItem.getData().getIsVideo() == false && (
					resultItem.getData().getUrl().endsWith(".jpg") ||
					resultItem.getData().getUrl().endsWith(".jpeg") ||
					resultItem.getData().getUrl().endsWith(".png"))
					)
			{
				mFilteredList.add(resultItem);
				RedditPostDB rdb = new RedditPostDB();
				rdb.setSubredditId(new Long (mSubredditType));
				rdb.setAuthor(resultItem.getData().getUrl());
				rdb.setUrl(resultItem.getData().getUrl());
				rdb.setName(resultItem.getData().getTitle());
				rdb.setInternalPostId(resultItem.getData().getId());
				mDaoSession.getRedditPostDBDao().insertOrReplace(rdb);
			}

		}
		mNumberOfPosts = mFilteredList.size();
		updateItems(mFilteredList);
	}

	@Override
	public void onFailure (Call<RedditRoot> call, Throwable t) {
		Log.e(TAG, "onFailure: Ha fallado ", t);
	}

	private void updateItems(List<Child> mList) {

		for (int i =0 ; i<mNumberOfPosts; i++) {
			mLista.add(new PostStructure(mList.get(i).getData().getTitle(), mList.get(i).getData().getUrl()));
		}
		if (mRVadapter != null) {
			Log.d(TAG, "onResponse: Notificando que cambio el dataset");
			mRVadapter.swap(mLista);
		}
		Log.d(TAG, "updateItems: Vamos a enviar un evento con  "+ mSubredditType + ((mSubredditType == EARTH) ? "EARTH" : "ANIMALS"));
		//EventBus.getDefault().post(new ListLoadedEvent((mSubredditType == EARTH) ? "EARTH" : "ANIMALS", mLista, mSubredditType));

	}
}
