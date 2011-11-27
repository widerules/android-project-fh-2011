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
import de.bubbleshoo.units.Kugel;
import de.bubbleshoo.units.NormaleKugel;
import de.bubbleshoo.units.Unit;

public class LogicThread extends Thread {

	
	private Context mContext;
	private List<Unit> m_lstUnit;
	private List<MapElement> m_lstMapEmt;
	private Map m_map;
	private TextureManager mTextureManager;

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
		loadmap() ;
		
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
				moveKugel();
				
				
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
			
			System.out.println("Thread läuft och.");
		}
		
		//Sensor auswerten
		
		
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
				unit.getSpeed()[0]=BsDataholder.getHandykipplageX();
				unit.getSpeed()[1]=BsDataholder.getHandykipplageY();
			}
		}
		
	}
	
	/** Bewegt die Kugel je nach Sensorlage
	 * 
	 */
	private void moveKugel() {
		for(Unit unit: m_lstUnit ){
			if(unit instanceof Kugel)
			{
				unit.getM_3dobject().move(unit.getSpeed()[0]/10, unit.getSpeed()[0]/10);
			}
		}
		
	}
	/** Läd die schon geladene Map (Aus der Bitdatei) in die Logic und erzeugt die Objekte.
	 * 
	 */
	private void loadmap() {

		int nY = 0;
		int nX = 0;

		for (ArrayList<Feld> row_Y : this.m_map.getFelderY()) {
			if (row_Y != null) {
				nY++;
				for (Feld col_X : row_Y) {
					if (col_X != null) {
						nX++;
						Log.d("Mapinit", "X: " + col_X.getFeldposX() + "; Y: "
								+ col_X.getFeldposY());
						if ((col_X.getFeldposX() > 0)
								&& (col_X.getFeldposY() > 0)) {

							int nTextureID;
							if (col_X.getMapElement() instanceof Baum) {
								nTextureID = R.drawable.grass_tile;
							} else if (col_X.getMapElement() instanceof Busch) {
								nTextureID = R.drawable.bush_tile;
								
								ObjParser parser = new ObjParser(
										mContext.getResources(),
										mTextureManager, R.raw.plane);
								parser.parse();
								BaseObject3D emt = parser.getParsedObject()
										.getChildByName("Plane");

								Bitmap texture = BitmapFactory.decodeResource(
										mContext.getResources(), nTextureID);
								emt.addTexture(mTextureManager.addTexture(
										texture, nTextureID));
								emt.setRotation(0, 0, 0);
								emt.setScale(1.0f);
								
								// emt.setPosition(-col_X.getFeldposX() +
								// (row_Y.size() / 2.0f), -col_X.getFeldposY() +
								// (this.m_map.getFelderY().size() / 2.0f),
								// 0.0f);
								emt.setPosition(
										2.0f * -col_X.getFeldposX()
												+ (row_Y.size()),
										2.0f
												* -col_X.getFeldposY()
												+ (this.m_map.getFelderY()
														.size()), -0.75f);
								this.m_lstMapEmt.add(new MapElement(emt));
									
							} else if (col_X.getMapElement() instanceof Felsen) {
								nTextureID = R.raw.fieldstone;
							} else if (col_X.getMapElement() instanceof Gras) {
								nTextureID = R.drawable.grass_tile;
							} else if (col_X.getMapElement() instanceof Mauer) {
								nTextureID = R.drawable.brick_tile;
								ObjParser parser = new ObjParser(
										mContext.getResources(),
										mTextureManager, R.raw.plane);
								parser.parse();
								BaseObject3D emt = parser.getParsedObject()
										.getChildByName("Plane");

								Bitmap texture = BitmapFactory.decodeResource(
										mContext.getResources(), nTextureID);
								emt.addTexture(mTextureManager.addTexture(
										texture, nTextureID));
								emt.setRotation(0, 0, 0);
								emt.setScale(1.0f);
								// emt.setPosition(-col_X.getFeldposX() +
								// (row_Y.size() / 2.0f), -col_X.getFeldposY() +
								// (this.m_map.getFelderY().size() / 2.0f),
								// 0.0f);
								emt.setPosition(
										2.0f * -col_X.getFeldposX()
												+ (row_Y.size()),
										2.0f
												* -col_X.getFeldposY()
												+ (this.m_map.getFelderY()
														.size()), -0.75f);
								this.m_lstMapEmt.add(new MapElement(emt));
							} else if (col_X.getMapElement() instanceof Oelteppich) {
								nTextureID = R.raw.diffuse;
							} else if (col_X.getMapElement() instanceof Rand) {
								nTextureID = R.drawable.grass_tile;
							} else if (col_X.getMapElement() instanceof Sand) {
								nTextureID = R.drawable.sand_tile;
							} else if (col_X.getMapElement() instanceof Stacheln) {
								nTextureID = R.drawable.grass_tile;
							} else if (col_X.getMapElement() instanceof Start) {
								nTextureID = R.drawable.heightmap1;
								ObjParser parser = new ObjParser(
										mContext.getResources(),
										mTextureManager, R.raw.sphere);
								parser.parse();
								BaseObject3D emt = parser.getParsedObject()
										.getChildByName("Sphere");

								Bitmap texture = BitmapFactory.decodeResource(
										mContext.getResources(), nTextureID);
								emt.addTexture(mTextureManager.addTexture(
										texture, nTextureID));
								emt.setRotation(0, 0, 0);
								emt.setScale(1.0f);
								// emt.setPosition(-col_X.getFeldposX() +
								// (row_Y.size() / 2.0f), -col_X.getFeldposY() +
								// (this.m_map.getFelderY().size() / 2.0f),
								// 0.0f);
								emt.setPosition(
										2.0f * -col_X.getFeldposX()
												+ (row_Y.size()),
										2.0f
												* -col_X.getFeldposY()
												+ (this.m_map.getFelderY()
														.size()), -0.75f);
								this.m_lstUnit.add(new NormaleKugel(emt));
							} else if (col_X.getMapElement() instanceof Wasser) {
								nTextureID = R.drawable.grass_tile;
							} else if (col_X.getMapElement() instanceof Weg) {
								nTextureID = R.drawable.sand_tile;
							} else if (col_X.getMapElement() instanceof Ziel) {
								nTextureID = R.drawable.grass_tile;
							} else {
								/**
								 * Default
								 */
								nTextureID = R.drawable.grass_tile;
							}
							ObjParser parser = new ObjParser(
									mContext.getResources(), mTextureManager,
									R.raw.plane);
							parser.parse();
							BaseObject3D emt = parser.getParsedObject()
									.getChildByName("Plane");

							Bitmap texture = BitmapFactory.decodeResource(
									mContext.getResources(), nTextureID);
							emt.addTexture(mTextureManager.addTexture(texture,
									nTextureID));
							emt.setRotation(0, 0, 0);
							emt.setScale(1.0f);
							// emt.setPosition(-col_X.getFeldposX() +
							// (row_Y.size() / 2.0f), -col_X.getFeldposY() +
							// (this.m_map.getFelderY().size() / 2.0f), 0.0f);
							emt.setPosition(
									2.0f * -col_X.getFeldposX()
											+ (row_Y.size()),
									2.0f * -col_X.getFeldposY()
											+ (this.m_map.getFelderY().size()),
									0.0f);

							this.m_lstMapEmt.add(new MapElement(emt));
						}
					}
				}
			}
		}
	}

	/*
	 * Getter / Setter
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

}
