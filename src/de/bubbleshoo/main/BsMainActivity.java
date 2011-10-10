/**
 * 
 */
package de.bubbleshoo.main;

import de.bubbleshoo.graphics.BsSurfaceView;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

/**
 * @author oliverl
 *
 */
public class BsMainActivity extends Activity {
	/**
	 * View to show OpenGL-Contents
	 */
	private GLSurfaceView		m_GLView;
	
    /** 
     * Construktor
     * Called when the activity is first created. 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.m_GLView = new BsSurfaceView(this);
        setContentView(this.m_GLView);
    }
    
	/** 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		// set the renderingthread in pausemode
		this.m_GLView.onPause();
	}

	/** 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		// resume the renderingthread
		this.m_GLView.onResume();
	}
}