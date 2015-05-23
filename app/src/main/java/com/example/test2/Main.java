package com.example.test2;

import org.openintents.sensorsimulator.hardware.Sensor;
import org.openintents.sensorsimulator.hardware.SensorEvent;
import org.openintents.sensorsimulator.hardware.SensorEventListener;
import org.openintents.sensorsimulator.hardware.SensorManagerSimulator;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
//uncoment if NO SIMULATOR
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//
import android.hardware.SensorManager;
//import android.view.SurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

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
//    else {
   // private SensorManager mSensorManager;


    private Sensor accelerometer;
	private ImageView image;
	private final Matrix mMatrix = new Matrix();
	private RectF mDisplayRect = new RectF();
	private float mScaleFactor;
	private long lastUpdate = 0;
	private float minOffsetX, maxOffsetX, imageOffsetX =0;

    private static final float BASE_X_OFFSET_FROM_CENTER =50;
    private static final float BASE_Y_OFFSET=510;

    // timer variables
    private Handler customHandler = new Handler();
    private int lastHour=-1;

    //Gif
    private float gifBaseX;
    private GifImageView gifView;
    private static final int GIF_ID=R.drawable.samolot_03;

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
		Point displaySize = new Point();
		display.getSize(displaySize);
		
		float displaySizeY= (float) displaySize.y;
		float imageY = (float) image.getDrawable().getIntrinsicHeight();
		mScaleFactor = displaySizeY / imageY;

        float scaleWidth = (float)displaySize.x/(float) image.getDrawable().getIntrinsicWidth();
		imageOffsetX = (image.getDrawable().getIntrinsicWidth()*mScaleFactor-displaySize.x) /2 ;
		minOffsetX = 0;
		maxOffsetX = image.getDrawable().getIntrinsicWidth()*mScaleFactor-displaySize.x;

        gifView =(GifImageView)findViewById(R.id.gifView);

        //TODO magic number
        float temp=BASE_Y_OFFSET*(imageY/693);
        float y= BASE_Y_OFFSET/693 *displaySizeY - gifView.getLayoutParams().height;
        gifView.setY(y);
        Log.d(TAG,"GIF_Y "+y);
        //TODO magic number
       // gifBaseX = (image.getDrawable().getIntrinsicWidth()*mScaleFactor - 100) /2 ;
        gifBaseX = (image.getDrawable().getIntrinsicWidth()*mScaleFactor + BASE_X_OFFSET_FROM_CENTER)/2;



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
				
				imageOffsetX +=y*10;
				imageOffsetX =clamp(imageOffsetX, minOffsetX, maxOffsetX);
				//update image
				mMatrix.reset();
				mMatrix.postScale(mScaleFactor, mScaleFactor);
				mMatrix.postTranslate(-imageOffsetX, 0);
				image.setImageMatrix(mMatrix);
				updateDisplayRect();
                //move gif
                gifView.setX(gifBaseX - imageOffsetX);
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
//                        case 3:
//                        case 4:
//                                image.setImageResource(R.drawable.h_3);
//                                gifView.setImageDrawable(null);
//                            break;
//                        case 5:
//                            image.setImageResource(R.drawable.h_5);
//                            gifView.setImageDrawable(null);
//                            break;
//                        case 6:
//                        case 7:
//                            image.setImageResource(R.drawable.h_6);
//                            gifView.setImageDrawable(null);
//                            break;
//                        case 8:
//                        case 9:
//                            image.setImageResource(R.drawable.h_8);
//                            gifView.setImageResource(GIF_ID);
//                            break;
//                        case 10:
//                        case 11:
//                            image.setImageResource(R.drawable.h_10);
//                            gifView.setImageResource(GIF_ID);
//                            break;
//                        case 12:
//                        case 13:
//                        case 14:
//                            image.setImageResource(R.drawable.h_12);
//                            gifView.setImageResource(GIF_ID);
//                            break;
//                        case 15:
//                        case 16:
//                            image.setImageResource(R.drawable.h_15);
//                            gifView.setImageResource(GIF_ID);
//                            break;
//                        case 17:
//                        case 18:
//                            image.setImageResource(R.drawable.h_17);
//                            gifView.setImageResource(GIF_ID);
//                            break;
//                        case 19:
//                            image.setImageResource(R.drawable.h_19);
//                            gifView.setImageResource(GIF_ID);
//                            break;
//                        case 20:
//                            image.setImageResource(R.drawable.h_20);
//                            gifView.setImageDrawable(null);
//                            break;
//                        case 21:
//                            image.setImageResource(R.drawable.h_21);
//                            gifView.setImageDrawable(null);
//                            break;
//                        case 23:
//                            image.setImageResource(R.drawable.h_23);
//                            gifView.setImageDrawable(null);
//                            break;
//                        case 24:
//                            image.setImageResource(R.drawable.h_24);
//                            gifView.setImageDrawable(null);
//                            break;
                        default :
                            image.setImageResource(R.drawable.h_12);
                            gifView.setImageResource(GIF_ID);
                    }
                }

                lastHour=currentHour;
                customHandler.post(this);


        }
    };

}
