package com.example.brpper.redditviewer.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.brpper.redditviewer.ListLoadedEvent;
import com.example.brpper.redditviewer.MainActivity;
import com.example.brpper.redditviewer.PostStructure;
import com.example.brpper.redditviewer.R;
import com.example.brpper.redditviewer.RedditAPI;
import com.example.brpper.redditviewer.RedditApp;
import com.example.brpper.redditviewer.adapters.ItemListAdapter;
import com.example.brpper.redditviewer.adapters.MainPagerAdaper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


public class PostsFragment extends Fragment {
	private static final String TAG = "PostsFragment";
	// Llaves del bundle
	public static final String PAGE_KEY = "1";
	private int mPage;
	private ItemListAdapter mAdapter;
	private RecyclerView rv;
	private SwipeRefreshLayout swipeRefreshLayout;
	public PostsFragment () {}

	/**
	 * @param page
	 * @return Una nueva instancia del fragmento
	 */
	public static PostsFragment newInstance (int page) {
		PostsFragment fragment = new PostsFragment();
		Bundle args = new Bundle();
		args.putInt(PAGE_KEY, page);
		fragment.setArguments(args);

		Log.d(TAG, "newInstance: Se crea una nueva instancia del fragmento. El page es: " + page );
		return fragment;
	}

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		if (getArguments() != null) {
			mPage = getArguments().getInt(PAGE_KEY);
			
		}



	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
							  Bundle savedInstanceState) {


		View result = inflater.inflate(R.layout.fragment_posts, container, false);
		rv = result.findViewById(R.id.rvPosts);


		swipeRefreshLayout = (SwipeRefreshLayout) result.findViewById(R.id.swipeRefreshLayout);
		Log.d(TAG, "onCreateView: vemos si rv tiene parent" + ((rv.getParent() == null) ? " es null" : "NO es null"));

		LinearLayoutManager llm = new LinearLayoutManager(getContext());


		ArrayList<PostStructure> lista = new ArrayList<>();
		RedditAPI rapi = new RedditAPI();
		switch (mPage) {
			case 0:
				mAdapter = new ItemListAdapter( ((MainActivity)getActivity()).postStructuresListEARTH);
				//rapi.setRvAdapter(mAdapter);
				//rapi.get(RedditAPI.EARTH);
				break;
			case 1:
				mAdapter = new ItemListAdapter( ((MainActivity)getActivity()).postStructuresListANIMALS);
				//rapi.setRvAdapter(mAdapter);
				//rapi.get(RedditAPI.ANIMALS);
				break;
			default:
				mAdapter = new ItemListAdapter(lista);
				if (((MainActivity) getActivity()).postStructuresListANIMALS != null)
				for (int i =0 ; i<((MainActivity) getActivity()).postStructuresListANIMALS.size(); i++)
					lista.add(new PostStructure(((MainActivity) getActivity()).postStructuresListANIMALS.get(i).getName(), ((MainActivity) getActivity()).postStructuresListANIMALS.get(i).getUrl()));
				break;
		}




		rv.setAdapter(mAdapter);
		rv.setLayoutManager(llm);

		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Esto se ejecuta cada vez que se realiza el gesto
				if (((MainActivity)getActivity()).isNetworkAvailable()) {
					new RedditAPI(((RedditApp)getActivity().getApplication()).getDaoSession()).get(RedditAPI.EARTH);
					new RedditAPI(((RedditApp)getActivity().getApplication()).getDaoSession()).get(RedditAPI.ANIMALS);
				}
				MainActivity.MyTask myTask = ((MainActivity) getActivity()) . new MyTask(swipeRefreshLayout);
				myTask.execute();
			}
		});


		return result;
	}

	@Override
	public void onPause () {
		super.onPause();
		Log.d(TAG, "onPause: Pausado!!!!! " + mPage );
	}

	@Override
	public void onDestroyView () {
		Log.d(TAG, "onDestroyView: se destruye la vista " + mPage);
		super.onDestroyView();
	}

	@Override
	public void onDestroy () {
		Log.d(TAG, "onDestroyView: se destruye el fragmento " + mPage);

		super.onDestroy();
		EventBus.getDefault().unregister(this);

	}

	public ItemListAdapter getmAdapter () {
		return mAdapter;
	}

	public RecyclerView getRv () {
		return rv;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onListLoadedEvent(ListLoadedEvent event) {
		if (event == null)
			Log.e(TAG, "onListLoadedEvent: Event is null!!!!");
		//Toast.makeText(getContext(), "He recibido un evento con mensaje: " + event.message + event.getSubredditType(), Toast.LENGTH_SHORT).show();
		if (event.getSubredditType() == RedditAPI.ANIMALS)
			((MainActivity) getActivity()).postStructuresListANIMALS = new ArrayList<>(event.getmListaPosts());
		if (event.getSubredditType() == RedditAPI.EARTH)
			((MainActivity) getActivity()).postStructuresListEARTH = new ArrayList<>(event.getmListaPosts());

		if (mPage == 0 && ((MainActivity) getActivity()).postStructuresListEARTH != null) {
			mAdapter.swap(((MainActivity) getActivity()).postStructuresListEARTH);

		} else if (mPage == 1 && ((MainActivity) getActivity()).postStructuresListANIMALS != null) {
			mAdapter.swap(((MainActivity) getActivity()).postStructuresListANIMALS);
		}
	}
}
