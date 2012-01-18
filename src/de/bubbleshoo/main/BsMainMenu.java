package de.bubbleshoo.main;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;


public class BsMainMenu extends Activity {
	Handler handler;
	ImageButton play, credits;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Timer t = new Timer();
        TimerTask runTask;
        handler = new Handler();
        
        runTask = new TimerTask() {
            public void run() {
                    handler.post(new Runnable() {
                            public void run() {
                            	setContentView(R.layout.loadingscreen);
                            	init();
                            }
                   });
            }};


        t.schedule(runTask, 4000);
        
        
    }
	
	void init()
	{
		play = (ImageButton)findViewById(R.id.button1);
		credits = (ImageButton)findViewById(R.id.button2);
		
		play.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent;  // Reusable Intent for each tab
			    // Create an Intent to launch an Activity for the tab (to be reused)
			    intent = new Intent().setClass(BsMainMenu.this, BsMainActivity.class);
			    startActivityForResult(intent, 0);
			}
		});
		
		credits.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(BsMainMenu.this);
				builder.setMessage("Oliver Laudi\nJorn Bettermann\nEva Bojorges")
				       .setCancelable(true)
				       .setTitle("Credits")
				       .setPositiveButton("Done", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                dialog.cancel();
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

}
