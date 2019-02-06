package com.example.brpper.redditviewer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.brpper.redditviewer.PostStructure;
import com.example.brpper.redditviewer.R;

import java.util.List;


public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> {
	private static final String TAG = "ItemListAdapter";
	private List<PostStructure> mlistPostStructure;


	public ItemListAdapter (List<PostStructure> lista) {
		mlistPostStructure = lista;
	}

	@Override
	public int getItemCount () {
		return mlistPostStructure.size();
	}

	@Override
	public ItemViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
		ViewGroup v = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post,parent, false);

		ItemViewHolder ivh = new ItemViewHolder(v);
		return ivh;
	}

	@Override
	public void onBindViewHolder (ItemViewHolder holder, int position) {
		holder.tv.setText(mlistPostStructure.get(position).getName());
	}

	@Override
	public void onAttachedToRecyclerView (RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
	}

	public class ItemViewHolder extends RecyclerView.ViewHolder {
		ImageView iv;
		TextView tv;
		public ItemViewHolder (View view) {
			super(view);
			iv = view.findViewById(R.id.image_post);
			tv = view.findViewById(R.id.title_post);
		}
	}
}
