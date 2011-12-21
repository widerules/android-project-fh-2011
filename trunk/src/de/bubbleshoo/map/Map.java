package de.bubbleshoo.map;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import de.bubbleshoo.data.BaseObject3D;
import de.bubbleshoo.logic.LogicThread;
import de.bubbleshoo.units.Kugel;
import de.bubbleshoo.units.NormaleKugel;

public class Map {

	/*
	 * Attribute
	 */
	private List<ArrayList<Feld>> felderY = new ArrayList<ArrayList<Feld>>();

	private Queue<Kugel> kugeln = new LinkedList<Kugel>();

	public Map() {
		
	}



	/*
	 * Konstruktoren
	 */

	/*
	 * Methoden
	 */

	/*
	 * Getter /Setter
	 */

	public List<ArrayList<Feld>> getFelderY() {
		return felderY;
	}

	public void setFelderY(List<ArrayList<Feld>> felderY) {
		this.felderY = felderY;
	}

	public Queue<Kugel> getKugeln() {
		return kugeln;
	}

	public void setKugeln(Queue<Kugel> kugeln) {
		this.kugeln = kugeln;
	}

}
