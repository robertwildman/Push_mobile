package com.wildapps.push;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class GcmMessageHandler extends IntentService {

     String mes;
     private Handler handler;
     public static SharedPreferences sharedPreferences;
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
       sharedPreferences = getSharedPreferences("com.wildapps.push",
				Context.MODE_PRIVATE);
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
       mes = extras.getString("default");
       //Comes in the format [Title],[Message],[URL],[Topicname];
       String[] context = mes.split(",");
       //This will display the message and send off to the class the Title Message and URL
       Intent i = new Intent(this,DisplayMessage.class);
       i.putExtra("Title",context[0]);
       i.putExtra("Message",context[1]);
       i.putExtra("URL",context[2]);
       //Will save it so it can show up on the frontpage. 
		ArrayList<String> Messages = getStringArrayPref("Messages");
		if (Messages.size() > 40)
		{
			Messages.remove(0);
			Messages.trimToSize();
		}
		Messages.add(context[0]+","+context[1]+","+context[2]);
		setStringArrayPref("Messages",Messages);
		
       PendingIntent pend = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
       NotificationManager notificationManager = (NotificationManager) 
    		   getSystemService(NOTIFICATION_SERVICE); 
       Notification n  = new Notification.Builder(this)
       .setSmallIcon(R.drawable.ic_launcher)
	   .setContentTitle(context[3])
	   .setContentText(context[0])
	   .setContentIntent(pend)
	   .setAutoCancel(true)
	   .setDefaults(Notification.DEFAULT_ALL).build();
       
       notificationManager.notify(0, n);
     
     

        GcmBroadcastReceiver.completeWakefulIntent(intent);

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