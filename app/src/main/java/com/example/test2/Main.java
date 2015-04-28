package com.example.test2;

import org.openintents.sensorsimulator.hardware.Sensor;
import org.openintents.sensorsimulator.hardware.SensorEvent;
import org.openintents.sensorsimulator.hardware.SensorEventListener;
import org.openintents.sensorsimulator.hardware.SensorManagerSimulator;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
//import android.view.SurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Display;
import android.widget.ImageView;
import android.widget.VideoView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import pl.droidsonroids.gif.GifImageView;

public class Main extends Activity implements SensorEventListener{

    // Transforming images variables
    // IF USING_SIMULATOR REMBER TO:
    // comment/uncomment imports
    // add INTERNET PERMISSION to manifest

	// if (USING_SIMULATOR) {
    private SensorManagerSimulator mSensorManager;
    //else {
    //private SensorManager mSensorManager;
    private Sensor accelerometer;
	private ImageView image;
	private final Matrix mMatrix = new Matrix();
	private RectF mDisplayRect = new RectF();
	private float mScaleFactor;
	private long lastUpdate = 0;
	private float minOffsetY,maxOffsetY,imageOffsetY=0;

    // timer variables
    private Handler customHandler = new Handler();
    private int lastHour=-1;

    //Gif
    private float gifBaseX;
    private GifImageView gifView;

    private static final String TAG="MY_DEBUG";

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

        gifView =(GifImageView)findViewById(R.id.gifView);

        //TODO magic number
        gifBaseX = (image.getDrawable().getIntrinsicWidth()*mScaleFactor - 100) /2 ;

        customHandler.post(timeThread);

        //USING_SIMULATOR
            //connecting to simulator
            mSensorManager = SensorManagerSimulator.getSystemService(this, SENSOR_SERVICE);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            mSensorManager.connectSimulator();
        // NOT USING_SIMULATOR
//            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//            accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//            mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

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
                //move gif
                gifView.setX(gifBaseX -imageOffsetY);
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

    private  Runnable timeThread= new Runnable(){
        public void run(){

                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                if (currentHour != lastHour) {

                    switch(currentHour){
                        case 3:
                        case 4:
                                image.setImageResource(R.drawable.h_3);
                            break;
                        case 5:
                            image.setImageResource(R.drawable.h_5);
                            break;
                        case 6:
                        case 7:
                            image.setImageResource(R.drawable.h_6);
                            break;
                        case 8:
                        case 9:
                            image.setImageResource(R.drawable.h_8);
                            break;
                        case 10:
                        case 11:
                            image.setImageResource(R.drawable.h_10);
                            break;
                        case 12:
                        case 13:
                        case 14:
                            image.setImageResource(R.drawable.h_12);
                            break;
                        case 15:
                        case 16:
                            image.setImageResource(R.drawable.h_15);
                            break;
                        case 17:
                        case 18:
                            image.setImageResource(R.drawable.h_17);
                            break;
                        case 19:
                            image.setImageResource(R.drawable.h_19);
                            break;
                        case 20:
                            image.setImageResource(R.drawable.h_20);
                            break;
                        case 21:
                            image.setImageResource(R.drawable.h_21);
                            break;
                        case 23:
                            image.setImageResource(R.drawable.h_23);
                            break;
                        case 24:
                            image.setImageResource(R.drawable.h_24);
                            break;
                        default :
                            image.setImageResource(R.drawable.h_24);
                    }
                }

                customHandler.post(this);


        }
    };

}
