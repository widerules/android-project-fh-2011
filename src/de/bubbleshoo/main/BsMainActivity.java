/**
 * 
 */
package de.bubbleshoo.main;

import de.bubbleshoo.graphics.BsSurfaceView;
import de.bubbleshoo.map.Map;
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
import android.os.Vibrator;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author oliverl
 *
 */
public class BsMainActivity extends Activity implements SensorEventListener{
	/**
	 * View to show OpenGL-Contents
	 * @uml.property  name="m_GLView"
	 * @uml.associationEnd  
	 */
	private GLSurfaceView		m_GLView;
	
	/**
	 * Sensoren
	 * @uml.property  name="mSensorManager"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="android.hardware.Sensor"
	 */
		private SensorManager 	mSensorManager;
	    /**
		 * @uml.property  name="mAccelerometer"
		 * @uml.associationEnd  
		 */
	    private Sensor 			mAccelerometer;
	    /**
		 * @uml.property  name="mkompass"
		 * @uml.associationEnd  
		 */
	    private Sensor 			mkompass;
	    /**
		 * @uml.property  name="mlight"
		 * @uml.associationEnd  
		 */
	    private Sensor 			mlight;
	    //Attribute für Sensoren:
	    // touch events
	    /**
		 * @uml.property  name="nONE"
		 */
	    private final int 		NONE = 0;
	    /**
		 * @uml.property  name="dRAG"
		 */
	    private final int 		DRAG = 1;
	    /**
		 * @uml.property  name="zOOM"
		 */
	    private final int 		ZOOM = 2;
	    // pinch to zoom
	    /**
		 * @uml.property  name="oldDist"
		 */
	    float 					oldDist = 100.0f;
	    /**
		 * @uml.property  name="newDist"
		 */
	    float 					newDist;
	    /**
		 * @uml.property  name="mode"
		 */
	    int 					mode = 0;
	    /**
		 * @uml.property  name="tOUCH_SCALE_FACTOR"
		 */
	    private final float 	TOUCH_SCALE_FACTOR = 180.0f / 320;
	    /**
		 * @uml.property  name="kompassOn"
		 */
	    private boolean 		kompassOn=true;
	    /**
		 * @uml.property  name="lichtOn"
		 */
	    private boolean 		lichtOn=false;
	    
	    //Bewegungsensor Zwischenspeicher:
	    private static float[] alteXWerte;
	    private static float[] alteYWerte;
	    
	    //Schwellwerte (0.1)
	    private static float sensorSchwellwert=(float) 0.0;
	    
	    /**
		 * @uml.property  name="v"
		 * @uml.associationEnd  
		 */
	    private Vibrator v;
	    private static BsMainActivity bsMainActivity;
	    
	    /*
	     * Android Attribute
	     */
	    private static Context anwendungsContex;
	    
	    //Handy Zustand (resum Create...)
	    public static int m_appzustand = 0;
	    public final static int 	ONCREATE=	1;
	    public final static int 	ONRESUME=	2;
	    public final static int 	ONPAUSE=	3;
	    public final static int 	ONEXIT=		4;
	    
	    
    /** 
     * Construktor
     * Called when the activity is first created. 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        anwendungsContex=this.getApplicationContext();
        bsMainActivity=this;
        BsMainActivity.getAnwendungsContex();
		v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        
        //LanDscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);  
//        //Fullscreen
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(	WindowManager.LayoutParams.FLAG_FULLSCREEN, 
//                				WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Sensoren laden
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		boolean accelerometerAvailable = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() > 0;
	
		//Sensor zwwischenspeicher initialisieren
		alteXWerte=new float[3];alteXWerte[0]=0;alteXWerte[1]=0;alteXWerte[2]=0;
		alteYWerte=new float[3];alteYWerte[0]=0;alteYWerte[1]=0;alteYWerte[2]=0;
		
		
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
		 
		 m_appzustand=ONCREATE;
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
        
        m_appzustand=ONPAUSE;
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
		
		 m_appzustand=ONRESUME;
	}
	
	
	/*
	 * Events
	 */
	 
