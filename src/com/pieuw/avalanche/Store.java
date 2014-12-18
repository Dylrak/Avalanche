package com.pieuw.avalanche;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Store extends Activity{
	
	final Button five_euros = (Button) findViewById(R.id.five_euros);
	final Button ten_euros = (Button) findViewById(R.id.ten_euros);
	final Button fifteen_euros = (Button) findViewById(R.id.fifteen_euros);
	

	protected void oncreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	
	setContentView(R.layout.activity_store);
		}
	
	public void storesave(View view){
		
		try {
			FileOutputStream store_out = openFileOutput("/** Location to be set*/", MODE_PRIVATE);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		/** Outputstream to file where info about ingame credits can be stored for use */
	
	five_euros.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			
		}
	});
	
	ten_euros.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			
		}
	});
	
	fifteen_euros.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
	
			}
		});
	}
}