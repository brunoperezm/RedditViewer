package com.example.brpper.redditviewer;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.brpper.redditviewer.adapters.MainPagerAdaper;
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

	public List<PostStructure> postStructuresList;
	public List<PostStructure> postStructuresList2;
	// KEYS
	public static final String KEY_POST_ID = "0";
	public static final String KEY_POST_IMAGE_URL = "1";

	private MainPagerAdaper mainPagerAdaper;
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
		EventBus.getDefault().register(this);
		setContentView(R.layout.activity_main);

		ViewPager viewPager = findViewById(R.id.pager);
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
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				Log.d(TAG, "\n" + gson.toJson( operation.getResult() ));
			}
		});
		asyncSession.loadAll(RedditSubredditDB.class);
		asyncSession.loadAll(RedditPostDB.class);


		List<RedditSubredditDB> lista_de_subredits = ((RedditApp)getApplication()).getDaoSession().getRedditSubredditDBDao().queryBuilder()
				.orderAsc(RedditSubredditDBDao.Properties.Id)
				.list();
		List<RedditPostDB> lista_de_posts = ((RedditApp)getApplication()).getDaoSession().getRedditPostDBDao().queryBuilder()
				.orderAsc(RedditPostDBDao.Properties.Id)
				.list();

		Log.d(TAG, "onCreate: Consulta de lista de posts usando el query builder");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Log.d(TAG, "\n" + gson.toJson(lista_de_posts));

		redditAPI.get(RedditAPI.EARTH);
		// ES POSIBLE QUE COMO TARDA TAN POCO EN BUSCAR DE INTERNET NO SE NOTA LA ASINCRONICIDAD
		// EN TEORÍA EL redditAPI.getmLista() debería dar null hasta que lo busque en internet
		// TODO: para eso hay que usar un event bus. IMPLEMENTAR!!

		Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
		postStructuresList = redditAPI.getmLista();
		Log.d(TAG, "Estructura de lista \n" + gson2.toJson(postStructuresList));
	}

	@Override
	protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
		Toast.makeText(this, "requestCode " + requestCode + "resultCode " + resultCode, Toast.LENGTH_SHORT).show();
		Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
		postStructuresList = redditAPI.getmLista();
		Log.d(TAG, "Estructura de lista \n" + gson2.toJson(postStructuresList));
	}
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onListLoadedEvent(ListLoadedEvent event) {
		Toast.makeText(this, "He recibido un evento", Toast.LENGTH_SHORT).show();
		postStructuresList = redditAPI.getmLista();
		// TODO: queda hacer que cuando recibe el evento actualiza la lista. Tendría que ver cómo acceder al adaptador de cada lista. Probablemtene creandolos de antemano. haciendo 3 adapters static en POSTSFRAGMENT y que dea cuerdo al switch use el que corresponda

	}

	@Override
	protected void onDestroy () {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
