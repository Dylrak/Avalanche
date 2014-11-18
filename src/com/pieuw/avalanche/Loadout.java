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
	int[] outValues = new int [] {0, 0};
		/**1. Lava Immunity**/ 
		/**2. Block Immunity**/ 
		/**3. Double Jump**/
		/**4. Icarus Boots**/
	int i, current_passive;
	File file = new File (FILENAME);
	
	final Button passive_1 = (Button) findViewById(R.id.passive_1);
	final Button passive_2 = (Button) findViewById(R.id.passive_2);
	final Button lavaImmunity = (Button) findViewById(R.id.lava_immunity);
	final Button blockImmunity = (Button) findViewById(R.id.block_immunity);
	final Button doubleJump = (Button) findViewById(R.id.double_jump);
	final Button icarusBoots = (Button) findViewById(R.id.icarus_boots);
	
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
				String[] tempArray = inValues.split("\n");
				i = 0;
				for (String str: tempArray) {
					outValues[i++] = Integer.parseInt (str);
				}
				
			}
		} else {
			printToFile();
		}
		
		refreshPassives ();
		
        lavaImmunity.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if (outValues[0] != 1) {
        			passive_1.setEnabled (true);
        		}
        		if (outValues[1] != 1) {
        			passive_2.setEnabled (true);
        		}
        	}
        
        });
        
        blockImmunity.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if (outValues[0] != 2) {
        			passive_1.setEnabled (true);
        		}
        		if (outValues[1] != 2) {
        			passive_2.setEnabled (true);
        		}
        	}
        
        });
        
        doubleJump.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if (outValues[0] != 3) {
        			passive_1.setEnabled (true);
        		}
        		if (outValues[1] != 3) {
        			passive_2.setEnabled (true);
        		}
        	}
        
        });
        
        icarusBoots.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if (outValues[0] != 4) {
        			passive_1.setEnabled (true);
        		}
        		if (outValues[1] != 4) {
        			passive_2.setEnabled (true);
        		}
        	}
        
        });
        passive_1.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		outValues[0] = current_passive;
        		printToFile();
        		refreshPassives ();
        	}
        
        });
        
        passive_2.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		outValues[1] = current_passive;
        		printToFile();
        		refreshPassives ();
        	}
        
        });
	}
	
	public void refreshPassives () {
		passive_1.setEnabled(false);
		passive_2.setEnabled(false);
		if (outValues[0] == 1) {
			passive_1.setBackgroundResource(R.drawable.lava_immunity);
		} else if (outValues[0] == 2) {
			passive_1.setBackgroundResource(R.drawable.block_immunity);
		} else if (outValues[0] == 3) {
			passive_1.setBackgroundResource(R.drawable.double_jump);
		} else if (outValues[0] == 4) {
			passive_1.setBackgroundResource(R.drawable.icarus_boots);
		}
		if (outValues[1] == 1) {
			passive_2.setBackgroundResource(R.drawable.lava_immunity);
		} else if (outValues[1] == 2) {
			passive_2.setBackgroundResource(R.drawable.block_immunity);
		} else if (outValues[1] == 3) {
			passive_2.setBackgroundResource(R.drawable.double_jump);
		} else if (outValues[1] == 4) {
			passive_2.setBackgroundResource(R.drawable.icarus_boots);
		}
	}
	
	private void printToFile() {
		for (i=0; i <= outValues.length; i++) {
			concatOutValues.concat(outValues[i] + "\n");
		}
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(FILENAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(concatOutValues);
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
