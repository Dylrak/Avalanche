package com.pieuw.avalanche;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Canvas;
import android.graphics.Point;
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
	int position;
	int startPosition;
	int jumpMilliSeconds = 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		display = getWindowManager().getDefaultDisplay();
		display.getSize(size);
		height = size.y;
		width = size.x;
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
		timer.postDelayed(runnableCubeMove, 50);
		position = 0;
		startPosition = position;
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
 	   			timer.postDelayed(this, 10);
 	   }
    };
    
    private Runnable runnableCubeMove = new Runnable() {
	   		@Override
	   		public void run() {
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
    	int i = random.nextInt(5) + 1;
    	String randBlock = "block_" + i;
    	int resID = getResources().getIdentifier(randBlock, "drawable", getPackageName());
    	Bitmap block = BitmapFactory.decodeResource(getResources(), resID);
    	
    	int size = random.nextInt(300 - 100) + 100;
    	int positionX = random.nextInt(width - size - 5) + 5;
    	cubes.add(new Cube(positionX, position + height + size, size, size, block));
    }
    
    private void MoveCube() {
    	int index = 0;
    	for (Cube cube:cubes) {
    		int index1 = 0;
    		for (Cube collision:cubes) {
    			if (index != index1 && !cube.collisionCube) {
    				cube.intersect(collision);
    				if (cube.collisionCube) {
    					cube.cubeY = collision.cubeY + cube.cubeHeight;
    				}
        		}
    			index1++;
    		}
    		if (!cube.collisionCube && cube.cubeY > height / 3 + cube.cubeHeight) {
    			cube.cubeY -= 10;
    			if (cube.cubeY < height / 3 + cube.cubeHeight) {
    				cube.cubeY = height / 3 + cube.cubeHeight;
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
    	if (startJump >= jumpMilliSeconds / 10) {
    		startJump = 0;
    		jumping = false;
    		jumped = true;
    	}
    	else {
    		position += 2;
    	}
    }
    
    class GameView extends View {
    	Paint paintUser = new Paint();
    	int floorID = getResources().getIdentifier("floor", "drawable", getPackageName());
		Bitmap floor = BitmapFactory.decodeResource(getResources(), floorID);
		Bitmap floorScaled = Bitmap.createScaledBitmap(floor, width, height / 3, true);
		public GameView(Context context) {
			super(context);
			paintUser.setColor(Color.BLUE);
		}
		
		@Override
		protected void onDraw(Canvas c) {
			c.drawColor(Color.WHITE);
			for (Cube cube: cubes) {
				if (position <= height / 3) {
					c.drawBitmap(floorScaled, 0, height / 3 * 2 + position, null);
				}
				if (cube.cubeY - position >= -cube.cubeHeight && cube.cubeY - position <= height) {
					c.drawBitmap(cube.block, cube.cubeX, height - cube.cubeY + position, null);
				}
			}
            c.drawCircle(x, height / 3 * 2, radius, paintUser);
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
    	Bitmap block;
    	boolean collisionCube = false;
    	public Cube(int startX, int startY, int sizeX, int sizeY, Bitmap image) {
    		cubeWidth = sizeX;
    		cubeHeight = sizeY;
    		cubeX = startX;
    		cubeY = startY;
    		block = Bitmap.createScaledBitmap(image, cubeWidth, cubeHeight, true);
    	}
    	
    	public void intersect(Cube cube) {
    		if (cubeY - cubeHeight <= cube.cubeY && cubeY - cubeHeight >= cube.cubeY - cube.cubeHeight){
    			int xLeft = cubeX;
    			int xMiddle = cubeX + cubeWidth / 2;
    			int xRight = cubeX + cubeWidth;
    			for(int i = 0; i < cube.cubeWidth; i++){
        			if (xLeft == cube.cubeX + i || 
        					xMiddle == cube.cubeX + i || 
        					xRight == cube.cubeX + i) 
        			{
        				collisionCube = true;
        			}
        		}
			}
    	}
    }
}
