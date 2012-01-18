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
	/**
	 * @uml.property  name="feldhoehe"
	 */
	private int 	feldhoehe=0;
		
	/**
	 * @uml.property  name="mapElement"
	 * @uml.associationEnd  
	 */
	private MapElement mapElement;
	
	/**
	 * @uml.property  name="units"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="de.bubbleshoo.units.Unit"
	 */
	private List<Unit> units=new ArrayList<Unit>();


	/**
	 * @uml.property  name="feldname"
	 */
	private String feldname="";
	/**
	 * @uml.property  name="feldposX"
	 */
	private int 	feldposX;
	/**
	 * @uml.property  name="feldposY"
	 */
	private int		feldposY;
	/**
	 * @uml.property  name="feldlaenge"
	 */
	private int 	feldlaenge;
	/**
	 * @uml.property  name="feldbreite"
	 */
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
	/**
	 * @return
	 * @uml.property  name="feldhoehe"
	 */
	public int getFeldhoehe() {
		return feldhoehe;
	}
	/**
	 * @param feldhoehe
	 * @uml.property  name="feldhoehe"
	 */
	public void setFeldhoehe(int feldhoehe) {
		this.feldhoehe = feldhoehe;
	}
	/**
	 * @return
	 * @uml.property  name="mapElement"
	 */
	public MapElement getMapElement() {
		return mapElement;
	}
	/**
	 * @param mapElement
	 * @uml.property  name="mapElement"
	 */
	public void setMapElement(MapElement mapElement) {
		this.mapElement = mapElement;
	}
	/**
	 * @return
	 * @uml.property  name="feldname"
	 */
	public String getFeldname() {
		return feldname;
	}
	/**
	 * @param feldname
	 * @uml.property  name="feldname"
	 */
	public void setFeldname(String feldname) {
		this.feldname = feldname;
	}
	/**
	 * @return
	 * @uml.property  name="feldposX"
	 */
	public int getFeldposX() {
		return feldposX;
	}
	/**
	 * @param feldposX
	 * @uml.property  name="feldposX"
	 */
	public void setFeldposX(int feldposX) {
		this.feldposX = feldposX;
	}
	/**
	 * @return
	 * @uml.property  name="feldposY"
	 */
	public int getFeldposY() {
		return feldposY;
	}
	/**
	 * @param feldposY
	 * @uml.property  name="feldposY"
	 */
	public void setFeldposY(int feldposY) {
		this.feldposY = feldposY;
	}
	/**
	 * @return
	 * @uml.property  name="feldlaenge"
	 */
	public int getFeldlaenge() {
		return feldlaenge;
	}
	/**
	 * @param feldlaenge
	 * @uml.property  name="feldlaenge"
	 */
	public void setFeldlaenge(int feldlaenge) {
		this.feldlaenge = feldlaenge;
	}
	/**
	 * @return
	 * @uml.property  name="feldbreite"
	 */
	public int getFeldbreite() {
		return feldbreite;
	}
	/**
	 * @param feldbreite
	 * @uml.property  name="feldbreite"
	 */
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
