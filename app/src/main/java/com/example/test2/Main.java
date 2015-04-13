package com.example.test2;

//import org.openintents.sensorsimulator.hardware.Sensor;
//import org.openintents.sensorsimulator.hardware.SensorEvent;
//import org.openintents.sensorsimulator.hardware.SensorEventListener;
//import org.openintents.sensorsimulator.hardware.SensorManagerSimulator;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
public class Main extends Activity implements SensorEventListener {
	
	private SensorManager mSensorManager;
	private ImageView image;
	private final Matrix mMatrix = new Matrix();
	private RectF mDisplayRect = new RectF();
	private float mScaleFactor;
	private long lastUpdate = 0;
	private float minOffsetY,maxOffsetY,imageOffsetY=0;
	private Sensor accelerometer;
	
	public static float clamp(float val, float min, float max) {
	    return Math.max(min, Math.min(max, val));
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		image = (ImageView) findViewById(R.id.imageView1);
		//scaling and centering
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
		float sizeY= (float) size.y;
		float imageY = (float) image.getDrawable().getIntrinsicHeight();
		mScaleFactor = sizeY / imageY;

		imageOffsetY = (image.getDrawable().getIntrinsicWidth()*mScaleFactor-size.x) /2 ;
		minOffsetY = 0;
		maxOffsetY = image.getDrawable().getIntrinsicWidth()*mScaleFactor-size.x;
		//connecting to simulator		
//		mSensorManager = SensorManagerSimulator.getSystemService(this,SENSOR_SERVICE);
//		StrictMode.ThreadPolicy policy = new
//				StrictMode.ThreadPolicy.Builder()
//				.permitAll().build();
//				StrictMode.setThreadPolicy(policy);
		//mSensorManager.connectSimulator();
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		 accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		 mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.values!=null ){

			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];

			long curTime = System.currentTimeMillis();

			if ((curTime - lastUpdate) > 100) {

				lastUpdate = curTime;
				
				imageOffsetY+=y*10;
				imageOffsetY=clamp(imageOffsetY,minOffsetY,maxOffsetY);
				//update image
				mMatrix.reset();
				mMatrix.postScale(mScaleFactor, mScaleFactor);
				mMatrix.postTranslate(-imageOffsetY, 0);
				image.setImageMatrix(mMatrix);
				updateDisplayRect();
			}
		}
	}
	private void updateDisplayRect() {
		  mDisplayRect.set(0, 0, image.getDrawable().getIntrinsicWidth(), image.getDrawable().getIntrinsicHeight());
		  mMatrix.mapRect(mDisplayRect);
		}
	@Override
	protected void onStop() {
		mSensorManager.unregisterListener(this);
		super.onStop();
	}
	@Override
	protected void onPause() {
		// unregister listener
		super.onPause();
		mSensorManager.unregisterListener(this);
	}
	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_FASTEST);
	}
}
