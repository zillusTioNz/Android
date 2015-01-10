package com.example.project;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SettingActivity extends Activity implements OnClickListener{

	private Spinner Spinner;
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		setTitle(R.string.setting);

		Spinner = (Spinner)findViewById(R.id.spinner_language);
		button = (Button)findViewById(R.id.btnSave);
		button.setOnClickListener(this);
		loadSavedPreferences();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void loadSavedPreferences() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		final String str_value = sharedPreferences.getString("Spinner", "English");
		List list_languages = new ArrayList();	
		list_languages.add("English");
		list_languages.add("Français");
		ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, list_languages);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner.setAdapter(adapter);
		if (str_value.contains("Français")) {
			Spinner.setSelection(1);
		}else {
			Spinner.setSelection(0);
		}
		Spinner.setOnItemSelectedListener(new OnItemSelectedListener() 
		{
			@Override
			public void onItemSelected(AdapterView adapter, View v, int postion, long lng) {
				if (adapter.getItemAtPosition(postion).toString().contains("Français")) {
					button.setText("Sauvegarder");
				}else {
					button.setText("Save");	
				}
			} 
			@Override     
			public void onNothingSelected(AdapterView<?> parentView) 
			{}
		}); 
	}

	@Override
	public void onClick(View v) {
		savePreferences("Spinner", Spinner.getSelectedItem().toString());
		finish();
	}

	private void savePreferences(String key, String value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();

		Resources res = getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		android.content.res.Configuration conf = res.getConfiguration();

		String lang;
		String country;
		if (value.contains("Français")) {
			lang = "fr";
			country = "FR";	
			button.setText("Sauvegarder");
		}
		else {
			lang = "en";
			country = "GB";		
			button.setText("Save");	
		}
		conf.locale = new Locale(lang, country);
		res.updateConfiguration(conf, dm);
	}

}
