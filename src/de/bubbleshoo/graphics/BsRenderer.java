/**
 * 
 */
package de.bubbleshoo.graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

/**
 * @author oliverl
 *
 */
public class BsRenderer implements GLSurfaceView.Renderer{

	/**
	 * Is called for each redraw of the View
	 */
	@Override
	public void onDrawFrame(GL10 gl) {
		// Redraw background Color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
	}

	/**
	 *  Is called if the view-geometry changes, perhaps if the screen's orientation changes
	 *  between landsape an portrait
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
	}

	/**
	 * Is called once to initalize the GLSurfaceView environmet
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Set the background frame color
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
	}

}
