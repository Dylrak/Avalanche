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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class MainGame extends Activity implements SensorEventListener{

	
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
 		if (angle < -1.5 || angle > 1.5) {
 			float speed = angle;
 			if (angle > 8) {
 				speed = 8;
 			}
 			x -= speed;
 			if (x <= 0 - radius / 2) {
 				x = width - radius / 2;
 			}
 			else if (x >= width - radius / 2) {
 				x = 0 - radius / 2;
 			}
 		}
 	}
 
    private void Jump() {
    	startJump++;
    	jumping = true;
    	jumped = false;
    	if (startJump >= 200) {
    		startJump = 0;
    		jumping = false;
    		jumped = true;
    	}
    	else {
    		y -= -0.2372 * startJump + 23.72;
    	}
    }
    
    class GameView extends View {
    	private Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
    	private Rect endSize = new Rect();
    	private Paint paint = new Paint();
		public GameView(Context context) {
			super(context);
			paint.setColor(Color.BLUE);
		}
		
		@Override
		protected void onDraw(Canvas c) {
			endSize.set(0, (int) (height - y), width, (int) (height + y));
            c.drawBitmap(background, null, endSize, null);
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
