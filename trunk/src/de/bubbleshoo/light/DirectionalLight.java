package de.bubbleshoo.light;

public class DirectionalLight extends ALight {
	/**
	 * @uml.property  name="mDirection" multiplicity="(0 -1)" dimension="1"
	 */
	protected float[] mDirection = new float[3];
	
	public DirectionalLight() {
		super();
		setDirection(0, -1.0f, 1.0f);
	}
	
	public DirectionalLight(float xDir, float yDir, float zDir) {
		super();
		setDirection(xDir, yDir, zDir);
	}
	
	public void setDirection(float x, float y, float z) {
		mDirection[0] = x; mDirection[1] = y; mDirection[2] = z;
	}
	
	public float[] getDirection() {
		return mDirection;
	}
}
