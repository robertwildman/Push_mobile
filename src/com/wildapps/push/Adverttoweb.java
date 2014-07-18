package com.wildapps.push;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class Adverttoweb extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advertview);
		Intent i = new Intent(Intent.ACTION_VIEW);
		Bundle extras = getIntent().getExtras();
		String url = extras.getString("url");
		i.setData(Uri.parse(url));
		startActivity(i);
	}
}
