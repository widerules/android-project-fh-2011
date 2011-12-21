package de.bubbleshoo.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import de.bubbleshoo.data.BaseObject3D;
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

	
	public static Context mContext;
	public static List<Unit> m_lstUnit;
	public static List<MapElement> m_lstMapEmt;
	public static Map m_map;
	public static TextureManager mTextureManager;

	public LogicThread(Context context, TextureManager textureManager) {
		m_lstMapEmt = Collections.synchronizedList(new ArrayList<MapElement>());
		m_lstUnit = Collections.synchronizedList(new ArrayList<Unit>());
		this.mContext=context;
		this.mTextureManager=textureManager;
		
		
		// Map laden
		try {
			m_map = MapLoader.laodMap("map1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogikMapLaden.loadmap() ;
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
		for(Unit unit: m_lstUnit ){
//			System.out.println("Unit1"+unit.getClass());
			//Alle KugelN
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
										if(mapelement instanceof Ziel)
										{
											System.out.println(LogicThread.m_lstUnit.get(0));
											LogicThread.m_lstUnit.remove(0);
//											System.out.println(LogicThread.m_lstUnit.get(0));
											Unit kugelvomstart = LogicThread.m_map.getKugeln().poll();
											
											if(kugelvomstart!=null)
													LogicThread.m_lstUnit.add(kugelvomstart);
											
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
//				System.out.println("Set");
					unit.getSpeed()[0]=-1*BsDataholder.getHandykipplageX()/unit.getGeschwindigkeitsverhalten();
					unit.getSpeed()[1]=-1*BsDataholder.getHandykipplageY()/unit.getGeschwindigkeitsverhalten();
			}
			if(unit instanceof ExplosionsKugel)
			{
//				System.out.println("Set");
					unit.getSpeed()[0]=-1*BsDataholder.getHandykipplageX()/unit.getGeschwindigkeitsverhalten();
					unit.getSpeed()[1]=-1*BsDataholder.getHandykipplageY()/unit.getGeschwindigkeitsverhalten();
			}
		}
		
	}
	
	/** Bewegt die Kugel je nach Sensorlage
	 * 
	 */
	private void allekugelnbewegen() {
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
					
				}
				else //Ansonsten normal bewegen
				{
					unit.getM_3dobject().move(unit.getSpeed()[0]/10, unit.getSpeed()[1]/10);
				}
			}
			
			
		}
		
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
