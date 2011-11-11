/**
 * 
 */
package de.bubbleshoo.camera;

import android.opengl.Matrix;

/**
 * @author Hans
 *
 */
public class BsCamera {
	/**
	 * Parameters
	 */
	private float[]	m_mVMatrix; 	// returns the result 
	private int 	m_rmOffset; 	// index into rm where the result matrix starts 
	private float 	m_eyeX;  		// eye point X 
	private float 	m_eyeY;  		// eye point Y 
	private float 	m_eyeZ;  		// eye point Z 
	private float 	m_centerX;  	// center of view X 
	private float 	m_centerY; 	 	// center of view Y 
	private float 	m_centerZ;  	// center of view Z 
	private float 	m_upX;  		// up vector X 
	private float 	m_upY;  		// up vector Y 
	private float 	m_upZ;  		// up vector Z  

	/**
	 * Konstruktor
	 * @param mVMatrix
	 * @param rmOffset
	 * @param eyeX
	 * @param eyeY
	 * @param eyeZ
	 * @param centerX
	 * @param centerY
	 * @param centerZ
	 * @param upX
	 * @param upY
	 * @param upZ
	 */
	public BsCamera(float[] mVMatrix, int rmOffset, float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ,
			float upX, float upY, float upZ) {
		this.m_mVMatrix	= mVMatrix;
		this.m_rmOffset = rmOffset;
		this.m_eyeX 	= eyeX; 
		this.m_eyeY 	= eyeY;
		this.m_eyeZ 	= eyeZ;
		this.m_centerX 	= centerX;
		this.m_centerY 	= centerY;
		this.m_centerZ 	= centerZ;
		this.m_upX 		= upX;
		this.m_upY 		= upY;
		this.m_upZ 		= upZ;
	}
	
	/**
	 * set the view to a specified point
	 * @param mVMatrix
	 * @param rmOffset
	 * @param eyeX
	 * @param eyeY
	 * @param eyeZ
	 * @param centerX
	 * @param centerY
	 * @param centerZ
	 * @param upX
	 * @param upY
	 * @param upZ
	 */
	public float[] setLookAtM(float[] mVMatrix, int rmOffset, float eyeX, float eyeY, float eyeZ,
			float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
		this.m_mVMatrix	= mVMatrix;
		this.m_rmOffset = rmOffset;
		this.m_eyeX 	= eyeX; 
		this.m_eyeY 	= eyeY;
		this.m_eyeZ 	= eyeZ;
		this.m_centerX 	= centerX;
		this.m_centerY 	= centerY;
		this.m_centerZ 	= centerZ;
		this.m_upX 		= upX;
		this.m_upY 		= upY;
		this.m_upZ 		= upZ;
		
		Matrix.setLookAtM(m_mVMatrix, 0, m_eyeX, m_eyeY, m_eyeZ, m_centerX, m_centerY, m_centerZ, m_upX, m_upX, m_upZ);
		
		return m_mVMatrix;
	}
}
