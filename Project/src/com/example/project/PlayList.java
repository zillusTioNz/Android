package com.example.project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class PlayList extends Activity{
	private ListView listData;
	private MediaPlayer myPlayer;
	protected void onCreate(Bundle saveInstanceState){
		super.onCreate(saveInstanceState);
		setContentView(R.layout.activity_playlist);
		setTitle(R.string.playlist_title);
		
		listData = (ListView) findViewById(R.id.listData);
		getDataFromSDCard();
		listData.setOnItemClickListener(new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
			
				String selectedFromList = (listData.getItemAtPosition(position).toString());
				/*
				if(myPlayer != null)
					myPlayer.stop();
				play(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+selectedFromList);
				
			*/
			
				Intent i = new Intent(PlayList.this, PlayFile.class);
				i.putExtra("fileName", Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+selectedFromList);
				startActivity(i);
			
			}
		});
		
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent setIntent = new Intent(this, MainActivity.class);
		startActivity(setIntent);
	}

	public void getDataFromSDCard(){
		
		List<String> myArrayList = new ArrayList<String>();
		
		File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString());
		File[] filelist = dir.listFiles();
				
		for (int i = 0; i < filelist.length; i++) {
			if(filelist[i].getName().endsWith(".3gpp"))
				myArrayList.add(filelist[i].getName());
		}
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
				myArrayList );
		
		listData.setAdapter(arrayAdapter);
		
	}
}
