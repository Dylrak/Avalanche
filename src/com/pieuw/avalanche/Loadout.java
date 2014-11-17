package com.pieuw.avalanche;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Loadout extends Activity {
	String TAG = Loadout.class.getName(), FILENAME = "loadout.txt", inValues, concatOutValues;
	String[] outValues = new String [] {"0", "0"};
		/**1. Lava Immunity**/ 
		/**2. Block Immunity**/ 
		/**3. Double Jump**/
		/**4. Icarus Boots**/
	int i;
	File file = new File (FILENAME);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    
    	
		setContentView(R.layout.activity_loadout);
		
		inValues = readFromFile();
		
		/**Check whether or not file contains anything; if not, write default values to file.**/
		if (file.exists() && file.canRead()) {
			if (inValues.matches("^\\d")) {
				outValues = inValues.split("\n");
			}
		} else {
			for (i=0; i <= outValues.length; i++) {
				concatOutValues.concat(outValues[i] + "\n");
			}
			writeToFile(concatOutValues);
		}
		
        final Button lavaImmunity = (Button) findViewById(R.id.lava_immunity);
        lavaImmunity.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		
        	}
        
        });
        
        final Button startGame = (Button) findViewById(R.id.start_game);
        startGame.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		
        	}
        
        });
	}
	
	private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(FILENAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
        } 
    }
        private String readFromFile() {
            
            String ret = "";
             
            try {
                InputStream inputStream = openFileInput(FILENAME);
                 
                if ( inputStream != null ) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();
                     
                    while ( (receiveString = bufferedReader.readLine()) != null ) {
                        stringBuilder.append(receiveString);
                    }
                     
                    inputStream.close();
                    ret = stringBuilder.toString();
                }
            }
            catch (FileNotFoundException e) {
                Log.e(TAG, "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e(TAG, "Can not read file: " + e.toString());
            }
     
            return ret;
        }
}
