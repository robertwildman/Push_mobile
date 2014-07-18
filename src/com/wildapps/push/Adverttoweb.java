package com.wildapps.push;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

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
	    interstitial.setAdListener(new AdListener(){
	          public void onAdLoaded(){
	               displayInterstitial();
	          }
	    
	});
		
		
		
	}
	protected void onResume(Bundle savedInstanceState)
	{
		 
	    
	}
	public void displayInterstitial() {
	    if (interstitial.isLoaded()) {
	      interstitial.show();
	      Runnable r = new Runnable() {
	    	    @Override
	    	    public void run(){
	    	    	try{
	  	    		  
	  	    	      Bundle extras = getIntent().getExtras();
	  	    			String url = extras.getString("url");
	  	    	      Intent intent = new Intent(Intent.ACTION_VIEW, 
	  	    				     Uri.parse(url));
	  	    				startActivity(intent);
	  	    	      }
	  	    	      catch(Exception e)
	  	    	      {
	  	    	    	  try
	  	    	    	  {
	  	    	    	  Bundle extras = getIntent().getExtras();
	  	    				String url = "http://"+extras.getString("url");
	  	    		      Intent intent = new Intent(Intent.ACTION_VIEW, 
	  	    					     Uri.parse(url));
	  	    					startActivity(intent);
	  	    	    	  }
	  	    	    	  catch(Exception e1)
	  	    	    	  {
	  	    	    	 e1.printStackTrace(); 
	  	    	    	  }
	  	    	    	 }
	    	    }
	    	};

	    	Handler h = new Handler();
	    	h.postDelayed(r, 10000);
	      
	      
	    	  
	      
	    
	    }
	    
	  }
}
