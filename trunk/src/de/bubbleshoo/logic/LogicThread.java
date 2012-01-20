package de.bubbleshoo.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import de.bubbleshoo.data.BaseObject3D;
import de.bubbleshoo.data.BsParser;
import de.bubbleshoo.data.ObjParser;
import de.bubbleshoo.graphics.TextureManager;
import de.bubbleshoo.logic.kugel.LogikExplosionsKugel;
import de.bubbleshoo.main.BsMainActivity;
import de.bubbleshoo.main.R;
import de.bubbleshoo.map.Feld;
import de.bubbleshoo.map.Map;
import de.bubbleshoo.map.MapLoader;
import de.bubbleshoo.mapElemente.Baum;
import de.bubbleshoo.mapElemente.Busch;
import de.bubbleshoo.mapElemente.Felsen;
import de.bubbleshoo.mapElemente.Gras;
import de.bubbleshoo.mapElemente.MapElement;
import de.bubbleshoo.mapElemente.Mauer;
import de.bubbleshoo.mapElemente.Oelteppich;
import de.bubbleshoo.mapElemente.Rand;
import de.bubbleshoo.mapElemente.Sand;
import de.bubbleshoo.mapElemente.Stacheln;
import de.bubbleshoo.mapElemente.Start;
import de.bubbleshoo.mapElemente.Wasser;
import de.bubbleshoo.mapElemente.Weg;
import de.bubbleshoo.mapElemente.Ziel;
import de.bubbleshoo.sensors.BsDataholder;
import de.bubbleshoo.settings.GeneralSettings;
import de.bubbleshoo.units.ExplosionsKugel;
import de.bubbleshoo.units.Kugel;
import de.bubbleshoo.units.NormaleKugel;
import de.bubbleshoo.units.Unit;

public class LogicThread extends Thread {
	/**
	 * Holderklasse für die Objekte
	 */
	protected BsParser		m_parser;
	
	public static Context mContext;
	public static List<Unit> m_lstUnit;
	public static List<MapElement> m_lstMapEmt;
	public static Map m_map;
	public static TextureManager mTextureManager;

