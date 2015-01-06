package com.example.project;


import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class SettingActivity extends Activity {

	private Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		btnBack = (Button)findViewById(R.id.button1);
		/*btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});*/
	}
	
}
