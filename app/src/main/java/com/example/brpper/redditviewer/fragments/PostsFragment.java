package com.example.brpper.redditviewer.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.brpper.redditviewer.MainActivity;
import com.example.brpper.redditviewer.PostStructure;
import com.example.brpper.redditviewer.R;
import com.example.brpper.redditviewer.RedditAPI;
import com.example.brpper.redditviewer.adapters.ItemListAdapter;

import java.util.ArrayList;


public class PostsFragment extends Fragment {
	private static final String TAG = "PostsFragment";
	// Llaves del bundle
	public static final String PAGE_KEY = "1";
	private int mPage;



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
		if (getArguments() != null) {
			mPage = getArguments().getInt(PAGE_KEY);
			
		}
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
							  Bundle savedInstanceState) {


		View result = inflater.inflate(R.layout.fragment_posts, container, false);
		RecyclerView rv = result.findViewById(R.id.rvPosts);

		Log.d(TAG, "onCreateView: vemos si rv tiene parent" + ((rv.getParent() == null) ? " es null" : "NO es null"));

		LinearLayoutManager llm = new LinearLayoutManager(getContext());


		ArrayList<PostStructure> lista = new ArrayList<>();
		ItemListAdapter adapter = new ItemListAdapter(lista);
		RedditAPI rapi = new RedditAPI();
		switch (mPage) {
			case 0:
				rapi.setRvAdapter(adapter);
				rapi.get(RedditAPI.EARTH);
				break;
			case 1:
				rapi.setRvAdapter(adapter);
				rapi.get(RedditAPI.ANIMALS);
			break;
			default:
				if (((MainActivity) getActivity()).postStructuresList != null)
				for (int i =0 ; i<((MainActivity) getActivity()).postStructuresList.size(); i++)
					lista.add(new PostStructure(((MainActivity) getActivity()).postStructuresList.get(i).getName(), ((MainActivity) getActivity()).postStructuresList.get(i).getUrl()));
				break;
		}




		rv.setAdapter(adapter);
		rv.setLayoutManager(llm);
		return result;
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
	}
}
