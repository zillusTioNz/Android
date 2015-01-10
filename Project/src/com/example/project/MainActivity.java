package com.example.project;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceActivity;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	//============================== Properties ===============================
	
	private MediaRecorder myRecorder;
	private MediaPlayer myPlayer;
	
	private String outputFile = null;
	
	private Button btnStart;
	private Button btnStop;
	private Button btnPlayList;
	private ListView listData;
	
	private TextView txtTime;
	private long startTime = 0L;
	private Handler customHandler =  new Handler();
	   
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	
	//============================== Method ===================================
	
	@Override
	protected void onCreate(Bundle saveInstanceState){
		super.onCreate(saveInstanceState);
		//addPreferencesFromResource(R.xml.preferences);
		setContentView(R.layout.activity_main);		
		txtTime = (TextView)findViewById(R.id.txtTime);
		
		// Start Button
		btnStart = (Button)findViewById(R.id.btnStart);
		btnStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startTime = SystemClock.uptimeMillis();
				customHandler.postDelayed(updateTimerThread, 0);
				
				startRecord(v);
			}
		});
		
		btnStop = (Button)findViewById(R.id.btnStop);
		btnStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				timeSwapBuff += timeInMilliseconds;
	  			customHandler.removeCallbacks(updateTimerThread);
	  			
				stopRecord(v);
			}
		});
		
		btnPlayList = (Button)findViewById(R.id.btnPlayList);
		btnPlayList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, PlayList.class);				
				startActivity(i);
			}
		});
		
		/*
		listData = (ListView) findViewById(R.id.listData);
		getDataFromSDCard();
		listData.setOnItemClickListener(new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
			
				String selectedFromList = (listData.getItemAtPosition(position).toString());
				
				if(myPlayer != null)
					myPlayer.stop();
				play(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+selectedFromList);
				
			
			
				Intent i = new Intent(MainActivity.this, PlayFile.class);
				i.putExtra("fileName", Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+selectedFromList);
				startActivity(i);
			
			}
		});
		*/
		
	}// end of onCreate
	
	private void setRecord(String name){
		outputFile = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+name+".3gpp";
				
		myRecorder = new MediaRecorder();
		myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	    myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	    myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
	    myRecorder.setOutputFile(outputFile);
	}
	
	public void startRecord(View v){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
		String currentDateandTime = sdf.format(new Date());
		setRecord(currentDateandTime.toString());
		
		Toast.makeText(getApplicationContext(), "Start recording...",
	    		  Toast.LENGTH_SHORT).show();
		
		try{
			myRecorder.prepare();
	        myRecorder.start();
		}catch (IllegalStateException e) {
	        // start:it is called before prepare()
	    	// prepare: it is called after start() or before setOutputFormat() 
	        e.printStackTrace();
	    } catch (IOException e) {
	        // prepare() fails
	        e.printStackTrace();
	    }
		btnStart.setEnabled(false);
		btnStop.setEnabled(true);
		
	}// end of start method
	
	public void stopRecord(View v){
		try {
		    myRecorder.stop();
		    myRecorder.release();
		    myRecorder  = null;
		      		      
		    /*Toast.makeText(getApplicationContext(), "Stop recording...",
		    		  Toast.LENGTH_SHORT).show();*/
		    //getDataFromSDCard();
		    
		    Intent i = new Intent(MainActivity.this, PlayFile.class);
			i.putExtra("fileName", outputFile);
			startActivity(i);
		} catch (IllegalStateException e) {
			//  it is called before start()
			e.printStackTrace();
		} catch (RuntimeException e) {
			// no valid audio/video data has been received
			e.printStackTrace();
		}
		btnStart.setEnabled(true);
		btnStop.setEnabled(false);
		
	}// end of stop method
 
	/*
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
	*/
	
	public void play(String fileName){
		
		try{			   
			   myPlayer = new MediaPlayer();
			   myPlayer.setDataSource(fileName);
			   myPlayer.prepare();
			   myPlayer.start();
			   
			   Toast.makeText(getApplicationContext(), "Start play the recording...", 
					   Toast.LENGTH_SHORT).show();
			   
		   } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		
		
	}
	
	private Runnable updateTimerThread = new Runnable() {
		   public void run() {
			   timeSwapBuff = 0L;
			   timeInMilliseconds = 0L;
			   timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
			   updatedTime = timeSwapBuff + timeInMilliseconds;
			   int secs = (int) (updatedTime / 1000);
			   int mins = secs / 60;
			   secs = secs % 60;
			   //int milliseconds = (int) (updatedTime % 1000);
			   txtTime.setText("" + String.format("%02d", mins) + ":"
					   + String.format("%02d", secs)); //+ ":"
					   //+ String.format("%03d", milliseconds));
			   customHandler.postDelayed(this, 0);
		   }
	};//end timer

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
	   	switch (item.getItemId()) {
	   	
	   	case R.id.action_settings:
	   		Intent goSetting = new Intent(getApplicationContext(),SettingActivity.class);
	   		try {
	 		   startActivity(goSetting);
	   		} catch (ActivityNotFoundException ex) {
	 		   ex.printStackTrace();
	   		}
	   		return true;
		   
	   	default:return super.onOptionsItemSelected(item);
	   }
   }
	
}// end Class

