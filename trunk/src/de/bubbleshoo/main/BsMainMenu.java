package de.bubbleshoo.main;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class BsMainMenu extends Activity {
	Handler handler;
	ImageButton play, credits, mapchoose;

	private static final int INSERT_ID = Menu.FIRST;
	private MediaController mc;
	private static boolean firstTime=true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// LanDscape
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// Fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		if(firstTime)
		{
			setContentView(R.layout.main);

			VideoView vd = (VideoView) findViewById(R.id.myvideoview);
	
			Uri uri = Uri.parse("android.resource://" + getPackageName() + "/"
					+ R.raw.loading);
	
			vd.setVideoURI(uri);
			vd.start();
	
			vd.setOnCompletionListener(new OnCompletionListener() {
	
				public void onCompletion(MediaPlayer mp) {
					setContentView(R.layout.loadingscreen);
					firstTime=false;
					init();
	
				}
			});
		}
		else
		{
			setContentView(R.layout.loadingscreen);
			init();
		}

		// mVideoView = (VideoView) findViewById(R.id.myvideoview);
		//
		// if (path == "") {
		// // Tell the user to provide a media file URL/path.
		// Toast.makeText(
		// BsMainMenu.this,
		// "Please edit VideoViewDemo Activity, and set path"
		// + " variable to your media file URL/path",
		// Toast.LENGTH_LONG).show();
		//
		// } else {
		//
		// /*
		// * Alternatively,for streaming media you can use
		// * mVideoView.setVideoURI(Uri.parse(URLstring));
		// */
		// mVideoView.setVideoPath(path);
		// mVideoView.setMediaController(new MediaController(this));
		// mVideoView.requestFocus();
		// }

		// Timer t = new Timer();
		// TimerTask runTask;
		// handler = new Handler();
		//
		// runTask = new TimerTask() {
		// public void run() {
		// handler.post(new Runnable() {
		// public void run() {
		// setContentView(R.layout.loadingscreen);
		// init();
		// }
		// });
		// }};
		// t.schedule(runTask, 3000);
		//
		//

	}

	void init() {

		mapchoose = (ImageButton) findViewById(R.id.button1);
		play = (ImageButton) findViewById(R.id.button2);
		credits = (ImageButton) findViewById(R.id.button3);

		play.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent; // Reusable Intent for each tab
				// Create an Intent to launch an Activity for the tab (to be
				// reused)
				intent = new Intent().setClass(BsMainMenu.this,
						BsMainActivity.class);
				startActivityForResult(intent, 0);
			}
		});

		credits.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						BsMainMenu.this);
				builder.setMessage(
						"Oliver Laudi\nJorn Bettermann\nEva Bojorges")
						.setCancelable(true)
						.setTitle("Credits")
						.setPositiveButton("Done",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});

		mapchoose.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent; // Reusable Intent for each tab
				// Create an Intent to launch an Activity for the tab (to be
				// reused)
				intent = new Intent().setClass(BsMainMenu.this,
						BsSubActivityMapChoose.class);
				startActivityForResult(intent, 0);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID, 0, "FullScreen");
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case INSERT_ID:
		}
		return true;
	}
}
