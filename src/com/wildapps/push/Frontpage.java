package com.wildapps.push;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardHeader.OnClickCardHeaderPopupMenuListener;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Frontpage extends Activity{
	public static SharedPreferences sharedPreferences;
	public String Title, Message ,URL;
	public TextView TvTitle,TvMessage;
	public ListView lvMessages;
	public Button BURL;
	public int cardid;
	public String[] Temp1;
	public Intent i;
	public ArrayList<String> TempMessages;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//This sets the view.
		setContentView(R.layout.frontpage);
		sharedPreferences = this.getSharedPreferences("com.wildapps.push",
				Context.MODE_PRIVATE);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setIcon(getResources().getDrawable(R.drawable.pushbanner));

		//Frontpage of the app This will show the recent pushes to the Phone 
		//If there as been none it will show a Welcome Message and also one telling them how they can sign up.
		//Will save it so it can show up on the frontpage. 
		try
		{
			TempMessages = getStringArrayPref("Messages");
		}
		catch (Exception ex) {
			TempMessages = new ArrayList<String>(1);
			Log.e("Push", ex.toString());
		}
		//Will find the size of the error and if it is less then 1 then it will add a message telling the user how to add topics. 
		if (TempMessages.size() < 1)
		{
			TempMessages.add("Welcome to Push Mobile"+","+"Sign up to topics using the Add Button on the top");
		}
		//Then starts the add to list view topic.
		addtolistview(TempMessages);

	}
	
	//This class deals with displaying the array to the listview. 
	public void addtolistview(final ArrayList<String> a)
	{
		
		List<Map<String,String>> data = new ArrayList<Map<String,String>>();
		ArrayList<Card> cards = new ArrayList<Card>();
		//This will spilt the array and add in to the listview 
		for(int i = 0; i < a.size();i++)
		{
			cardid = i;
			String Temp = a.get(i);
			Temp1 = Temp.split(",");
			//Dealing with the card library 
			//Create a Card
			Card card = new Card(this);
			//Create a CardHeader
			CardHeader header = new CardHeader(this);
			
			//Add Header to card
			card.addCardHeader(header);
			if(Temp1.length > 3)
			{
				header.setTitle(Temp1[0]+" - "+Temp1[3]);
			}else
			{
				header.setTitle(Temp1[0]);
			}
			//Add a popup menu. This method set OverFlow button to visibile
			header.setPopupMenu(R.menu.message, new OnClickCardHeaderPopupMenuListener(){
				@Override
				public void onMenuItemClick(BaseCard card, MenuItem item) {
					if(item.getTitle().toString().equalsIgnoreCase("View Website URL"))
					{
						int cardpos = cardid;
						String Temp = TempMessages.get(cardpos);
						String[] Temp1 = Temp.split(",");
						toast("URL: " + Temp1[2]);
					}else if(item.getTitle().toString().equalsIgnoreCase("View Topic"))
					{
						toast("View Topic");
						
					}else if (item.getTitle().toString().equalsIgnoreCase("Unsubscribe to Topic"))
					{
						toast("Unsubscribing to this topics");
					}else
					{
						toast("FAILED");
					}

				}
			});
			card.setTitle(Temp1[1]);
			cards.add(card);
		}
		Collections.reverse(cards);
		CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this,cards);
		CardListView listView = (CardListView)findViewById(R.id.CardList);
		if (listView!=null){
			listView.setAdapter(mCardArrayAdapter);
		}
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Collections.reverse(a);
				String arraytemp = a.get(position);
				String[] array = arraytemp.split(",");
				Intent i = new Intent(Frontpage.this,DisplayMessage.class);
				i.putExtra("Title",array[0]);
				i.putExtra("Message",array[1]);
				i.putExtra("URL",array[2]);
				startActivity(i);


			}


		});
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// handle item selection

		switch (item.getItemId()) {

		case R.id.action_add:
			//This will deal with the adding of topics into the sns with a dialog box to try and tidy up the topic view.
			return true;
		case R.id.action_about:
			//This will start a intent that will open the about me class
			return true;
		case R.id.action_view:
			//This will open a new tidy topic view to the user showing just the topics in a card style
			return true;
		default:

			return super.onOptionsItemSelected(item);

		}

	}

	public void toast(String msg) {
		// A basic setup for a toast making it easy to display them in code
		Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG)
		.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
}
