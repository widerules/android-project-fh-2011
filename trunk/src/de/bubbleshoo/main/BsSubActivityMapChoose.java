package de.bubbleshoo.main;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class BsSubActivityMapChoose extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LanDscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);  
        //Fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(	WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.mapchooser);
        
        init();
        
        
	}
	/*
	 * Alles laden erzeugen Button Listener hinzufügen
	 */
	public void init()
	{
		ImageButton map1 = (ImageButton) findViewById(R.id.button1);
		ImageButton map2 = (ImageButton) findViewById(R.id.button2);
		ImageButton map3 = (ImageButton) findViewById(R.id.button3);
		
		ImageButton map4 = (ImageButton) findViewById(R.id.button4);
		ImageButton map5 = (ImageButton) findViewById(R.id.button5);
		ImageButton map6 = (ImageButton) findViewById(R.id.button6);
		
		ImageButton backbutton=(ImageButton) findViewById(R.id.button7);
		
		map1.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent; // Reusable Intent for each tab
				// Create an Intent to launch an Activity for the tab (to be
				// reused)
				intent = new Intent().setClass(BsSubActivityMapChoose.this,
						BsMainActivity.class);
				startActivityForResult(intent, 0);
			}
		});
		
		backbutton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent; // Reusable Intent for each tab
				// Create an Intent to launch an Activity for the tab (to be
				// reused)
				intent = new Intent().setClass(BsSubActivityMapChoose.this,BsMainMenu.class);
				startActivityForResult(intent, 0);
			}
		});
		
	}
}
