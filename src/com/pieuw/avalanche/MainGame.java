package com.pieuw.avalanche;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Canvas;


public class MainGame extends Activity {

	GameView gameView;
	Paint paint = new Paint();
	private float x = 100;
	private float y = 200;
	int radius = 50;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new GameView(this));
		paint.setColor(Color.BLUE);
	}
	
	class GameView extends View {
		public GameView(Context context) {
			super(context);
		}
		
		@Override
		protected void onDraw(Canvas c) {
            c.drawCircle(x, y, radius, paint);
            invalidate();
        }
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			x = event.getX();
			y = event.getY();
			return true;
		}
	}
}
