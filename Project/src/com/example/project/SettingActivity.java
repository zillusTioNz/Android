package com.example.project;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class SettingActivity extends Activity implements OnClickListener{

	private CheckBox checkBox;
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		setTitle(R.string.setting);
		
		checkBox = (CheckBox)findViewById(R.id.chkBoxUpdate);
		button = (Button)findViewById(R.id.btnSave);
		button.setOnClickListener(this);
		loadSavedPreferences();

		
		/*SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("n" + sharedPrefs.getBoolean("perform_updates", false));
		builder.append("n" + sharedPrefs.getString("updates_interval", "-1"));
		builder.append("n" + sharedPrefs.getString("welcome_message", "NULL"));
		
		TextView settingsTextView = (TextView) findViewById(R.id.txtView);
		settingsTextView.setText(builder.toString());*/
	}

	private void loadSavedPreferences() {
		// TODO Auto-generated method stub
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean chkBoxValue = sharedPreferences.getBoolean("chkBoxValue", false);
		if (chkBoxValue) {
			checkBox.setChecked(true);
		}else {
			checkBox.setChecked(false);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		savePreferences("chkBoxValue", checkBox.isChecked());
		finish();
	}

	private void savePreferences(String key, boolean value) {
		// TODO Auto-generated method stub
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();

		
		/*btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});*/
	}
	
}
