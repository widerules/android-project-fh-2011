package de.bubbleshoo.data;

import java.io.Serializable;

public class SerializedObject3D implements Serializable {
	private static final long serialVersionUID = 5264861128471177349L;

	/**
	 * @uml.property  name="mVertices" multiplicity="(0 -1)" dimension="1"
	 */
	protected float[] mVertices;
	/**
	 * @uml.property  name="mNormals" multiplicity="(0 -1)" dimension="1"
	 */
	protected float[] mNormals;
	/**
	 * @uml.property  name="mTextureCoords" multiplicity="(0 -1)" dimension="1"
	 */
	protected float[] mTextureCoords;
	/**
	 * @uml.property  name="mIndices" multiplicity="(0 -1)" dimension="1"
	 */
	protected short[] mIndices;
	
	public SerializedObject3D(int numVertices, int numNormals, int numTextureCoords, int numIndices) {
		mVertices = new float[numVertices];
		mNormals = new float[numNormals];
		mTextureCoords = new float[numTextureCoords];
		mIndices = new short[numIndices];
	}
	
	public float[] getVertices() {
		return mVertices;
	}
	public void setVertices(float[] vertices) {
		this.mVertices = vertices;
	}
	public float[] getNormals() {
		return mNormals;
	}
	public void setNormals(float[] normals) {
		this.mNormals = normals;
	}
	public float[] getTextureCoords() {
		return mTextureCoords;
	}
	public void setTextureCoords(float[] textureCoords) {
		this.mTextureCoords = textureCoords;
	}
	public short[] getIndices() {
		return mIndices;
	}
	public void setIndices(short[] indices) {
		this.mIndices = indices;
	}
}
