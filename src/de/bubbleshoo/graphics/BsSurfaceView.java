/**
 * 
 */
package de.bubbleshoo.graphics;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import de.bubbleshoo.data.BaseObject3D;
import de.bubbleshoo.data.Bs3DObject;
import de.bubbleshoo.data.BsMesh;
import de.bubbleshoo.main.R;
import de.bubbleshoo.main.R.raw;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

/**
 * @author oliverl
 *
 */
public class BsSurfaceView extends GLSurfaceView {

	/* (non-Javadoc)
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = event.getX();
        float y = event.getY();
        
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
    
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;
    
                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                  dx = dx * -1 ;
                }
    
                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                  dy = dy * -1 ;
                }
              
                //m_bsRenderer.setAngleX(m_bsRenderer.getAngleX() + (dx) * TOUCH_SCALE_FACTOR);
                //m_bsRenderer.setAngleY(m_bsRenderer.getAngleY() + (dy) * TOUCH_SCALE_FACTOR);
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;

	}

	/**
	 * Renderer
	 */
	private BsRenderer		m_bsRenderer;
	
	/**
	 * 
	 */
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private float mPreviousX;
    private float mPreviousY;
	
	/**
	 * Elements to draw
	 */
	private Stack<BaseObject3D>	m_lstElements;
	
	/**
	 * Constructor
	 * @param context
	 */
	public BsSurfaceView(Context context) {
		super(context);
		// Create an OpenGL ES 2.0 Context
		setEGLContextClientVersion(2);
		// Init Elements
		initElementList(context);
		// Set the renderer for drawing on the GLSurfaceView
		this.m_bsRenderer = new BsRenderer(context);
		this.m_bsRenderer.setLstElements(this.m_lstElements);
		setRenderer(this.m_bsRenderer);
		
		// Render the view always
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}

	/**
	 * Initialize all elements to draw
	 */
	private void initElementList(Context context) {
		this.m_lstElements = new Stack<BaseObject3D>();
		Bs3DObject emt = null;
		
		try {
            //int[] normalMapTextures = {R.raw.diffuse_old, R.raw.diffusenormalmap_deepbig};
            int[] textures1 = {R.raw.fieldstone, R.raw.fieldstonebump_dot3};
            int[] textures2 = {R.raw.diffuse_old, R.raw.diffusenormalmap_deepbig};
//            emt = new Bs3DObject(textures1, R.raw.texturedcube, true, context);
//            emt.setAngleY(0.0f);
//            emt.setScale(1.0f, 1.0f, 1.0f);
//            emt.setTranslate(-2.5f, 0.0f, 0.0f);
//            this.m_lstElements.add(emt);
//            emt = new Bs3DObject(textures2, R.raw.texturedcube, true, context);
//            emt.setAngleY(0.0f);
//            emt.setScale(1.0f, 1.0f, 1.0f);
//            emt.setTranslate(2.5f, 0.0f, 0.0f);
//            this.m_lstElements.add(emt);
            //emt = new Bs3DObject(textures1, R.raw.test2, true, context);
//            emt = new BaseObject3D(name);
//            emt.setAngleY(0.0f);
//            emt.setScale(3.0f, 3.0f, 3.0f);
//            emt.setTranslate(0.0f, 0.0f, 0.0f);
//            this.m_lstElements.add(emt);
	    } catch (Exception e) {
	    	System.out.println("" + e.getMessage());
	    }
	}
	
	/**
	 * l�dt die Map
	 */
	private void loadMap() {
		
	}
}