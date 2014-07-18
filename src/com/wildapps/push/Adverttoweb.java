package com.wildapps.push;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class Adverttoweb extends Activity{
	 private InterstitialAd interstitial;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advertview);
		//Will have the adverts displayed here.
		  // Create the interstitial.
	    interstitial = new InterstitialAd(this);
	    interstitial.setAdUnitId("ca-app-pub-2049126681125303/9962405275");

	    // Create ad request.
	    AdRequest adRequest = new AdRequest.Builder().build();

	    // Begin loading your interstitial.
	    interstitial.loadAd(adRequest);
		
		
		
		
	}
	public void displayInterstitial() {
	    if (interstitial.isLoaded()) {
	      interstitial.show();
	      Intent i = new Intent(Intent.ACTION_VIEW);
			Bundle extras = getIntent().getExtras();
			String url = extras.getString("url");
			i.setData(Uri.parse(url));
			if(url.isEmpty() == false)
			{	
			startActivity(i);
			}
	    }
	    
	  }
}
