package de.bubbleshoo.units;

import android.content.Context;
import de.bubbleshoo.data.Bs3DObject;
import de.bubbleshoo.main.BsMainActivity;

public class Unit extends Bs3DObject {

	private double speedX;
	private double speedY;
	
	public Unit(int[] texFile, int meshID, boolean hasTexture, Context context) {
		super(texFile, meshID, hasTexture, context);
		speedX=0;
		speedY=0;
	}
	
	public Unit()
	{
		
		super(1,true,BsMainActivity.getAnwendungsContex());
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
