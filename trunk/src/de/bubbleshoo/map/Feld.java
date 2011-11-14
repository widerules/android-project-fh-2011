package de.bubbleshoo.map;

import java.util.ArrayList;
import java.util.List;

import de.bubbleshoo.mapElemente.MapElement;
import de.bubbleshoo.units.Enemy;

public class Feld {

	/*
	 * Attribute
	 */
	private int 	feldhoehe=0;
		
	private MapElement mapElement;
	
	private List<Enemy> enemys=new ArrayList<Enemy>();
	private List<Enemy> vips=new ArrayList<Enemy>();

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
	public List<Enemy> getEnemys() {
		return enemys;
	}
	public void setEnemys(List<Enemy> enemys) {
		this.enemys = enemys;
	}
	public List<Enemy> getVips() {
		return vips;
	}
	public void setVips(List<Enemy> vips) {
		this.vips = vips;
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
	
	
	
}
