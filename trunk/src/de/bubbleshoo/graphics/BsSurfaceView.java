/**
 * 
 */
package de.bubbleshoo.graphics;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * @author oliverl
 *
 */
public class BsSurfaceView extends GLSurfaceView {

	/**
	 * Constructor
	 * @param context
	 */
	public BsSurfaceView(Context context) {
		super(context);
		// Create an OpenGL ES 2.0 Context
		setEGLContextClientVersion(2);
		// Set the renderer for drawing on the GLSurfaceView
		setRenderer(new BsRenderer());
	}

}
