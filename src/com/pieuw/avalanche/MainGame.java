package com.pieuw.avalanche;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Canvas;


public class MainGame extends Activity {

	GameView gameView;
	Paint paint = new Paint();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new GameView(this));
	}
	
	class GameView extends View {
		public GameView(Context context) {
			super(context);
		}
		
		@Override
		protected void onDraw(Canvas c) {
			super.onDraw(c);
			paint.setColor(Color.BLUE);
            c.drawCircle(50, 50, 20, paint);
        }
	}
}
