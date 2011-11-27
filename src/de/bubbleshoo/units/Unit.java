package de.bubbleshoo.units;

import android.content.Context;
import de.bubbleshoo.data.BaseObject3D;
import de.bubbleshoo.data.Bs3DObject;
import de.bubbleshoo.main.BsMainActivity;

public class Unit {

	private float[] speed 	= new float[2];

	private BaseObject3D m_3dobject;
	
	
	public Unit(int[] texFile, int meshID, boolean hasTexture, Context context) 
	{
		
		speed[0]=0;
		speed[1]=0;
	}
	
	public Unit()
	{
		speed[0]=0;
		speed[1]=0;
	}


	public Unit(BaseObject3D emt) 
	{
		m_3dobject= emt;
		speed[0]=0;
		speed[1]=0;
	}


	/*
	 * Getter Setter
	 */
	/** Liefert die Geschwindigkeit des Objektes in X und Y Richtung.
	 * 	speed[0] ist die Geschwindikeit in X Richtung;
		speed[1] ist die Geschwindikeit in Y Richtung;
		Wertebereich: 10 bis -10, wobei 0 keine Bewegung bedeutet. 
		X -10 bis 10. -10 steht für maximale Bewegung zur Negativen x Richtung (Nach links ^^). +10 Max nach rechts.
		Y (das selbe wie bei X)  -10 bis 10. -10 steht für maximale Bewegung zur Negativen y Richtung (Nach unten ^^) +10 max geschwindigkeit nach oben
	 * @return  die Geschwindigkeit in x und y Richtung
	 */
	public float[] getSpeed() {
		return speed;
	}

	public void setSpeed(float[] speed) {
		this.speed = speed;
	}

	public BaseObject3D getM_3dobject() {
		return m_3dobject;
	}

	public void setM_3dobject(BaseObject3D m_3dobject) {
		this.m_3dobject = m_3dobject;
	}

}
