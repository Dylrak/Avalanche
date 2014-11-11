package com.pieuw.avalanche;

import java.util.ArrayList;
import java.util.Random;
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
	Handler timer;
	ArrayList<Cube> cubes = new ArrayList<Cube>();
	Random random = new Random();
	
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
		timer = new Handler();
		startJump = 0;
		timer.postDelayed(runnableMove, 10);
		timer.postDelayed(runnableSpawnCube, 2000);
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
    	      timer.postDelayed(this, 10);
    	      if (jumped) {
    	    	  timer.removeCallbacks(runnableJump);
    	      }
    	   }
    };
    
    private Runnable runnableMove = new Runnable() {
 	   		@Override
 	   		public void run() {
 	   			Move();
 	   			MoveCube();
 	   			timer.postDelayed(this, 10);
 	   }
    };
    
    private Runnable runnableSpawnCube = new Runnable() {
	   		@Override
	   		public void run() {
	   			SpawnCube();
	   			timer.postDelayed(this, 5000);
	   }
    };
    
    private void SpawnCube() {
    	int i = random.nextInt(10) + 1;
    	String randBlock = "block_" + i;
    	int resID = getResources().getIdentifier(randBlock, "drawable", getPackageName());
    	Bitmap block = BitmapFactory.decodeResource(getResources(), resID);
    	
    	int size = random.nextInt(100 - 80) + 80;
    	int position = random.nextInt(width - size - 5) + 5;
    	cubes.add(new Cube(position, size, size, size));
    }
    
    private void MoveCube() {
    	int index = 0;
    	for (Cube cube:cubes) {
    		boolean collisionCube = false;
    		int index1 = 0;
    		for (Cube collision:cubes) {
    			if (cube.intersect(collision) && (index != index1)) {
    				collisionCube = true;
    			}
    			index1++;
    		}
    		if (!collisionCube && cube.cubeY < height - cube.cubeWidth) {
    			cube.cubeY += 10;
    			if (cube.cubeY > height - cube.cubeHeight) {
    				cube.cubeY = height - cube.cubeHeight;
    			}
    		}
    		index++;
    	}
    }
    
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
    	Paint paintUser = new Paint();
    	Paint paintCube = new Paint();
		public GameView(Context context) {
			super(context);
			paintUser.setColor(Color.BLUE);
			paintCube.setColor(Color.RED);
		}
		
		@Override
		protected void onDraw(Canvas c) {
			for (Cube cube: cubes) {
				/*Can't parse "block" from SpawnCube to here. Please fix.*/
				c.drawBitmap(block, cube.cubeX, cube.cubeY, paintCube);
			}
            c.drawCircle(x, y, radius, paintUser);
            
            invalidate();
        }
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    		if (!jumping) {
	        		timer.postDelayed(runnableJump, 10);
	    		}
	    	}
			return true;
		}
	}
    
    public class Cube {
    	int cubeX = 0, cubeY = 0, cubeWidth = 0, cubeHeight = 0;
    	public Cube(int startX, int startY, int sizeX, int sizeY) {
    		cubeWidth = sizeX;
    		cubeHeight = sizeY;
    		cubeX = startX;
    		cubeY = startY;
    	}
    	
    	public boolean intersect(Cube cube) {
    		boolean intersected = false;
    		if (cubeY + cubeHeight * 2 >= cube.cubeY) {
    			int xLeft = cubeX;
    			int xMiddle = cubeX + cubeWidth;
    			int xRight = cubeX + cubeWidth * 2;
    			for(int i = 0; i < cube.cubeWidth * 2; i++){
        			if (xLeft == cube.cubeX + i || 
        					xMiddle == cube.cubeX + i || 
        					xRight == cube.cubeX + i) 
        			{
        				intersected = true;
        			}
        		}
			}
			return intersected;
    	}
    }
}
