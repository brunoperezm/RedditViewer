package com.example.brpper.redditviewer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brpper.redditviewer.MainActivity;
import com.example.brpper.redditviewer.PostStructure;
import com.example.brpper.redditviewer.PostViewActivity;
import com.example.brpper.redditviewer.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> {
	private static final String TAG = "ItemListAdapter";
	private List<PostStructure> mlistPostStructure;
	private Context mContext;
	private Activity mActivity;

	public ItemListAdapter (List<PostStructure> lista) {
		mlistPostStructure = lista;
	}

	@Override
	public int getItemCount () { return (mlistPostStructure == null) ? 0 : mlistPostStructure.size();}

	@Override
	public ItemViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
		ViewGroup v = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post,parent, false);

		final ImageView imageView = v.findViewById(R.id.image_post);
		final ItemViewHolder ivh = new ItemViewHolder(v);

		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View v) {
//				Toast.makeText(v.getContext(), "Hola " + ivh.getAdapterPosition(), Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(v.getContext(), PostViewActivity.class);
				intent.putExtra(MainActivity.KEY_POST_ID,""+ ivh.getAdapterPosition());
				intent.putExtra(MainActivity.KEY_POST_IMAGE_URL,""+ mlistPostStructure.get(ivh.getAdapterPosition()).getUrl() );
				ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, imageView, "imageDetailTransition");
				((Activity) v.getContext()).startActivityForResult(intent, 54, optionsCompat.toBundle());
			}
		});
		return ivh;
	}

	@Override
	public void onBindViewHolder (ItemViewHolder holder, int position) {
		holder.tv.setText(mlistPostStructure.get(position).getName());
		Picasso.get()
				.load(mlistPostStructure.get(position).getUrl())
				.fit()
				.centerCrop()
				.into(holder.iv);
	}

	@Override
	public void onAttachedToRecyclerView (RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
	}

	public class ItemViewHolder extends RecyclerView.ViewHolder {
		ImageView iv;
		TextView tv;
		View containerView;
		public ItemViewHolder (View view) {
			super(view);
			containerView = view.findViewById(R.id.redditPost);
			iv = view.findViewById(R.id.image_post);
			tv = view.findViewById(R.id.title_post);
		}
	}
	public void swap(List<PostStructure> list) {
		Log.d(TAG, "swap: vamos a hacer un swap con una lista de tamaño: " + list.size());
		if (mlistPostStructure != null) {
			mlistPostStructure.clear();
			mlistPostStructure.addAll(list);
		}
		else {
			mlistPostStructure = list;
		}
		notifyDataSetChanged();
	}

	public Context getmContext () {
		return mContext;
	}

	public void setmContext (Context mContext) {
		this.mContext = mContext;
	}

	public Activity getmActivity () {
		return mActivity;
	}

	public void setmActivity (Activity mActivity) {
		this.mActivity = mActivity;
	}
}
