/**
 * 
 */
package de.bubbleshoo.main;

import de.bubbleshoo.graphics.BsSurfaceView;
import de.bubbleshoo.map.MapLoader;
import de.bubbleshoo.sensors.BsDataholder;
import de.bubbleshoo.settings.GeneralSettings;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @author oliverl
 *
 */
public class BsMainActivity extends Activity implements SensorEventListener{
	/**
	 * View to show OpenGL-Contents
	 */
	private GLSurfaceView		m_GLView;
	
	/**
	 * Sensoren
	 */
		private SensorManager 	mSensorManager;
	    private Sensor 			mAccelerometer;
	    private Sensor 			mkompass;
	    private Sensor 			mlight;
	    //Attribute f�r Sensoren:
	    // touch events
	    private final int 		NONE = 0;
	    private final int 		DRAG = 1;
	    private final int 		ZOOM = 2;
	    // pinch to zoom
	    float 					oldDist = 100.0f;
	    float 					newDist;
	    int 					mode = 0;
	    private final float 	TOUCH_SCALE_FACTOR = 180.0f / 320;
	    private boolean 		kompassOn=true;
	    private boolean 		lichtOn=false;
    
	    /*
	     * Android Attribute
	     */
	    private static Context anwendungsContex;
	    
    /** 
     * Construktor
     * Called when the activity is first created. 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        anwendungsContex=this.getApplicationContext();
        
        //LanDscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);  
        
        //Sensoren laden
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		boolean accelerometerAvailable = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() > 0;
	
		mAccelerometer = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
		if(!mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME ) )
		   accelerometerAvailable = false;
		
		//Kombass
		if(kompassOn){
		mkompass=mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION).get(0);;
		mSensorManager.registerListener(mCompassListener, mkompass,SensorManager.SENSOR_DELAY_NORMAL);
		}
		
		//Licht
		if(lichtOn){
		mlight=mSensorManager.getSensorList(Sensor.TYPE_LIGHT).get(0);;
		mSensorManager.registerListener(mlichtListener, mlight,SensorManager.SENSOR_DELAY_NORMAL);
		}
		
	
		 if (detectOpenGLES20()) 
	        {
			   this.m_GLView = new BsSurfaceView(this);
		       setContentView(this.m_GLView);
	        } 
	        else
	        {
	        	Log.e(GeneralSettings.LoggerKategorie, "OpenGL ES 2.0 not supported on device.  Exiting...");
	        	finish();
	        } 
		 
		 //Joerns Test Bereich Start
		 try {
			MapLoader.laodMap("1map");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 //Joerns Test Bereich Ende
    }
    
	/** 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		// set the renderingthread in pausemode
		this.m_GLView.onPause();
		//Sensoren unregistrieren
		mSensorManager.unregisterListener(this);
		
		if(kompassOn)
        mSensorManager.unregisterListener(mCompassListener);
        if(lichtOn)
        mSensorManager.unregisterListener(mlichtListener);
	}

	/** 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		// resume the renderingthread
		this.m_GLView.onResume();
		//Sensoren Registrieren
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		if(kompassOn)
		mSensorManager.registerListener(mCompassListener, mkompass,SensorManager.SENSOR_DELAY_NORMAL);
		if(lichtOn)
		mSensorManager.registerListener(mlichtListener,mlight, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	
	/*
	 * Events
	 */
	 
