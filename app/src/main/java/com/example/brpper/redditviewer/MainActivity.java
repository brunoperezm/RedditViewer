package com.example.brpper.redditviewer;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.example.brpper.redditviewer.adapters.ItemListAdapter;
import com.example.brpper.redditviewer.adapters.MainPagerAdaper;
import com.example.brpper.redditviewer.fragments.PostsFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = "MainActivity";

	public List<PostStructure> postStructuresListEARTH;
	public List<PostStructure> postStructuresListANIMALS;
	// KEYS
	public static final String KEY_POST_ID = "0";
	public static final String KEY_POST_IMAGE_URL = "1";

	private MainPagerAdaper mainPagerAdaper;
	private ViewPager viewPager;
	RedditAPI redditAPI = new RedditAPI();

	// TODO: la idea es hacer una app que tenga 3 tabs y cada unos de esos tabs tenga un recyclerview y al hacer clic en un elemento se vaya a un coordinatorlayout. Los datos son los que se van a descargar de reddit. Las pestañas serán new top y trending y una cuarta de usuario tiene que tiene una mini configuración
	/*
	* TODO: Splash activity, guardar en greenDAO, hacer que al presionar item se abra una nueva actividad con una animación y que al presionar en cada item haya opciones
	*
	* */
	// Para recyclerview estuve usando https://code.tutsplus.com/es/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465
	// pag 459 del libro para hacer tabs
	@Override
	protected void onCreate (Bundle savedInstanceState) {


		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		viewPager = findViewById(R.id.pager);
		mainPagerAdaper = new MainPagerAdaper(getSupportFragmentManager());
		viewPager.setAdapter(mainPagerAdaper);
		TabLayout tabLayout = findViewById(R.id.tablayout);
		tabLayout.setupWithViewPager(viewPager);

		AsyncSession asyncSession = ((RedditApp)getApplication()).getDaoSession().startAsyncSession();
		asyncSession.setListener(new AsyncOperationListener() {
			@Override
			public void onAsyncOperationCompleted (AsyncOperation operation) {
				Log.d(TAG, "onAsyncOperationCompleted: Operancion asincrónica  de base de datos completada");
//				postStructuresList = (ArrayList<RedditPostDB>) operation.getResult();
				//Gson gson = new GsonBuilder().setPrettyPrinting().create();
				//Log.d(TAG, "\n" + gson.toJson( operation.getResult() ));
			}
		});
		asyncSession.loadAll(RedditSubredditDB.class);
		asyncSession.loadAll(RedditPostDB.class);


		MyTask myTask = new MyTask(null);
		myTask.execute();



	}

	@Override
	protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
		//Toast.makeText(this, "requestCode " + requestCode + "resultCode " + resultCode, Toast.LENGTH_SHORT).show();
	}



	@Override
	protected void onDestroy () {
		super.onDestroy();
		// Borrar luego!!!!!1
		//((RedditApp)getApplication()).getDaoSession().getRedditPostDBDao().deleteAll();
	}
	public class MyTask extends AsyncTask<Void, String, Void> {
		SwipeRefreshLayout swipe;
		public MyTask (SwipeRefreshLayout swipeRefreshLayout) {
			if (swipeRefreshLayout != null)	swipe= swipeRefreshLayout;
		}

		@Override
		protected Void doInBackground (Void... voids) {
			publishProgress("Working");
			List<RedditSubredditDB> lista_de_subredits = ((RedditApp)getApplication()).getDaoSession().getRedditSubredditDBDao().queryBuilder()
					.orderAsc(RedditSubredditDBDao.Properties.Id)
					.list();
			List<RedditPostDB> lista_de_posts_EARTH = ((RedditApp)getApplication()).getDaoSession().getRedditPostDBDao().queryBuilder()
					.where(RedditPostDBDao.Properties.SubredditId.eq(RedditAPI.EARTH))
					.orderDesc(RedditPostDBDao.Properties.Id)
					.list();
			List<RedditPostDB> lista_de_posts_ANIMALS = ((RedditApp)getApplication()).getDaoSession().getRedditPostDBDao().queryBuilder()
					.where(RedditPostDBDao.Properties.SubredditId.eq(RedditAPI.ANIMALS))
					.orderDesc(RedditPostDBDao.Properties.Id)
					.list();

			//Log.d(TAG, "onCreate: Consulta de lista de posts usando el query builder");
			//Gson gson = new GsonBuilder().setPrettyPrinting().create();
			//Log.d(TAG, "\n" + gson.toJson(lista_de_posts));
			postStructuresListANIMALS = new ArrayList<>();
			postStructuresListEARTH = new ArrayList<>();
			for (RedditPostDB postItem :
					lista_de_posts_ANIMALS) {
				android.util.Log.d(TAG, "doInBackground: name " + postItem.getName() + " url: " + postItem.getUrl());
				postStructuresListANIMALS.add(new PostStructure(postItem.getName(), postItem.getUrl()));
			}
			for (RedditPostDB postItem :
					lista_de_posts_EARTH) {
				postStructuresListEARTH.add(new PostStructure(postItem.getName(), postItem.getUrl()));
			}

				EventBus.getDefault().post(new ListLoadedEvent( "ANIMALS", postStructuresListANIMALS, RedditAPI.ANIMALS));
				EventBus.getDefault().post(new ListLoadedEvent( "EARTH", postStructuresListEARTH, RedditAPI.EARTH));

//			if (isNetworkAvailable()) {
//				new RedditAPI(((RedditApp)getApplication()).getDaoSession()).get(RedditAPI.EARTH);
//				new RedditAPI(((RedditApp)getApplication()).getDaoSession()).get(RedditAPI.ANIMALS);
//			}
			return null;
		}

		@Override
		protected void onPostExecute (Void swipeRefreshLayout) {
			if (swipe != null) swipe.setRefreshing(false);
			Log.d(TAG, "onPostExecute: Terminado el task");
		}

		@Override
		protected void onPreExecute () {
			Log.d(TAG, "onPreExecute: Iniciando el task");
		}

		@Override
		protected void onProgressUpdate (String... values) {
			Log.d(TAG, "onProgressUpdate: Progresando " + values[0]);
		}
	}


	public static class Log {

		public static void d(String TAG, String message) {
			int maxLogSize = 2000;
			for(int i = 0; i <= message.length() / maxLogSize; i++) {
				int start = i * maxLogSize;
				int end = (i+1) * maxLogSize;
				end = end > message.length() ? message.length() : end;
				android.util.Log.d(TAG, message.substring(start, end));
			}
		}

	}
	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
