package com.example.brpper.redditviewer;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.brpper.redditviewer.adapters.MainPagerAdaper;

public class MainActivity extends AppCompatActivity {

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

		ViewPager viewPager = findViewById(R.id.pager);
		viewPager.setAdapter(new MainPagerAdaper(getSupportFragmentManager()));
		TabLayout tabLayout = findViewById(R.id.tablayout);
		tabLayout.setupWithViewPager(viewPager);

	}


}
