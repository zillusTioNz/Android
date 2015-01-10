package com.example.project;
/*
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class PlayFile extends Activity {

	private MediaPlayer myPlayer;
	
	private Button btnPlay;
	private Button btnPuase;
	private Button btnStop;
	private String fileName;
	
	@Override
	protected void onCreate(Bundle saveInstanceState){
		super.onCreate(saveInstanceState);
		setContentView(R.layout.activity_playfile);
		
		Intent i = getIntent();
		fileName =  i.getStringExtra("fileName");
		
		try{
			myPlayer = new MediaPlayer();
			myPlayer.setDataSource(fileName);
			myPlayer.prepare();
			playFile(fileName);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		btnPlay = (Button)findViewById(R.id.btnPlay);
		btnPlay.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
				// TODO Auto-generated method stub
				playFile(fileName);
			}
		});
		
		btnPuase = (Button)findViewById(R.id.btnPuase);
		btnPuase.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				puasePlay();
			}
		});
		
		btnStop = (Button)findViewById(R.id.btnStop);
		btnStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopPlay();
			}
		});		
		
	}
	
	public void playFile(String fileName){
		
		try{	 
			   myPlayer.start();
			   
			   Toast.makeText(getApplicationContext(), "Start play the recording...", 
					   Toast.LENGTH_SHORT).show();
			   
		   } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		
	}
	
	public void puasePlay() {
		try {
			if (myPlayer != null) {
				myPlayer.pause();
		           
		           Toast.makeText(getApplicationContext(), "Stop playing the recording...", 
						   Toast.LENGTH_SHORT).show();
		       }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	
	public void stopPlay() {
		try {
			if (myPlayer != null) {
				myPlayer.stop();
		        myPlayer.release();
		        myPlayer = null;
		           
		           Toast.makeText(getApplicationContext(), "Stop playing the recording...", 
						   Toast.LENGTH_SHORT).show();
		       }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	
}
*/

import java.io.File;
import java.util.concurrent.TimeUnit;

import com.example.project.Dialog.dialogDoneListener;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore.Audio.Playlists;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class PlayFile extends Activity implements Dialog.dialogDoneListener{

	private MediaPlayer mediaPlayer;
	public TextView songName, duration;
	private double timeElapsed = 0, finalTime = 0;
	private int forwardTime = 2000, backwardTime = 2000;
	private Handler durationHandler = new Handler();
	private SeekBar seekbar;
	private String fileName;
	private ImageButton delBtn;
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		try {
			mediaPlayer.stop();
		} catch (Exception e) {
			// TODO: handle exception
		}
		Intent setIntent = new Intent(this, PlayList.class);
		startActivity(setIntent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//set the layout of the Activity
		setContentView(R.layout.activity_playfile);
		setTitle(R.string.playtitle);
		Intent i = getIntent();
		fileName =  i.getStringExtra("fileName");
		
		delBtn = (ImageButton)findViewById(R.id.delete);
		delBtn.setEnabled(false);
		
		//initialize views
		initializeViews();
		
		timeElapsed = mediaPlayer.getCurrentPosition();
		seekbar.setProgress((int) timeElapsed);
		durationHandler.postDelayed(updateSeekBarTime, 100);
		
		seekbar = (SeekBar)findViewById(R.id.seekBar);
		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

	        @Override
	        public void onStopTrackingTouch(SeekBar seekBar) {

	        }

	        @Override
	        public void onStartTrackingTouch(SeekBar seekBar) {

	        }

	            @Override
	            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {              
	                if(mediaPlayer != null && fromUser){	                	
	                	mediaPlayer.seekTo(progress);
	                }
	            }
	    });
		
		mediaPlayer.start();
		
	}//end onCreate
	
	public void initializeViews(){
		songName = (TextView) findViewById(R.id.songName);
		mediaPlayer = new MediaPlayer();
		try{
			mediaPlayer.setDataSource(fileName);
			mediaPlayer.prepare();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		finalTime = mediaPlayer.getDuration();
		duration = (TextView) findViewById(R.id.songDuration);
		seekbar = (SeekBar) findViewById(R.id.seekBar);
		songName.setText(fileName);
		
		seekbar.setMax((int) finalTime);
		seekbar.setClickable(false);
		
	}

	// play mp3 song
	public void play(View view) {
		mediaPlayer.start();
		timeElapsed = mediaPlayer.getCurrentPosition();
		seekbar.setProgress((int) timeElapsed);
		durationHandler.postDelayed(updateSeekBarTime, 100);
		
		delBtn = (ImageButton)findViewById(R.id.delete);
		delBtn.setEnabled(false);
	}

	//handler to change seekBarTime
	private Runnable updateSeekBarTime = new Runnable() {
		public void run() {
			//get current position
			timeElapsed = mediaPlayer.getCurrentPosition();
			//set seekbar progress
			seekbar.setProgress((int) timeElapsed);
			//set time remaing
			double timeRemaining = finalTime - timeElapsed;
			duration.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
			
			//repeat yourself that again in 100 miliseconds
			durationHandler.postDelayed(this, 100);
			
			if (timeRemaining==0) {
				delBtn = (ImageButton)findViewById(R.id.delete);
				delBtn.setEnabled(true);
			}
		}
	};

	// pause mp3 song
	public void pause(View view) {
		mediaPlayer.pause();
	}

	// go forward at forwardTime seconds
	public void forward(View view) {
		try{
			//check if we can go forward at forwardTime seconds before song endes
			if ((timeElapsed + forwardTime) <= finalTime) {
				timeElapsed = timeElapsed + forwardTime;

				//seek to the exact second of the track
				mediaPlayer.seekTo((int) timeElapsed);
			}
		}catch(Exception e){
			
		}
	}
	
	// go forward at rewind seconds
		public void rewind(View view) {
			try{
				//check if we can go forward at forwardTime seconds before song endes
				if ((timeElapsed - backwardTime) >= 0) {
					timeElapsed = timeElapsed - backwardTime;

					//seek to the exact second of the track
					mediaPlayer.seekTo((int) timeElapsed);
				}
			}catch(Exception e){
				
			}
		}
	
	public void deleteFileRecord(View view){
		//DialogFragment dialog = new DialogFragment();
		//dialog.show(getFragmentManager(), "Dialog");
		
		Dialog.newInstance().show(getFragmentManager(), null);
	}

	public void onDone(){
		//Toast.makeText(getApplicationContext(), "DONE...", Toast.LENGTH_SHORT).show();
		try {
			File file = new File(fileName);
			if (file.delete()) {
				Toast.makeText(getApplicationContext(), "Delete file complete...", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(getApplicationContext(), PlayList.class);
				startActivity(i);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void stopPlay(View view){
		
		try {
			mediaPlayer.stop();
			mediaPlayer = new MediaPlayer();
			try {
				mediaPlayer.setDataSource(fileName);
				mediaPlayer.prepare();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			seekbar.setProgress(0);
			//Toast.makeText(getApplicationContext(), "Stop playing the recording...", Toast.LENGTH_SHORT).show();
			
			delBtn = (ImageButton)findViewById(R.id.delete);
			delBtn.setEnabled(true);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
