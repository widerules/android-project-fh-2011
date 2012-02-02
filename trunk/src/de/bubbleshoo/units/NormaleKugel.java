package de.bubbleshoo.units;

import de.bubbleshoo.data.BaseObject3D;

public class NormaleKugel extends Kugel{

	public NormaleKugel(BaseObject3D emt) {
		super(emt);
		setGeschwindigkeitsverhalten(1);
		this.setM_isShown(true);
	}

	
	
}
