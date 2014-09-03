package com.wildapps.push;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class MainActivity extends Activity {

	String Endpoint;
	public static SharedPreferences sharedPreferences;
	public static EditText etsearch;
	public String stTopicname;
	public int stTopicend;
	public Boolean stRetry;
	public SubscribeResult subscribeResult;
	public AmazonSNS sns;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mytopics);
		sns = new AmazonSNSClient(new BasicAWSCredentials(
				"AKIAIB6HMHNDL5JNGATQ",
				"UEsQLbVTVjJTWxif/4Garpi7usnMnxeFWvUiNS6u"));
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
		// Setting up the layout
		ListView list = (ListView) findViewById(R.id.lvtopics);
		ArrayList<String> topics = getStringArrayPref("Topics");
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_checked, topics);
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Meaning user will be unsubscribing
            	ArrayList<String> topics = getStringArrayPref("Topics");
            	ArrayList<String> topicArn = getStringArrayPref("Topicsarn");
            	Log.d("Push", topicArn.get(position));
            	sns.unsubscribe(topicArn.get(position));
            	Toast.makeText(getApplicationContext(), "You have unsubcribed to " + topics.get(position), Toast.LENGTH_LONG).show();
            	topics.remove(position);
            	topicArn.remove(position);
    			setStringArrayPref("Topics", topics);
    			setStringArrayPref("Topicsarn", topicArn);
            	ListView list = (ListView) findViewById(R.id.lvtopics);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						getBaseContext(),
						android.R.layout.simple_list_item_checked,
						getStringArrayPref("Topics"));
				list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
				list.setAdapter(adapter);
				int i;
				for(i = 0; i < list.getCount();i++)
				{
					list.setItemChecked(i, true);
				}

                
            }

			
        });
        if (topics.size() < 1) {
			topics = new ArrayList<String>(1);
			topics.add("Please Subscribe to a topic");
			ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, topics);
			list.setAdapter(adapter1);
		}else
		{
		list.setAdapter(adapter);
		
		int i;
		for(i = 0; i < list.getCount();i++)
		{
			list.setItemChecked(i, true);
		}
		
		}
		

		// Setting up the EditText
		etsearch = (EditText) findViewById(R.id.etsearch);
		etsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					addtopic(etsearch.getText().toString(),
							getApplicationContext());
					ListView list = (ListView) findViewById(R.id.lvtopics);
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							getBaseContext(),
							android.R.layout.simple_list_item_checked,
							getStringArrayPref("Topics"));
					list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
					list.setAdapter(adapter);
					int i;
					for(i = 0; i < list.getCount();i++)
					{
						list.setItemChecked(i, true);
					}
					etsearch.setText("");
					return true;
				}
				return false;
			}

		});
		list.focusableViewAvailable(getCurrentFocus());
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

	public void subscribe_to_topic(String Topicname, int Topicend,
			Boolean retry, Context c) {
		stTopicname = Topicname;
		stTopicend = Topicend;
		stRetry = retry;

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

			Log.d("Push", e.toString());
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
					Toast.makeText(c.getApplicationContext(),
							"Topic not found try again", Toast.LENGTH_LONG)
							.show();
				}

			}
		}
	}

}
