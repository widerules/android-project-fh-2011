/**
 * 
 */
package de.bubbleshoo.graphics;

import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import de.bubbleshoo.data.BsElement;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

/**
 * @author oliverl
 *
 */
public class BsRenderer implements GLSurfaceView.Renderer{

	/**
	 * Programm for Vertex- and Fragmentshader
	 */
	private int mProgram;
	
	/**
	 * Position for Elements manipulated by shader
	 */
    private int maPositionHandle;
    
	/**
	 * Elements to draw
	 */
	private List<BsElement>	m_lstElements;
	
	/**
	 *  Vertex shader
	 */
	private final String 	m_vertexShaderCode = 
        "attribute vec4 vPosition; \n" +
        "void main(){              \n" +
        " gl_Position = vPosition; \n" +
        "}                         \n";
    
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
	@Override
	public void onDrawFrame(GL10 gl) {
		// Redraw background Color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
        for (BsElement bsEmt : this.m_lstElements) {
        	// Add program to OpenGL environment
            GLES20.glUseProgram(this.mProgram);
        	// Prepare the triangle data
            GLES20.glVertexAttribPointer(this.maPositionHandle, 3, GLES20.GL_FLOAT, false, 12, bsEmt.getVertexBuffer());
            GLES20.glEnableVertexAttribArray(this.maPositionHandle);
            
            // Draw the triangle
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
		}
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
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, this.m_vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, this.m_fragmentShaderCode);

		// Set the background frame color
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		
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
	public void setLstElements(List<BsElement> lstElements) {
		this.m_lstElements = lstElements;
	}

	/**
	 * @return the m_lstElements
	 */
	public List<BsElement> getLstElements() {
		return m_lstElements;
	}

}
