package de.bubbleshoo.units;

import android.content.Context;
import de.bubbleshoo.data.Bs3DObject;
import de.bubbleshoo.main.BsMainActivity;

public class Unit extends Bs3DObject {

	public Unit(int[] texFile, int meshID, boolean hasTexture, Context context) {
		super(texFile, meshID, hasTexture, context);
		// TODO Auto-generated constructor stub
	}
	
	public Unit()
	{
		super(1,true,BsMainActivity.getAnwendungsContex());
	}
}
