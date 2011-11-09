package de.bubbleshoo.map;

import java.util.ArrayList;
import java.util.List;

public class Map {

	/*
	 * Attribute
	 */
	private List<ArrayList<Feld>> gamemap = new ArrayList<ArrayList<Feld>>();
	

	
	/*
	 * Konstruktoren
	 */
	
	
	/*
	 * Methoden
	 */
	
	
	
	/*
	 * Getter /Setter
	 */
	
	public List<ArrayList<Feld>> getGamemap() {
		return gamemap;
	}

	public void setGamemap(List<ArrayList<Feld>> gamemap) {
		this.gamemap = gamemap;
	}
	
}
