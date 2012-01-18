package de.bubbleshoo.camera;

import de.bubbleshoo.data.BaseObject3D;
import android.opengl.Matrix;

public class Camera3D {
	/**
	 * @uml.property  name="x"
	 */
	protected float x;
	/**
	 * @uml.property  name="y"
	 */
	protected float y;
	/**
	 * @uml.property  name="z"
	 */
	protected float z;
	/**
	 * @uml.property  name="lookAtX"
	 */
	protected float lookAtX;
	/**
	 * @uml.property  name="lookAtY"
	 */
	protected float lookAtY;
	/**
	 * @uml.property  name="lookAtZ"
	 */
	protected float lookAtZ;
	/**
	 * @uml.property  name="mVMatrix" multiplicity="(0 -1)" dimension="1"
	 */
	protected float[] mVMatrix = new float[16];
	
	public Camera3D(){}
	
	public float[] getViewMatrix() {
		 Matrix.setLookAtM(mVMatrix, 0, x, y, z, lookAtX, lookAtY, lookAtZ, 0f, 1.0f, 0.0f);
		 return mVMatrix;
	}
	
	public void setPosition(float x, float y, float z) {
		this.x = x; this.y = y; this.z = z;
	}
	
	public void setLookAt(float lookAtX, float lookAtY, float lookAtZ) {
		this.lookAtX = lookAtX;
		this.lookAtY = lookAtY;
		this.lookAtZ = lookAtZ;
	}
	
	public void lookAt(BaseObject3D lookatObject) {
		this.lookAtX = lookatObject.getX();
		this.lookAtY = lookatObject.getY();
		this.lookAtZ = lookatObject.getZ();
	}

	/**
	 * @return
	 * @uml.property  name="x"
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x
	 * @uml.property  name="x"
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @return
	 * @uml.property  name="y"
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param y
	 * @uml.property  name="y"
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * @return
	 * @uml.property  name="z"
	 */
	public float getZ() {
		return z;
	}

	/**
	 * @param z
	 * @uml.property  name="z"
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * @return
	 * @uml.property  name="lookAtX"
	 */
	public float getLookAtX() {
		return lookAtX;
	}

	/**
	 * @param lookAtX
	 * @uml.property  name="lookAtX"
	 */
	public void setLookAtX(float lookAtX) {
		this.lookAtX = lookAtX;
	}

	/**
	 * @return
	 * @uml.property  name="lookAtY"
	 */
	public float getLookAtY() {
		return lookAtY;
	}

	/**
	 * @param lookAtY
	 * @uml.property  name="lookAtY"
	 */
	public void setLookAtY(float lookAtY) {
		this.lookAtY = lookAtY;
	}

	/**
	 * @return
	 * @uml.property  name="lookAtZ"
	 */
	public float getLookAtZ() {
		return lookAtZ;
	}

	/**
	 * @param lookAtZ
	 * @uml.property  name="lookAtZ"
	 */
	public void setLookAtZ(float lookAtZ) {
		this.lookAtZ = lookAtZ;
	}
}
