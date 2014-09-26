package com.wildapps.push;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayMessage extends Activity{
	public static SharedPreferences sharedPreferences;
	public String Title, Message ,URL;
	public TextView TvTitle,TvMessage;
	public Button BURL;
	public Intent i;
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
		      //Set card in the cardView
		      CardView cardView = (CardView)findViewById(R.id.carddemo);
		      cardView.setCard(card);
		}
		
	
		
	}
	
	public static void setStringArrayPref(String key, ArrayList<String> values) {

		SharedPreferences.Editor editor = sharedPreferences.edit();
		JSONArray a = new JSONArray();
		for (int i = 0; i < values.size(); i++) {
			a.put(values.get(i));
		}
		if (!values.isEmpty()) {
			editor.putString(key, a.toString());
		} else {
			editor.putString(key, null);
		}
		editor.commit();
	}

	public static ArrayList<String> getStringArrayPref(String key) {
		String json = sharedPreferences.getString(key, null);
		ArrayList<String> urls = new ArrayList<String>();
		if (json != null) {
			try {
				JSONArray a = new JSONArray(json);
				for (int i = 0; i < a.length(); i++) {
					String url = a.optString(i);
					urls.add(url);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return urls;
	}
}
