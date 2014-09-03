package com.wildapps.push;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class GcmMessageHandler extends IntentService {

     String mes;
     private Handler handler;
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

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.;
       mes = extras.getString("default");
       //Comes in the format [Title],[Message],[URL],[Topicname];
       String[] context = mes.split(",");
       //This will display the message and send off to the class the Title Message and URL
       Intent i = new Intent(this,DisplayMessage.class);
       i.putExtra("Title",context[0]);
       i.putExtra("Message",context[1]);
       i.putExtra("URL",context[2]);
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

}