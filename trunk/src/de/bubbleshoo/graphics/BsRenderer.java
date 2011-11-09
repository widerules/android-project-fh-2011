/**
 * 
 */
package de.bubbleshoo.graphics;

import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import de.bubbleshoo.data.Bs3DObject;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

/**
 * @author oliverl
 *
 */
public class BsRenderer implements GLSurfaceView.Renderer{
	    
	/**
	 * Needed for projectionmatrix
	 */
	private int muMVPMatrixHandle;
	
	/**
	 * Modelviewprojectionmatrix (proj * view)
	 */
	private float[] mMVPMatrix = new float[16];
	
	/**
	 * View Matrix
	 */
	private float[] mVMatrix = new float[16];
	
	/**
	 * Projectionmatrix
	 */
	private float[] mProjMatrix = new float[16];
	
	/**
	 * MotionMatrix
	 */
	private float[] mMMatrix = new float[16];
	
	/**
	 * Programm for Vertex- and Fragmentshader
	 */
	private int mProgram;
	
	/**
	 * Position for Elements manipulated by shader
	 */
    private int maPositionHandle;
    
    /**
     * Rotationswinkel
     */
    private float fAngle;
	/**
	 * Elements to draw
	 */
	private List<Bs3DObject>	m_lstElements;
	
	/**
	 *  Vertex shader
	 */
	private final String 	m_vertexShaderCode = 
		// This matrix member variable provides a hook to manipulate
        // the coordinates of the objects that use this vertex shader
        "uniform mat4 uMVPMatrix;   \n" +

        "attribute vec4 vPosition; \n" +
        "void main(){              \n" +
        //" normalVec = normalize (gl_NormalMatrix * gl_Normal);\n" +
        " gl_Position = uMVPMatrix  * vPosition; \n" +
        "}\n";
    
	/**
	 * Fragment Shader
	 */
    private final String 	m_fragmentShaderCode = 
        "precision mediump float;  \n" +
        "void main(){              \n" +
        " gl_FragColor = vec4 (0.63671875, 0.76953125, 0.22265625, 1.0); \n" +
        "}                         \n";
	
	/**
	 * Is called for each redraw of the View
	 */
	public void onDrawFrame(GL10 gl) {
		// Redraw background Color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
		// Apply a ModelView Projection transformation
        //Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
		// Create a rotation for the triangle
//        long time = SystemClock.uptimeMillis() % 4000L;
//        float angle = 0.090f * ((int) time);
        // Rotatio around z-axis
        //Matrix.setRotateM(mMMatrix, 0, -fAngle, 0.0f, 1.0f, 0.0f);
        // result multiplied with View-matrix
        //Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMMatrix, 0);
        // result multiplied with projectionmatrix
        //Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        
        
        // Draw Meshs
        for (Bs3DObject bsEmt : this.m_lstElements) {
        	bsEmt.setAngleY(-fAngle);
        	
        	bsEmt.drawObject(mProgram, muMVPMatrixHandle, mVMatrix, mProjMatrix);
		}
	}

	/**
	 *  Is called if the view-geometry changes, perhaps if the screen's orientation changes
	 *  between landsape an portrait
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		
		float fRatio = (float) width / height;
		// this projection matrix is applied to object coodinates
        // in the onDrawFrame() method
		// public static void frustumM (float[] m, int offset, float left, float right, float bottom, float top, float near, float far) 
        Matrix.frustumM(mProjMatrix, 0, -fRatio, fRatio, -1, 1, 2, 9);
        // reference the uMVPMatrix shader matrix variable
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        // define a camera view matrix
        // Set Viewpoint 3 back and 1 up in the scene
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
	}

	/**
	 * Is called once to initalize the GLSurfaceView environmet
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, this.m_vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, this.m_fragmentShaderCode);

		// Set the background frame color
		GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		
		this.mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(this.mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(this.mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(this.mProgram);                  // creates OpenGL program executables
        
        // get handle to the vertex shader's vPosition member
        this.maPositionHandle = GLES20.glGetAttribLocation(this.mProgram, "vPosition");
	}
	
	/**
	 * loads shader from its code and type
	 * @param type
	 * @param shaderCode
	 * @return
	 */
	private int loadShader(int type, String shaderCode){
	    
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type); 
        
        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        
        return shader;
    }
	
	/**
	 * @param m_lstElements the m_lstElements to set
	 */
	public void setLstElements(List<Bs3DObject> lstElements) {
		this.m_lstElements = lstElements;
	}

	/**
	 * @return the m_lstElements
	 */
	public List<Bs3DObject> getLstElements() {
		return m_lstElements;
	}

	/**
	 * @param fAngle the fAngle to set
	 */
	public void setAngle(float fAngle) {
		this.fAngle = fAngle;
	}

	/**
	 * @return the fAngle
	 */
	public float getAngle() {
		return fAngle;
	}

}
