package com.example.brpper.redditviewer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PostViewActivity extends AppCompatActivity {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_view);
		Intent intent = getIntent();

		String mensaje = intent.getStringExtra(MainActivity.KEY_POST_ID);
		TextView tv = findViewById(R.id.hola);
		tv.setText(mensaje);

		Button button = findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View v) {
				Intent returnIntent = new Intent();
				setResult(200, returnIntent);
				finish();
			}
		});

		ImageView image = findViewById(R.id.imageView);
		Picasso.get().load(intent.getStringExtra(MainActivity.KEY_POST_IMAGE_URL))
				.fit().centerCrop().into(image);
	}

	@Override
	protected void onDestroy () {
		super.onDestroy();

	}
}
