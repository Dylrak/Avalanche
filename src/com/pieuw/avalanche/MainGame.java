package com.pieuw.avalanche;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class MainGame extends Activity implements SensorEventListener{

	private Paint paint = new Paint();
	private float x = 100;
	private float y = 200;
	private String turn = "null";
	private int radius = 50;
	private SensorManager sensorManager;
	Sensor accelerometer;
	Toast toast;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new GameView(this));
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
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
    	x -= event.values[0] * 2;
    	y += event.values[1] * 2;
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
	}
}
