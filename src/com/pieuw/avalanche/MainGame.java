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
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

//sensorevent = scherm kantelen
public class MainGame extends Activity implements SensorEventListener{
	private float x, angle;
	private int startJump;
	private SensorManager sensorManager;
	Sensor accelerometer;
	Toast toast;
	Display display;
	Point size = new Point();
	int width, height;
	boolean jumping = false, jumped = true;
	Handler timer;
	ArrayList<Cube> cubes = new ArrayList<Cube>();
	Random random = new Random();
	int position;
	int jumpMilliSeconds;
	int userWidth, userHeight;
	float userSpeed = 0, cubeSpeed = 0;
	boolean dead = false;
	
	public native void Engine();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		display = getWindowManager().getDefaultDisplay();
		display.getSize(size);
		height = size.y;
		width = size.x;
		userWidth = 100;
		userHeight = 78;
		super.onCreate(savedInstanceState);
		//maakt nieuwe view aan
		setContentView(new GameView(this));
		//sensor voor als je het scherm beweegt
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		//sensor voor de x hoek van de mobiel
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		display = getWindowManager().getDefaultDisplay();
		display.getSize(size);
		//hoogte van het scherm
		height = size.y;
		//breedte van het scherm
		width = size.x;
		//zet user in het midden
		x = (width - userWidth) / 2;
		timer = new Handler();
		//integer om het springen niet unlimited door te laten gaan
		startJump = 0;
		//hoe lang je omhoog springt
		jumpMilliSeconds = 500;
		//start timer om te bewegen
		timer.postDelayed(runnableMove, 10);
		//timer om cubes te spawnen
		timer.postDelayed(runnableSpawnCube, 2000);
		//timer om de cubes te bewegen zolang er geen collision is
		timer.postDelayed(runnableCubeMove, 10);
		//normale y waarde van de onderkant van het scherm is (onderkant y => 0)
		position = 0;
		userSpeed = height * 0.02F;
		cubeSpeed = userSpeed / 2;

	}
	
	//wanneer het scherm bewogen wordt
	protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
	
	//wanneer het scherm niet bewogen wordt
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
	
    //doet niks
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	
	//berekent onder welke x hoek het scherm zit
    public void onSensorChanged(SensorEvent event) {
    	angle = event.values[0] * 2;
    }
    
    
    private Runnable runnableJump = new Runnable() {
    	   @Override
    	   public void run() {
    		   if (!dead) {
    			  Jump();
         	      if (!jumping) {
         	    	  timer.removeCallbacks(runnableJump);
         	      } else {
         	    	 timer.postDelayed(this, 10);
         	      }
    		  }
    		   else {
    			   jumped = false;
    			   jumping = true;
    			   timer.removeCallbacks(runnableJump);
    		   }
    	   }
    };
    
    private Runnable runnableMove = new Runnable() {
 	   		@Override
 	   		public void run() {
 	   			//checkt voor top en bot collision tegelijk aka dead
 	   			Dead();
 	   			if (!dead) {
 	   				//beweegt x van user
 	   				Move();
 	   				//zo lang er geen collision en niet gesprongen wordt valt de user
 	   				Fall();
 	   				//doet dit elke 10ms
 	   				timer.postDelayed(this, 10);
 	   			}
 	   			else {
 	   				//als user dead is dan verwijder beweeg timer
 	   				timer.removeCallbacks(runnableMove);
 	   			}
 	   }
    };
    
    private Runnable runnableCubeMove = new Runnable() {
	   		@Override
	   		public void run() {
	   			//beweegt cube en checkt voor collision
	   			MoveCube();
	   			//doet dit elke 10ms
	   			timer.postDelayed(this, 10);
	   }
    };
    
    private Runnable runnableSpawnCube = new Runnable() {
	   		@Override
	   		public void run() {
	   			//spawnt cube
	   			SpawnCube();
	   			//doet dit elke 2 secondes
	   			timer.postDelayed(this, 2000);
	   }
    };
    
    private void SpawnCube() {
    	int i = random.nextInt(5) + 1;
    	String randBlock = "block_" + i;
    	int resID = getResources().getIdentifier(randBlock, "drawable", getPackageName());
    	Bitmap block = BitmapFactory.decodeResource(getResources(), resID);
    	
    	int size = (int) ((random.nextInt(1) + 1) * (10 * userSpeed));
    	int positionX = random.nextInt(width - size - 5) + 5;
    	cubes.add(new Cube(positionX, position + height + size, size, size, block));
    }
    
    private void MoveCube() {
    	int index = 0;
    	for (Cube cube:cubes) {
    		int index1 = 0;
    		for (Cube collision:cubes) {
    			//als cube niet zichzelf is en geen collision heeft
    			if (index != index1 && !cube.collisionCube) {
    				//kijk voor collision met andere cubes
    				cube.intersect(collision);
    				if (cube.collisionCube) {
    					cube.cubeY = collision.cubeY + cube.cubeHeight;
    				}
        		}
    			index1++;
    		}
    		//checkt voor collision met grond en anders laat cube bewegen
    		if (!cube.collisionCube && cube.cubeY > height / 3 + cube.cubeHeight) {
    			cube.cubeY -= cubeSpeed;
    			if (cube.cubeY < height / 3 + cube.cubeHeight) {
    				cube.cubeY = height / 3 + cube.cubeHeight;
    			}
    		}
    		index++;
    	}
    }
    
 	private void Move() {
 		boolean collision = false;
 		for (Cube cube:cubes) {
 			if ((intersectUserLeft(cube) && angle > 1.5) || (intersectUserRight(cube) && angle < -1.5)) {
 				collision = true;
 			}
 		}
 		if ((angle < -1.5 || angle > 1.5) && collision == false) {
 			float speed = angle;
 			if (angle > 8) {
 				speed = 8;
 			} else if (angle < -8) {
 				speed = -8;
 			}
 			x -= speed;
 			//door de muren
 			if (x <= 0 - userWidth / 2) {
 				x = width - userWidth / 2;
 			}
 			else if (x >= width - userWidth / 2) {
 				x = 0 - userWidth / 2;
 			}
 		}
 	}
 	
    private void Jump() {
    	jumped = false;
    	startJump++;
    	jumping = true;
    	if (startJump >= jumpMilliSeconds / 10) {
    		jumping = false;
    	}
    	else {
    		//loopt door elke cube
    		for (Cube cube:cubes) {
    			//kijk of de cube zich op het veld bevindt
    			if (cube.cubeY - position >= -500 && cube.cubeY - position <= height + cube.cubeHeight + 500) {
    				if (intersectUserTop(cube)) {
        	    		position -= userSpeed;
        	    		jumping = false;
        				break;
        			}
    			}
    		}
    		if (jumping) {
    			position += userSpeed;
    		}
    	}
    }
    
    private void Dead() {
    	for (Cube cube:cubes){
    		if (cube.cubeY - position >= -500 && cube.cubeY - position <= height + cube.cubeHeight + 500) {
    			if (intersectUserTop(cube) && intersectUserBot(cube)) {
    				dead = true;
    			}
    		}
    	}
    	
    }
    
    private void Fall() {
    	boolean collision = false;
    	for (Cube cube:cubes) {
    		if (cube.cubeY - position >= -500 && cube.cubeY - position <= height + cube.cubeHeight + 500) {
    			if (intersectUserBot(cube)) {
    				position = cube.cubeY - height / 3 - 1;
    				jumped = true;
    				collision = true;
    				if (!cube.collisionCube){
    					userSpeed = cubeSpeed;
    				}
    				break;
    			}
    		}
    	}
    	if (!jumping && position > 0 && !collision) {
    		position -= userSpeed;
    		if (position < 0) {
    			position = 0;
    		}
    	}
    	if (position == 0) {
    		jumping = false;
    		jumped = true;
    	}
    }
    
    private boolean intersectUserBot(Cube cube) {
    	boolean collision = false;
    	if (position + height / 3 <= cube.cubeY && position + height / 3 >= cube.cubeY - userSpeed * 2) {
			int xLeft = (int) x;
			int xMiddle = (int) (x + userWidth / 2);
			int xRight = (int) (x + userWidth);
			for(int i = 0; i < cube.cubeWidth; i++){
    			if (xLeft == cube.cubeX + i || 
    					xMiddle == cube.cubeX + i || 
    					xRight == cube.cubeX + i) 
    			{
    				collision = true;
    			}
    		}
		}
		return collision;
    }
    
    private boolean intersectUserTop(Cube cube) {
    	boolean collision = false;
    	if (position + height / 3 + userHeight >= cube.cubeY - cube.cubeHeight && position + height / 3 + userHeight <= cube.cubeY - cube.cubeHeight + userSpeed * 2) {
			int xLeft = (int) x;
			int xMiddle = (int) (x + userWidth / 2);
			int xRight = (int) (x + userWidth);
			for(int i = 0; i < cube.cubeWidth; i++){
    			if (xLeft == cube.cubeX + i || 
    					xMiddle == cube.cubeX + i || 
    					xRight == cube.cubeX + i) 
    			{
    				collision = true;
    			}
    		}
		}
		return collision;
    }
    
    private boolean intersectUserLeft(Cube cube) {
    	boolean collision = false;
    	
    	/**INSERT CODE HERE**/
    	
		return collision;
    }
    
    private boolean intersectUserRight(Cube cube) {
    	boolean collision = false;
    	
    	/**INSERT CODE HERE**/
    	
		return collision;
    }
    
    class GameView extends View {
    	int userID = getResources().getIdentifier("user", "drawable", getPackageName());
		Bitmap userImage = BitmapFactory.decodeResource(getResources(), userID);
		Bitmap userImageScaled = Bitmap.createScaledBitmap(userImage, userWidth, userHeight, true);
    	int floorID = getResources().getIdentifier("floor", "drawable", getPackageName());
		Bitmap floor = BitmapFactory.decodeResource(getResources(), floorID);
		Bitmap floorScaled = Bitmap.createScaledBitmap(floor, width, height / 3, true);
		Paint textColor = new Paint();
		public GameView(Context context) {
			super(context);
			textColor.setColor(Color.BLUE);
			textColor.setTextSize(16);
		}
		
		@Override
		protected void onDraw(Canvas c) {
			c.drawColor(Color.WHITE);
			if (position <= height / 3) {
				c.drawBitmap(floorScaled, 0, height / 3 * 2 + position, null);
			}
			for (Cube cube: cubes) {
				if (cube.cubeY - position >= 0 && cube.cubeY - position <= height + cube.cubeHeight) {
					c.drawBitmap(cube.block, cube.cubeX, height - (cube.cubeY - position), null);
				}
			}
            c.drawBitmap(userImage, x, height / 3 * 2 - userHeight, null);
            long points = position * 1000 / height;
            c.drawText(String.valueOf(points), width / 2, 100, textColor);
            invalidate();
        }
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    		if (jumped) {
	    			startJump = 0;
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
