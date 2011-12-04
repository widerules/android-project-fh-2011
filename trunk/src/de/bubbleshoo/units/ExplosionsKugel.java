package de.bubbleshoo.units;

import de.bubbleshoo.data.BaseObject3D;

/** Explodiert wenn er mit einer mauer kollidiert
 * 
 * @author Jörn Bettermann
 *
 */
public class ExplosionsKugel extends Kugel{
	
	
	
	/*
	 * Konstruktor
	 */
	public ExplosionsKugel(BaseObject3D emt) {
		super(emt);
		setGeschwindigkeitsverhalten(5.0f);
	}
	

}
