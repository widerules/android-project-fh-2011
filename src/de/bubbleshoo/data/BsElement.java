/**
 * 
 */
package de.bubbleshoo.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @author oliverl
 *
 */
public class BsElement{
	/**
	 * vertices of element
	 */
	protected float[]			m_fCoords;
	
	/**
	 * vertices of element (important for rendering)
	 */
	protected FloatBuffer		m_fbVertexBuffer;
	
	/**
	 * vertices
	 */
	protected ByteBuffer		m_bbVerticeBuffer;
	
	/**
	 * Constructor
	 * @param fbVertexBuffer
	 */
	public BsElement(float[] fCoords) {
		// Initialise element
		initElement(fCoords);
	}
	
	/**
	 * Initialise element and buffers
	 */
	public void initElement(float[] fCoords) {
		this.m_fCoords			= fCoords;
		// * 4 because of 4 Bytes per float
		this.m_bbVerticeBuffer	= ByteBuffer.allocateDirect(this.m_fCoords.length * 4);
		// use Androids byte order
		this.m_bbVerticeBuffer.order(ByteOrder.nativeOrder());
		// Create a floatingpoint buffer from the yteBuffer
		this.m_fbVertexBuffer = this.m_bbVerticeBuffer.asFloatBuffer();
		// Add the Coordinates to the FloatBuffer
		this.m_fbVertexBuffer.put(this.m_fCoords);
		// Set the Buffer to read the first coordinate
		this.m_fbVertexBuffer.position(0);
	}
	
	/**
	 * @return the m_fbVertices
	 */
	public FloatBuffer getVertexBuffer() {
		return this.m_fbVertexBuffer;
	}
}
