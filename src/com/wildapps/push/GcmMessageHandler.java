package com.wildapps.push;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

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
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
        Log.d("Push", String.valueOf(extras));
       mes = extras.getString("default");
       showToast();
       Intent i = new Intent(this,Adverttoweb.class);
       Log.d("gre", extras.getString("url"));
       i.putExtra("url",extras.getString("url"));
       PendingIntent pend = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
       NotificationManager notificationManager = (NotificationManager) 
    		   getSystemService(NOTIFICATION_SERVICE); 
       Notification n  = new Notification.Builder(this)
       .setSmallIcon(R.drawable.ic_launcher)
	   .setContentTitle(extras.getString("title"))
	   .setContentText(extras.getString("message"))
	   .setContentIntent(pend).build();
       
       notificationManager.notify(0, n);
     
       Log.i("GCM", "Received : (" +messageType+")  "+mes+extras.toString());

        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    public void showToast(){
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(),mes , Toast.LENGTH_LONG).show();
            }
         });

    }
}