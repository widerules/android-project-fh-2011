package de.bubbleshoo.units;

import de.bubbleshoo.data.BaseObject3D;

/** Explodiert wenn er mit einer mauer kollidiert
 * 
 * @author J�rn Bettermann
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
