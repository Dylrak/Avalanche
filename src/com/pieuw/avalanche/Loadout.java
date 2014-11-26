package com.pieuw.avalanche;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Loadout extends Activity {
	public static String FILENAME = "preferences";
	int passive1, passive2, inValues, i, current_passive;
	SharedPreferences preferences;
	Button passive_1, passive_2, lavaImmunity, blockImmunity, doubleJump, icarusBoots;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	
		setContentView(R.layout.activity_loadout);

		passive_1 = (Button) findViewById(R.id.passive_1);
		passive_2 = (Button) findViewById(R.id.passive_2);
		lavaImmunity = (Button) findViewById(R.id.lava_immunity);
		blockImmunity = (Button) findViewById(R.id.block_immunity);
		doubleJump = (Button) findViewById(R.id.double_jump);
		icarusBoots = (Button) findViewById(R.id.icarus_boots);
		
		preferences = getSharedPreferences(FILENAME, 0);
		SharedPreferences.Editor editor = preferences.edit();
		passive1 = preferences.getInt("passive_1", 0);
		passive2 = preferences.getInt("passive_2", 0);
		editor.commit();
		
		refreshPassives ();
		
		int xy = lavaImmunity.getWidth();
		lavaImmunity.setHeight(xy);
		blockImmunity.setHeight(xy);
		doubleJump.setHeight(xy);
		icarusBoots.setHeight(xy);
		
		/**1. Lava Immunity**/ 
		/**2. Block Immunity**/ 
		/**3. Double Jump**/
		/**4. Icarus Boots**/
		
        lavaImmunity.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if (passive1 != 1) {
        			passive_1.setEnabled (true);
        		}
        		if (passive2 != 1) {
        			passive_2.setEnabled (true);
        		}
        		current_passive = 1;
        	}
        
        });
        
        blockImmunity.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if (passive1 != 2) {
        			passive_1.setEnabled (true);
        		}
        		if (passive2 != 2) {
        			passive_2.setEnabled (true);
        		}
        		current_passive = 2;
        	}
        
        });
        
        doubleJump.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if (passive1 != 3) {
        			passive_1.setEnabled (true);
        		}
        		if (passive2 != 3) {
        			passive_2.setEnabled (true);
        		}
        		current_passive = 3;
        	}
        
        });
        
        icarusBoots.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if (passive1 != 4) {
        			passive_1.setEnabled (true);
        		}
        		if (passive2 != 4) {
        			passive_2.setEnabled (true);
        		}
        		current_passive = 4;
        	}
        
        });
        passive_1.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		passive1 = current_passive;
        		refreshPassives ();
        	}
        
        });
        
        passive_2.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		passive2 = current_passive;
        		refreshPassives ();
        	}
        
        });
	}
	
	public void refreshPassives () {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("passive_1", passive1);
		editor.putInt("passive_2", passive2);
		editor.commit();
		passive_1.setEnabled(false);
		passive_2.setEnabled(false);
		if (passive1 == 1) {
			passive_1.setBackgroundResource(R.drawable.lava_immunity);
		} else if (passive1 == 2) {
			passive_1.setBackgroundResource(R.drawable.block_immunity);
		} else if (passive1 == 3) {
			passive_1.setBackgroundResource(R.drawable.double_jump);
		} else if (passive1 == 4) {
			passive_1.setBackgroundResource(R.drawable.icarus_boots);
		} else {
			passive_1.setBackgroundResource(R.drawable.standard);
		}
		if (passive2 == 1) {
			passive_2.setBackgroundResource(R.drawable.lava_immunity);
		} else if (passive2 == 2) {
			passive_2.setBackgroundResource(R.drawable.block_immunity);
		} else if (passive2 == 3) {
			passive_2.setBackgroundResource(R.drawable.double_jump);
		} else if (passive2 == 4) {
			passive_2.setBackgroundResource(R.drawable.icarus_boots);
		} else {
			passive_2.setBackgroundResource(R.drawable.standard);
		}
	}
}