	//Logische Konstanten:
	private static double decreaseGras=-1.0f;
	private static double decreaseFelsen=-1.0f;
	private static double decreaseOel=1.0f;
	private static double decreaseWasser=-1.0f;
	
	
	public LogicThread(Context context, TextureManager textureManager) {
		m_lstMapEmt 			= Collections.synchronizedList(new ArrayList<MapElement>());
		m_lstUnit 				= Collections.synchronizedList(new ArrayList<Unit>());
		this.mContext 			= context;
		this.mTextureManager 	= textureManager;
		
		// Map laden
		try {
			m_map = MapLoader.laodMap("map1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Objectparser laden
		m_parser				= new BsParser(this.mContext, this.mTextureManager);
				
		LogikMapLaden.loadmap(m_parser) ;
		LogicThread.m_lstUnit.add(LogicThread.m_map.getKugeln().poll());
	}

	/*
	 * Main Run Methode,die (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		
		//Solange die app
		while(BsMainActivity.m_appzustand!=BsMainActivity.ONEXIT)
		{
			while(BsMainActivity.m_appzustand==BsMainActivity.ONCREATE||BsMainActivity.m_appzustand==BsMainActivity.ONRESUME)
			{
//				System.out.println("Hier!");
				//Geschwindigkeit aus Sensoren laden
				
				setSpeedOfUnits();
				allekugelnbewegen();
				checkKugelposition();
				
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
			// Wenn Pause soll er nix machen:
			while(BsMainActivity.m_appzustand==BsMainActivity.ONPAUSE)
			{
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Log.d(GeneralSettings.LoggerKategorie,"Thread läuft och.");
		}
		
		//Sensor auswerten
	}

	/** Methode sagt auf welchen Feld sie gerade ist
	 * 
	 */
	private void checkKugelposition() {
		boolean zielangekommen=false;
		boolean mauerAngekommen=false;
		for(Unit unit: m_lstUnit ){
//			System.out.println("Unit1"+unit.getClass());
			//Alle KugelN
			if(unit.isM_isShown())
				for(MapElement mapelement: m_lstMapEmt )
				{
						if(unit!=null)
							if((unit.getM_3dobject().getX()>=(mapelement.getM_3dobject().getX()-1.0f))
							 &&(unit.getM_3dobject().getX()<=(mapelement.getM_3dobject().getX()+1.0f)))
							{
//								System.out.println("1Kugelbefindet sich im feld: "+mapelement.getM_3dobject().getClass()+" Pos:"+mapelement.getM_3dobject().getX()+":"+mapelement.getM_3dobject().getY());
								if(((unit.getM_3dobject().getY())>=(mapelement.getM_3dobject().getY()-1.0f)))
								{		
									if(((unit.getM_3dobject().getY())<=(mapelement.getM_3dobject().getY()+1.0f)))
									{
										Log.d(GeneralSettings.LoggerKategorie,"Kugelbefindet sich im feld: "+mapelement.getClass()+" Pos:"+mapelement.getM_3dobject().getX()+":"+mapelement.getM_3dobject().getY());
										
										//Was passiert im Ziel
										if(mapelement instanceof Ziel)
										{
//											System.out.println(LogicThread.m_lstUnit.get(0));
											zielangekommen=true;
//											LogicThread.m_lstUnit.remove(0);
//											System.out.println(LogicThread.m_lstUnit.get(0));
											unit.setM_isShown(false);
										}
										//Was passiert auf Öl
										if(mapelement instanceof Oelteppich)
										{
											veraendereUnitSpeed(unit,decreaseOel);
										}
										//Was passiert auf Felsen
										if(mapelement instanceof Felsen)
										{
											veraendereUnitSpeed(unit,decreaseFelsen);	
										}
										//Was passiert auf Baum
										if(mapelement instanceof Baum)
										{
													
										}
										//Was passiert auf Busch
										if(mapelement instanceof Busch)
										{
														
										}
										//Was passiert auf Gras
										if(mapelement instanceof Gras)
										{
											veraendereUnitSpeed(unit,decreaseGras);	
										}
										//Was passiert auf Mauer (Muss kaputte Mauer sein)
										if(mapelement instanceof Mauer)
										{
//											mauerAngekommen=true;
//											unit.setM_isShown(false);
										}
										//Was passiert auf Sand
										if(mapelement instanceof Sand)
										{
											veraendereUnitSpeed(unit,decreaseFelsen);			
										}
										//Was passiert auf Stacheln
										if(mapelement instanceof Stacheln)
										{
														
										}
										//Was passiert auf Wasser
										if(mapelement instanceof Wasser)
										{
											veraendereUnitSpeed(unit,decreaseWasser);			
										}
										//Was passiert auf Weg
										if(mapelement instanceof Weg)
										{
													
										}
									}
								}
							}
				}
				
				//Was kann die Normale Kugel:
				if(unit instanceof NormaleKugel)
				{
					
				}
		}
		if(zielangekommen)
		{
			Unit kugelvomstart = LogicThread.m_map.getKugeln().poll();
			kugelvomstart.setM_isShown(true);
			if(kugelvomstart!=null)
					LogicThread.m_lstUnit.add(kugelvomstart);
		}
		if(mauerAngekommen)
		{
			Unit kugelvomstart = LogicThread.m_map.getKugeln().poll();
			kugelvomstart.setM_isShown(true);
			if(kugelvomstart!=null)
					LogicThread.m_lstUnit.add(kugelvomstart);
		}
	}

	/** Setzt die aktuelle Geschwindigkeit der Kugel
	 * 
	 */
	private void setSpeedOfUnits()
	{
		for(Unit unit: m_lstUnit ){
//			System.out.println("m_lstUnit");
			if(unit instanceof NormaleKugel)
			{
				setSpeedNormaleKugel(unit);
			}
			if(unit instanceof ExplosionsKugel)
			{
				setSpeedExplosionsKugel(unit);
								
			}
		}
		
	}
	
	/*
	 * Eine Kugel geht weg und eine neue kommt rein
	 */
	private static void generateNewKugel() {
		
		Unit kugelvomstart = LogicThread.m_map.getKugeln().poll();
		kugelvomstart.setM_isShown(true);
		if(kugelvomstart!=null)
				LogicThread.m_lstUnit.add(kugelvomstart);
	}
	
	/**
	 *  Setzt den Speed von der Normale Kugel
	 *  
	 */
	private void setSpeedNormaleKugel(Unit unit)
	{
		unit.getSpeed()[0]=-1*BsDataholder.getHandykipplageX()/unit.getGeschwindigkeitsverhalten();
		unit.getSpeed()[1]=-1*BsDataholder.getHandykipplageY()/unit.getGeschwindigkeitsverhalten();
	}
	
	/**
	 *  Setzt den Speed von der Explosions Kugel
	 *  
	 */
	private void setSpeedExplosionsKugel(Unit unit)
	{
		unit.getSpeed()[0]=-1*BsDataholder.getHandykipplageX()/unit.getGeschwindigkeitsverhalten();
		unit.getSpeed()[1]=-1*BsDataholder.getHandykipplageY()/unit.getGeschwindigkeitsverhalten();
	}
	
	/**
	 * Verändert den Speed einer Unit um einen gewissen Wert
	 * @param unit
	 * @param decreasevalue: Der Wert um den die Unit erhoeht verniedrigt werden soll.
	 */
	private void veraendereUnitSpeed(Unit unit, double decreasevalue)
	{
		unit.getSpeed()[0]+=decreasevalue;
		unit.getSpeed()[1]+=decreasevalue;

	}
	
	/** Bewegt die Kugel je nach Sensorlage
	 * 
	 */
	private void allekugelnbewegen() {
		boolean newKugel=false;
		for(Unit unit: m_lstUnit ){
			/*
			 * Normale Kugel bewegen
			 */
			if(unit instanceof NormaleKugel)
			{
				//Kollisionsabfrage Wand
				if(Kollision.checkForWallKollisions(unit,m_lstMapEmt))
				{
					
				}
				else //Ansonsten normal bewegen
				{
					unit.getM_3dobject().move(unit.getSpeed()[0]/10, unit.getSpeed()[1]/10);
				}
			}
			/*
			 * Explosionskugel bewegen
			 */
			if(unit instanceof ExplosionsKugel)
			{
				//Kollisionsabfrage Wand
				if(LogikExplosionsKugel.checkForWallKollisions(unit,m_lstMapEmt))
				{
//					unit.setM_isShown(false);
//					newKugel=true;
				}
				else //Ansonsten normal bewegen
				{
					unit.getM_3dobject().move(unit.getSpeed()[0]/10, unit.getSpeed()[1]/10);
				}
			}
			
			
		}
//		if(newKugel)
//		generateNewKugel();
		
	}
	
	/*
	 * Getter / Setter
	 */

	/**
	 * Mit dierser Getter kann man die Units erhalten.
	 * @return returned die unit
	 */
	public List<Unit> getM_lstUnit() {
		return m_lstUnit;
	}

	public void setM_lstUnit(List<Unit> m_lstUnit) {
		this.m_lstUnit = m_lstUnit;
	}

	public List<MapElement> getM_lstMapEmt() {
		return m_lstMapEmt;
	}

	public void setM_lstMapEmt(List<MapElement> m_lstMapEmt) {
		this.m_lstMapEmt = m_lstMapEmt;
	}

	public Map getM_map() {
		return m_map;
	}

	public void setM_map(Map m_map) {
		this.m_map = m_map;
	}

	public static Context getmContext() {
		return mContext;
	}

	public static void setmContext(Context mContext) {
		LogicThread.mContext = mContext;
	}

	public static TextureManager getmTextureManager() {
		return mTextureManager;
	}

	public static void setmTextureManager(TextureManager mTextureManager) {
		LogicThread.mTextureManager = mTextureManager;
	}

	
}
