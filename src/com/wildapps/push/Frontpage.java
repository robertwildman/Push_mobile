package com.wildapps.push;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardHeader.OnClickCardHeaderPopupMenuListener;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Frontpage extends Activity{
	public static SharedPreferences sharedPreferences;
	public String Title, Message ,URL,Endpoint,stTopicname;
	public TextView TvTitle,TvMessage;
	public ListView lvMessages;
	public Button BURL;
	public int cardid,stTopicend;
	public String[] Temp1;
	public Intent i;
	public Boolean stRetry;
	public SubscribeResult subscribeResult;
	public AmazonSNS sns;
	public final Context context = this;
	public ArrayList<String> TempMessages;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Set up sns 
		sns = new AmazonSNSClient(new BasicAWSCredentials(
				"AKIAIB6HMHNDL5JNGATQ",
				"UEsQLbVTVjJTWxif/4Garpi7usnMnxeFWvUiNS6u"));
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
		// Will see if the device has been registered if it hasn't it with start
		// up the getregid function
		if (sharedPreferences.getBoolean("Registered", false) == false) {
			getRegId();
		}
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
				int cardpos = cardid;
				@Override
				public void onMenuItemClick(BaseCard card, MenuItem item) {
					if(item.getTitle().toString().equalsIgnoreCase("View Website URL"))
					{
						
						String Temp = TempMessages.get(cardpos);
						String[] Temp1 = Temp.split(",");
						//This class will display a small dialog with twitter and email if they have an issue. 
						AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(context);
						dialogbuilder.setTitle("URL")
						.setMessage(Temp1[2])
						.setCancelable(true);
						AlertDialog dialog = dialogbuilder.create();
						dialog.show();
					
					}else if(item.getTitle().toString().equalsIgnoreCase("View Topic"))
					{
						;
						//This class will display a small dialog with twitter and email if they have an issue. 
						AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(context);
						dialogbuilder.setTitle("Topic")
						.setMessage(Temp1[3])
						.setCancelable(true);
						AlertDialog dialog = dialogbuilder.create();
						dialog.show();
					
						
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
			card.setOnClickListener(new Card.OnCardClickListener() {
				int cardpos = cardid;
				
	            @Override
	            public void onClick(Card card, View view) {
	            	String Temp = TempMessages.get(cardpos);
					String[] Temp1 = Temp.split(",");
					String url = Temp1[2];
	            	Intent i = new Intent(Intent.ACTION_VIEW);
	            	i.setData(Uri.parse(url));
	            	startActivity(i);
	            }
	        });
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
			addtopicdialog();
			return true;
		case R.id.action_about:
			//This will start a dialog that will open the about me class
			aboutus();
			return true;
		case R.id.action_view:
			//This will open a new tidy topic view to the user showing just the topics in a card style
			return true;
		default:

			return super.onOptionsItemSelected(item);

		}

	}
	
	public void aboutus()
	{
		//This class will display a small dialog with twitter and email if they have an issue. 
		AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
		dialogbuilder.setTitle("About us")
		.setMessage("You can find us on twitter @Robertwildman or Email us: wildappsuk@gmail.com")
		.setCancelable(true);
		AlertDialog dialog = dialogbuilder.create();
		dialog.show();
	}

	public void addtopicdialog()
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Add new Topic");
		alert.setMessage("Enter Name of Topic you wish to Subscribe to:");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setHint("Topic Name");
		alert.setView(input);

		alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  String value = input.getText().toString();
		  addtopic(value,context);
		  toast("Adding Topic");
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		   dialog.cancel();
		  }
		});

		alert.show();
		
		
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
	public void subscribe_to_topic(String Topicname, int Topicend,
			Boolean retry, Context c) {
		stTopicname = Topicname;
		stTopicend = Topicend;
		stRetry = retry;

		if(Topicname.length() > 0)
		{
		try {
			String Endpoint = sharedPreferences.getString("Endpoint", null);
			String Topicfullname = "arn:aws:sns:us-east-1:072893446206:"
					+ Topicname + "_" + Topicend;
			SubscribeRequest subscribeRequest = new SubscribeRequest(
					Topicfullname, "application", Endpoint);
			subscribeResult = sns.subscribe(subscribeRequest);
			ArrayList<String> topicArn = getStringArrayPref("Topicsarn");
			Log.d("Push", subscribeResult.getSubscriptionArn());
			topicArn.add(subscribeResult.getSubscriptionArn());
			Toast.makeText(c.getApplicationContext(),
					"You have been subscribe to: " + Topicname,
					Toast.LENGTH_LONG).show();
			ArrayList<String> topics = getStringArrayPref("Topics");
			topics.add(Topicname);
			
			setStringArrayPref("Topics", topics);
			setStringArrayPref("Topicsarn", topicArn);

		} catch (Exception e) {

			Log.d("Push-Error", e.toString());
			if (e.toString().contains("403")) {
				// If the topic is full
				// Will see if there is a new one.

				subscribe_to_topic(stTopicname, stTopicend + 1, true, c);
			} else if (e.toString().contains("404")) {
				// Topic does not exist
				if (retry == true) {
					// Will make a part 2 of topic
					String newtopicname = stTopicname + "_" + stTopicend;
					sns.createTopic(newtopicname);
					subscribe_to_topic(stTopicname, stTopicend, true, c);

				} else {
					// No Topic found
					toast("Topic not found try again");
				}

			}
			else
			{
				toast("Topic not found! Try Again!!");
			}
		}
		}
		else
		{
			toast("Please Enter a topic name");
		}
	}
	public void getRegId() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {

				sns.setEndpoint("https://sns.us-east-1.amazonaws.com");
				String msg = "";
				try {
					GoogleCloudMessaging gcm = GoogleCloudMessaging
							.getInstance(getApplicationContext());
					String regid = gcm.register("781661055860");
					CreatePlatformEndpointRequest createEndpointRequest = new CreatePlatformEndpointRequest();
					createEndpointRequest
							.setPlatformApplicationArn("arn:aws:sns:us-east-1:072893446206:app/GCM/Push");
					createEndpointRequest.setToken(regid);
					CreatePlatformEndpointResult endpointResult = sns
							.createPlatformEndpoint(createEndpointRequest);
					Endpoint = endpointResult.getEndpointArn();
					Log.d("GCM", Endpoint);
					sharedPreferences.edit().putString("Endpoint", Endpoint)
							.commit();
					sharedPreferences.edit().putBoolean("Registered", true)
							.commit();
					Log.i("GCM", msg);

				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();

				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {

			}
		}.execute(null, null, null);
	}
	public void addtopic(String topicname, Context c) {
		// In this topic we will add the user up to the sns server and then
		// it will save the name to the end of the file called Topicnames
		// This means that the user will be able to see the topics subscribe
		// to and also get the notfications.



		// Will now add to the sns topic.This requires reading the endpoint
		// from file.
		subscribe_to_topic(topicname, 1, false, c);
	}
}