	/** Wird aufgerufen wenn einer aufs Display drückt
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
	    	boolean bigchange=checkForChange(event.values[1],event.values[0]);
//	    	System.out.println("2te Wert y"+event.values[1]);
//	    	System.out.println("1te Wert x"+event.values[0]);
	    	
	    	if(!bigchange)
	    	{
//	    		System.out.println("nix");
	    	}
	    	else
	    	{
	    		BsDataholder.setHandykipplageX(event.values[1]);
	    		BsDataholder.setHandykipplageY(event.values[0]);
	    	}
	    }

	    /** Die funktion guck ob der Sensor sich sehr dolle verändert (Sehr dolle je nach sensorSchwellwert)
	     * 
	     * @param x = z.B. die X achsen PositioN
	     * @param yy = die y Achsen Position
	     * @return true wenn es eine größere änderung gibt
	     */
	    private boolean checkForChange(float x, float yy) {
			
//	    	System.out.println("Liste 0="+alteXWerte[0]);
//	    	System.out.println("Liste 1="+alteXWerte[1]);
//	    	System.out.println("Liste 2="+alteXWerte[2]);
//	    	System.out.println("y ABS0="+Math.abs(x-alteXWerte[0]));
//	    	System.out.println("y ABS1="+Math.abs(x-alteXWerte[0]));
//	    	System.out.println("y ABS2="+Math.abs(x-alteXWerte[0]));
	    	
	    	for(int i=0; i<alteXWerte.length;i++)
	    	{
	    		if(Math.abs(x-alteXWerte[i])>sensorSchwellwert)
		    	{
	    			addalteXWerte(x);
	    	    	addalteYWerte(yy);
	    			return true;
		    	}
	    		
	    	}
	    	for(int i=0; i<alteYWerte.length;i++)
	    	{
	    		if(Math.abs(yy-alteYWerte[i])>sensorSchwellwert)
		    	{
	    			addalteXWerte(x);
	    	    	addalteYWerte(yy);
	    			return true;
		    	}
	    		
	    	}
	    	addalteXWerte(x);
	    	addalteYWerte(yy);
			return false;
		}


		//Kompass
	    //Liefer 0-360 ich vermute 0 bedeutet Norden ist vom Handy oben
	    /**
		 * @uml.property  name="mCompassListener"
		 * @uml.associationEnd  
		 */
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
	    //Wert zwischen 10 und 360... Normales licht ist ca 140. Wenn man Telefon an Ohr hält 10. 
	    /**
		 * @uml.property  name="mlichtListener"
		 * @uml.associationEnd  
		 */
	    private final SensorEventListener mlichtListener = new SensorEventListener() {
		   	 
	  	  public void onSensorChanged(SensorEvent event) {
		  		if(lichtOn)
			  	{
		  		  	float[] mValues = event.values;
		  		  	BsDataholder.setRaumhelligkeit(event.values[0]) ;
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
	    
		
		private static void addalteXWerte(float x)
		{
			for(int i=alteXWerte.length-1;i>0;i--)
			{
				alteXWerte[i]=alteXWerte[i-1];
			}
		   alteXWerte[0]=x;

		}
		private static void addalteYWerte(float y)
		{
			for(int i=alteYWerte.length-1;i>0;i--)
			{
				alteYWerte[i]=alteYWerte[i-1];
			}
			
		    alteYWerte[0]=y;
		}

		/**
		 * @return
		 * @uml.property  name="v"
		 */
		public Vibrator getV() {
			return v;
		}

		/**
		 * @param v
		 * @uml.property  name="v"
		 */
		public void setV(Vibrator v) {
			this.v = v;
		}

		public static BsMainActivity getBsMainActivity() {
			return bsMainActivity;
		}

		public static void setBsMainActivity(BsMainActivity bsMainActivity) {
			BsMainActivity.bsMainActivity = bsMainActivity;
		}
		
		
		
}