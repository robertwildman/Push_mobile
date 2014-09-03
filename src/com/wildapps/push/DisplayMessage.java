package com.wildapps.push;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayMessage extends Activity{
	public String Title, Message ,URL;
	public TextView TvTitle,TvMessage;
	public Button BURL;
	public Intent i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages);
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
		//Finds the objects on the screen
		TvTitle = (TextView)findViewById(R.id.tvmessagetitle);
		TvMessage = (TextView)findViewById(R.id.tvmessagebody);
		BURL = (Button)findViewById(R.id.bwebsite);
		//Will now set the message up and display
		//Also making sure that nothing is empty
		if(Title.length() > 0 & Message.length() > 0 & URL.length() > 0)
		{
			TvTitle.setText(Title);
			TvMessage.setText(Message);
			BURL.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//Will send the url to the Advert to Web Class
					Intent i = new Intent(DisplayMessage.this,Adverttoweb.class);
				    i.putExtra("url",URL);
					
				}
			});
		}
		
	}
}
