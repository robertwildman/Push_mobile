package com.wildapps.push;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayMessage extends Activity{
	public static SharedPreferences sharedPreferences;
	public String Title, Message ,URL;
	public TextView TvTitle,TvMessage;
	public Button BURL;
	public Intent i;
	public InterstitialAd advert;
	public ArrayList<String> Messages;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages);
		//Making Shared Prefs
		sharedPreferences = this.getSharedPreferences("com.wildapps.push",
				Context.MODE_PRIVATE);
		//Action bar 
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setIcon(getResources().getDrawable(R.drawable.pushbanner));
		//This class will be dealing with the displaying to the user. 
		//I will get get the Message Title , Body and URL from intents
		//It will then display it to the class
		if(getIntent() != null)
		{
			Bundle extras = getIntent().getExtras();
			Title = extras.getString("Title");
			Message = extras.getString("Message");
			URL = extras.getString("URL");
		}
		//Will now set the message up and display
		//Also making sure that nothing is empty
		if(Title.length() > 0 && Message.length() > 0 && URL.length() > 0)
		{
			 //Dealing with the card library 
			 //Create a Card
		      Card card = new Card(this);

		      //Create a CardHeader
		      CardHeader header = new CardHeader(this);
		      
		      //Add Header to card
		      card.addCardHeader(header);
		      header.setTitle(Title);
		      card.setTitle(Message);
		      card.setOnClickListener(new Card.OnCardClickListener() {
				
				@Override
				public void onClick(Card arg0, View arg1) {
					displayurl(URL);
					
				}
			});
		      //Set card in the cardView
		      CardView cardView = (CardView)findViewById(R.id.carddemo);
		      cardView.setCard(card);
		}
		//Setting up the advert 
		AdView advert = new AdView(this);
		advert.setAdSize(AdSize.SMART_BANNER);
		advert.setAdUnitId("ca-app-pub-2049126681125303/4752608873");
		LinearLayout layout = (LinearLayout) findViewById(R.id.advert1);
		layout.addView(advert);
		
		AdRequest ad = new AdRequest.Builder().build();
		advert.loadAd(ad);
		
	}

	public void displayurl(String url)
	{
		final String url1 = url;
		//This topic will show advert then open the website 
		advert = new InterstitialAd(this);
		advert.setAdUnitId("ca-app-pub-2049126681125303/9962405275");
		advert.setAdListener(new AdListener()
		{
			
			@Override
			public void onAdClosed()
			{
				Intent i = new Intent(Intent.ACTION_VIEW);
		    	i.setData(Uri.parse(url1));
		    	startActivity(i);
			}
			public void onAdLoaded()
			{
				advert.show();
			}
		}
		);
		AdRequest adrequest = new AdRequest.Builder().build();
		advert.loadAd(adrequest);
		toast("Loading Website");
		
		
		
	}
	public void toast(String msg) {
		// A basic setup for a toast making it easy to display them in code
		Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG)
		.show();
	}
}
