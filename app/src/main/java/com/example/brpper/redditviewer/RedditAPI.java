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

	public static final short EARTH = 0;
	public static final short ANIMALS = 1;
	public RedditAPI() {
	}

	public void get(short subreddit) {
		Gson gson = new GsonBuilder().setLenient().create();

		Retrofit retrofit  = new Retrofit.Builder()
				.baseUrl(API_URL)
				.addConverterFactory(GsonConverterFactory.create(gson))
				.build();
		RedditAPIInterface rapi = retrofit.create(RedditAPIInterface.class);

		switch (subreddit) {
			case EARTH:
				rapi.getAPI("EarthPorn").enqueue(this);
				break;
			case ANIMALS:
				rapi.getAPI("aww").enqueue(this);
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
		@GET("/r/{subreddit}/top.json?sort=new&limit=10")
		Call<RedditRoot> getAPI(@Path("subreddit") String subreddit);
	}

	@Override
	public void onResponse (Call<RedditRoot> call, Response<RedditRoot> response) {
		Log.d(TAG, "onResponse: Cant de posts" + response.body().getData().getChildren().size());
		mNumberOfPosts = response.body().getData().getChildren().size();
		mList = response.body().getData().getChildren();
		updateItems(mList);
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
			EventBus.getDefault().post(new ListLoadedEvent("Mi mensaje"));
			mRVadapter.swap(mLista);
		}

	}
}
