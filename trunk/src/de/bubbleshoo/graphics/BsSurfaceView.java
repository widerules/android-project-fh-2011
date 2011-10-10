/**
 * 
 */
package de.bubbleshoo.graphics;

import java.util.ArrayList;
import java.util.List;

import de.bubbleshoo.data.BsElement;
import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * @author oliverl
 *
 */
public class BsSurfaceView extends GLSurfaceView {

	/**
	 * Renderer
	 */
	private BsRenderer		m_bsRenderer;
	
	/**
	 * Elements to draw
	 */
	private List<BsElement>	m_lstElements;
	
	/**
	 * Constructor
	 * @param context
	 */
	public BsSurfaceView(Context context) {
		super(context);
		// Create an OpenGL ES 2.0 Context
		setEGLContextClientVersion(2);
		// Init Elements
		initElementList();
		// Set the renderer for drawing on the GLSurfaceView
		this.m_bsRenderer = new BsRenderer();
		this.m_bsRenderer.setLstElements(this.m_lstElements);
		setRenderer(this.m_bsRenderer);
	}

	/**
	 * Initialize all elements to draw
	 */
	private void initElementList() {
		this.m_lstElements = new ArrayList<BsElement>();
		
		// Dreieck
		 float triangleCoords[] = {
		            // X, Y, Z
		            -0.5f, -0.25f, 0,
		             0.5f, -0.25f, 0,
		             0.0f,  0.559016994f, 0
		        };
		 
		 BsElement emt1 = new BsElement(triangleCoords);
		 this.m_lstElements.add(emt1);
	}
}
