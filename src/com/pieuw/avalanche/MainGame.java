package com.pieuw.avalanche;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Canvas;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class MainGame extends Activity implements SensorEventListener{

	private Paint paint = new Paint();
	private float x, y, angle;
	private int startJump;
	private int radius = 50;
	private SensorManager sensorManager;
	Sensor accelerometer;
	Toast toast;
	Display display;
	Point size = new Point();
	int width, height;
	boolean jumping = false, jumped = false;
	Handler jumpTimer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new GameView(this));
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		display = getWindowManager().getDefaultDisplay();
		display.getSize(size);
		height = size.y;
		width = size.x;
		x = (width - radius) / 2;
		y = height - radius;
		jumpTimer = new Handler();
		startJump = 0;
		jumpTimer.postDelayed(runnableMove, 10);
	}
	
	protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
	
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	
    public void onSensorChanged(SensorEvent event) {
    	angle = event.values[0] * 2;
    }
    
    private Runnable runnableJump = new Runnable() {
    	   @Override
    	   public void run() {
    	      Jump();
    	      jumpTimer.postDelayed(this, 10);
    	      if (jumped) {
    	    	  jumpTimer.removeCallbacks(runnableJump);
    	      }
    	   }
    };
    
    private Runnable runnableMove = new Runnable() {
 	   		@Override
 	   		public void run() {
 	   		Move();
 	   		jumpTimer.postDelayed(this, 10);
 	   }
    };
    
 	private void Move() {
 		if (angle < -3) {
 			x += 1.5F;
 		} else if (angle > 3) {
 			x -= 1.5F;
 		}
 	}
 
    private void Jump() {
    	startJump++;
    	jumping = true;
    	jumped = false;
    	if (startJump > 60) {
    		startJump = 0;
    		jumping = false;
    		jumped = true;
    	}
    	else if (startJump > 30) {
    		y += 1.5F;
    	} 
    	else {
    		y -= 1.5F;
    	}
    }
    
    class GameView extends View {
	    
		public GameView(Context context) {
			super(context);
			paint.setColor(Color.BLUE);
		}
		
		@Override
		protected void onDraw(Canvas c) {
            c.drawCircle(x, y, radius, paint);
            invalidate();
        }
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    		if (!jumping) {
	        		jumpTimer.postDelayed(runnableJump, 10);
	    		}
	    	}
			return true;
		}
	}
}
