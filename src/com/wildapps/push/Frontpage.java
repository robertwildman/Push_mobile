package com.wildapps.push;

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
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Frontpage extends Activity{
	public static SharedPreferences sharedPreferences;
	public String Title, Message ,URL;
	public TextView TvTitle,TvMessage;
	public ListView lvMessages;
	public Button BURL;
	public Intent i;
	public ArrayList<String> TempMessages;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		lvMessages = (ListView)findViewById(R.id.lvMessages);
		//Will save it so it can show up on the frontpage. 
		try
		{
		TempMessages = getStringArrayPref("Messages");
		}
		catch (Exception ex) {
		TempMessages = new ArrayList<String>(1);
		Log.e("Push", ex.toString());
		}
		if (TempMessages.size() < 1)
		{
			TempMessages.add("Welcome to Push Mobile"+","+"Sign up to topics using the Add Button on the top");
		}
		addtolistview(TempMessages);
		
	}
	
	public void addtolistview(ArrayList<String> a)
	{
		List<Map<String,String>> data = new ArrayList<Map<String,String>>();
		
		//This will spilt the array and add in to the listview 
		String[] names = new String[2];
		names[0] = "First Line";
		names[1] = "Second Line";
		for(int i = 0; i < a.size();i++)
		{
			Map<String,String> datum = new HashMap<String,String>();
			String Temp = a.get(i);
			String[] Temp1 = Temp.split(",");
			datum.put("First Line", Temp1[0]);
			datum.put("Second Line", Temp1[1]);
			data.add(datum);
		}
		
		Log.e("Push", data.toString());
		Log.e("Push", names.toString());
		Collections.reverse(data);
		SimpleAdapter adapter = new SimpleAdapter(Frontpage.this,data,android.R.layout.two_line_list_item,names,new int[] {android.R.id.text1,android.R.id.text2});
		lvMessages = (ListView)findViewById(R.id.lvMessages);
		lvMessages.setAdapter(adapter);
		lvMessages.refreshDrawableState();
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
