package de.bubbleshoo.units;

import android.content.Context;
import de.bubbleshoo.data.BaseObject3D;
import de.bubbleshoo.data.Bs3DObject;
import de.bubbleshoo.main.BsMainActivity;

public class Unit {

	private double speedX;
	private double speedY;
	private BaseObject3D m_3dobject;
	
	
	public Unit(int[] texFile, int meshID, boolean hasTexture, Context context) 
	{
		speedX=0;
		speedY=0;
	}
	
	public Unit()
	{
		speedX=0;
		speedY=0;
	}


	public Unit(BaseObject3D emt) 
	{
		m_3dobject= emt;
		speedX=0;
		speedY=0;
	}

	/*
	 * Getter Setter
	 */
	public double getSpeedX() {
		return speedX;
	}

	public void setSpeedX(double speedX) {
		this.speedX = speedX;
	}

	public double getSpeedY() {
		return speedY;
	}

	public void setSpeedY(double speedY) {
		this.speedY = speedY;
	}
}
