package de.bubbleshoo.units;

import android.content.Context;
import de.bubbleshoo.data.BaseObject3D;
import de.bubbleshoo.data.Bs3DObject;
import de.bubbleshoo.main.BsMainActivity;

public class Unit {

	/**
	 * @uml.property  name="speed"
	 */
	private float[] speed 	= new float[2];
	
	/**
	 * Alles was gr��er ist als 1 ist schneller und kleiner wird langsamer...
	 * @uml.property  name="geschwindigkeitsverhalten"
	 */
	private float geschwindigkeitsverhalten=1;
	
	
	/**
	 * @uml.property  name="m_3dobject"
	 * @uml.associationEnd  
	 */
	private BaseObject3D m_3dobject;
	private boolean m_isShown=false;
	
	
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
	/**
	 * Liefert die Geschwindigkeit des Objektes in X und Y Richtung. speed[0] ist die Geschwindikeit in X Richtung; speed[1] ist die Geschwindikeit in Y Richtung; Wertebereich: 10 bis -10, wobei 0 keine Bewegung bedeutet.  X -10 bis 10. -10 steht f�r maximale Bewegung zur Negativen x Richtung (Nach links ^^). +10 Max nach rechts. Y (das selbe wie bei X)  -10 bis 10. -10 steht f�r maximale Bewegung zur Negativen y Richtung (Nach unten ^^) +10 max geschwindigkeit nach oben
	 * @return   die Geschwindigkeit in x und y Richtung
	 * @uml.property  name="speed"
	 */
	public float[] getSpeed() {
		return speed;
	}

	/**
	 * @param speed
	 * @uml.property  name="speed"
	 */
	public void setSpeed(float[] speed) {
		this.speed = speed;
	}

	/**
	 * @return
	 * @uml.property  name="m_3dobject"
	 */
	public BaseObject3D getM_3dobject() {
		return m_3dobject;
	}

	/**
	 * @param m_3dobject
	 * @uml.property  name="m_3dobject"
	 */
	public void setM_3dobject(BaseObject3D m_3dobject) {
		this.m_3dobject = m_3dobject;
	}

	/**
	 * @return
	 * @uml.property  name="geschwindigkeitsverhalten"
	 */
	public float getGeschwindigkeitsverhalten() {
		return geschwindigkeitsverhalten;
	}

	/**
	 * @param geschwindigkeitsverhalten
	 * @uml.property  name="geschwindigkeitsverhalten"
	 */
	public void setGeschwindigkeitsverhalten(float geschwindigkeitsverhalten) {
		this.geschwindigkeitsverhalten = geschwindigkeitsverhalten;
	}

	public boolean isM_isShown() {
		return m_isShown;
	}

	public void setM_isShown(boolean m_isShown) {
		this.m_isShown = m_isShown;
	}
	
	

}