	/** Wird aufgerufen wenn einer aufs Display dr�ckt
	 * 
	 */
	    @Override public boolean onTouchEvent(MotionEvent e) {
	            float x = e.getX();
	            float y = e.getY();
                     
	            switch (e.getAction()) {
	                    case MotionEvent.ACTION_DOWN:                   // one touch: drag
	                  Log.d(GeneralSettings.LoggerKategorie, "mode=DRAG" );
	                  mode = DRAG;
	                  break;
	                    case MotionEvent.ACTION_POINTER_DOWN:   // two touches: zoom
	                            Log.d(GeneralSettings.LoggerKategorie, "mode=ZOOM" );
	                            oldDist = spacing(e);
	                            
	                            if (oldDist > 10.0f) {
	                                    mode = ZOOM; // zoom
	                            }
	                            break;
	                    case MotionEvent.ACTION_UP:             // no mode
	                            mode = NONE;
	                            Log.d(GeneralSettings.LoggerKategorie, "mode=NONE" );
	                            oldDist = 100.0f;
	                            break;
	                    case MotionEvent.ACTION_POINTER_UP:             // no mode
	                            mode = NONE;
	                            Log.d(GeneralSettings.LoggerKategorie, "mode=NONE" );
	                            oldDist = 100.0f;
	                            break;
	                    case MotionEvent.ACTION_MOVE:                                           // rotation
	                            if (e.getPointerCount() > 1 && mode == ZOOM) {
	                                    newDist = spacing(e);
	                                    Log.d(GeneralSettings.LoggerKategorie, "OldDist: " + oldDist + ", NewDist: " + newDist);
	                                    BsDataholder.setAbstandZwischenZweiFingern(newDist);
	                                    if (newDist > 10.0f) {
	                                            float scale = newDist/oldDist; // scale
	                                            // scale in the renderer
	                                           // renderer.changeScale(scale);
	                                            
	                                            oldDist = newDist;
	                                    }
	                                    /*if (newDist > oldDist) {              // distance increasing - scale
	                                            renderer.increaseScale();
	                                    }
	                                    else if (newDist < oldDist){    // distance decreasing
	                                            renderer.decreaseScale();
	                                    }*/
	                            }
	                            else if (mode == DRAG){
	                	            BsDataholder.setTouchModus(mode);
	                	            BsDataholder.setTouchX(x* TOUCH_SCALE_FACTOR);
	                	            BsDataholder.setTouchY(y* TOUCH_SCALE_FACTOR);
	                            }
	                            break;
	            }

	           // mAngle = y;
	            return true;
	    }
	    
	  /** Macht nichts momentan...
	   * (non-Javadoc)
	   * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor, int)
	   */
	    public void onAccuracyChanged(Sensor sensor, int accuracy) {

	    }

	    /** Der Handypositionssensor / Neigungssensor
	     * 
	     */
	    public void onSensorChanged(SensorEvent event) {
	    	//Vertauscht wegen Lenscape
	        BsDataholder.setHandykipplageX(event.values[1]* TOUCH_SCALE_FACTOR);
            BsDataholder.setHandykipplageY(event.values[0]* TOUCH_SCALE_FACTOR);
	    }
	    
	    
	    //Kompass
	    //Liefer 0-360 ich vermute 0 bedeutet Norden ist vom Handy oben
	    private final SensorEventListener mCompassListener = new SensorEventListener() {
	    	   	 
	    	  public void onSensorChanged(SensorEvent event) {
		    		  if(kompassOn)
		    		  	{
		    			  	float[] mValues = event.values;
		    		  		BsDataholder.setKompassrichtung(event.values[0]);
		    		  	}
	    		  	}
	    	  
	    	  public void onAccuracyChanged(Sensor sensor, int accuracy) {
	    	  }
	    };
	    
	    //Licht
	    //Wert zwischen 10 und 360... Normales licht ist ca 140. Wenn man Telefon an Ohr h�lt 10. 
	    private final SensorEventListener mlichtListener = new SensorEventListener() {
		   	 
	  	  public void onSensorChanged(SensorEvent event) {
		  		if(lichtOn)
			  	{
		  		  	float[] mValues = event.values;
		  		  	BsDataholder.setRaumhelligkeit(event.values[0]* TOUCH_SCALE_FACTOR) ;
			  	}
		  	}
	  	  
	  	  public void onAccuracyChanged(Sensor sensor, int accuracy) {
	  	  }
	  };
	    // finds spacing
	    private float spacing(MotionEvent event) {
	            float x = event.getX(0) - event.getX(1);
	            float y = event.getY(0) - event.getY(1);
	            return FloatMath.sqrt(x * x + y * y);
	    }
	    /** Guckt ob das Handy OpeG
	     * 
	     * @return
	     */   
	    private boolean detectOpenGLES20() 
	     {
	         ActivityManager am =
	             (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	         ConfigurationInfo info = am.getDeviceConfigurationInfo();
	         return (info.reqGlEsVersion >= 0x20000);
	     }


	    
	    /*
	     * Getter Setter
	     */
		public static Context getAnwendungsContex() {
			return anwendungsContex;
		}

		public static void setAnwendungsContex(Context anwendungsContex) {
			BsMainActivity.anwendungsContex = anwendungsContex;
		}
	    
	    
}