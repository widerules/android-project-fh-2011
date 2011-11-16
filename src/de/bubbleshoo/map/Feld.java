package de.bubbleshoo.map;

import java.util.ArrayList;
import java.util.List;

import de.bubbleshoo.mapElemente.MapElement;
import de.bubbleshoo.units.Enemy;
import de.bubbleshoo.units.Unit;

public class Feld {

	/*
	 * Attribute
	 */
	private int 	feldhoehe=0;
		
	private MapElement mapElement;
	
	private List<Unit> units=new ArrayList<Unit>();


	private String feldname="";
	private int 	feldposX;
	private int		feldposY;
	private int 	feldlaenge;
	private int 	feldbreite;

	/*
	 * Konstruktoren
	 */
	public Feld(String name)
	{
		this.feldname=name;
	}
	
	
	
	/*
	 * Getter Setter
	 */
	public int getFeldhoehe() {
		return feldhoehe;
	}
	public void setFeldhoehe(int feldhoehe) {
		this.feldhoehe = feldhoehe;
	}
	public MapElement getMapElement() {
		return mapElement;
	}
	public void setMapElement(MapElement mapElement) {
		this.mapElement = mapElement;
	}
	public String getFeldname() {
		return feldname;
	}
	public void setFeldname(String feldname) {
		this.feldname = feldname;
	}
	public int getFeldposX() {
		return feldposX;
	}
	public void setFeldposX(int feldposX) {
		this.feldposX = feldposX;
	}
	public int getFeldposY() {
		return feldposY;
	}
	public void setFeldposY(int feldposY) {
		this.feldposY = feldposY;
	}
	public int getFeldlaenge() {
		return feldlaenge;
	}
	public void setFeldlaenge(int feldlaenge) {
		this.feldlaenge = feldlaenge;
	}
	public int getFeldbreite() {
		return feldbreite;
	}
	public void setFeldbreite(int feldbreite) {
		this.feldbreite = feldbreite;
	}
	public void addEnemy(Unit temp) {
		units.add(temp);
	}
	public Unit getFirstUnit() {
		if(units.size()>0)
			return units.get(0);
		return null;
	}

	public List<Unit> getUnits() {
		return units;
	}


	public void setUnits(List<Unit> units) {
		this.units = units;
	}
	
	
	
}
