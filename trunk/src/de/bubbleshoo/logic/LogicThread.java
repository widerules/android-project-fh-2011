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
import de.bubbleshoo.settings.GeneralSettings;
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
				woIstKugel();
				
				
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
	private void woIstKugel() {
		for(Unit unit: m_lstUnit ){
//			System.out.println("Unit1"+unit.getClass());
			if(unit instanceof NormaleKugel)
			{
				for(MapElement mapelement: m_lstMapEmt )
				{
							if((unit.getM_3dobject().getX()>=(mapelement.getM_3dobject().getX()-1.0f))
							 &&(unit.getM_3dobject().getX()<=(mapelement.getM_3dobject().getX()+1.0f)))
							{
//								System.out.println("1Kugelbefindet sich im feld: "+mapelement.getM_3dobject().getClass()+" Pos:"+mapelement.getM_3dobject().getX()+":"+mapelement.getM_3dobject().getY());
								if(((unit.getM_3dobject().getY())>=(mapelement.getM_3dobject().getY()-1.0f)))
								{		
									if(((unit.getM_3dobject().getY())<=(mapelement.getM_3dobject().getY()+1.0f)))
									{
										Log.d(GeneralSettings.LoggerKategorie,"Kugelbefindet sich im feld: "+mapelement.getClass()+" Pos:"+mapelement.getM_3dobject().getX()+":"+mapelement.getM_3dobject().getY());
									}
								}
							}
				}
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
					unit.getSpeed()[0]=-1*BsDataholder.getHandykipplageX();
					unit.getSpeed()[1]=-1*BsDataholder.getHandykipplageY();
			}
		}
		
	}
	
	/** Bewegt die Kugel je nach Sensorlage
	 * 
	 */
	private void moveKugel() {
		for(Unit unit: m_lstUnit ){
			if(unit instanceof NormaleKugel)
			{
				//Kollisionsabfrage Wand
				if(checkForWallKollisions(unit))
				{
					
				}
				else //Ansonsten normal bewegen
				{
					unit.getM_3dobject().move(unit.getSpeed()[0]/10, unit.getSpeed()[1]/10);
				}
			}
		}
		
	}
	
	/** Guckt ob eine Kugel an eine Mauer kollidiert
	 * 
	 * @param unit
	 * @return
	 */
	private boolean checkForWallKollisions(Unit unit)
	{
		boolean wasgefunden=false;
		float [] positionDerKugel=unit.getM_3dobject().getPosition();
		//ALle Elemente in der Gegend durchgehen und gucken ob eine Mauer in der nähe ist.
//		System.out.println("Position:");
//		System.out.println("mit: "+unit.getM_3dobject().getX()+":"+unit.getM_3dobject().getY()+unit.getClass());
		int counter = 0;
		
		for(MapElement mapelement: m_lstMapEmt )
		{
			if(mapelement instanceof Mauer)
			{
				float x= unit.getM_3dobject().getPosition()[0];
				float y= unit.getM_3dobject().getPosition()[1];
				float durchmesserKugel		 = (float) 1.0;
				float durchmesserWuerfel  	 = (float) 1.0;
				float a=mapelement.getM_3dobject().getPosition()[0]+durchmesserWuerfel;
				float b=mapelement.getM_3dobject().getPosition()[0]-durchmesserWuerfel;
				float c=mapelement.getM_3dobject().getPosition()[1]+durchmesserWuerfel;
				float d=mapelement.getM_3dobject().getPosition()[1]-durchmesserWuerfel;
				
				//unit.getSpeed()[0]
				if(unit.getSpeed()[0]<0) //Bewegung nach Rechts
				{	// Wenn die bewegung nach rechts geht
					if(counter%100000==0)
					{
//						System.out.println("x:"+x+":"+y);
//						System.out.println("x:"+a+":"+b+":"+c+":"+d);
//						System.out.println("x:"+a+":"+b+":"+c+":"+d);
					}
					counter++;
				
					if(((x-durchmesserKugel)>=b))//Vor 
						if((x-durchmesserKugel)<=a) //und RückSeite
						{
							if(y<=c&&y>=d) //Y richtung beachten
							{
								Log.d(GeneralSettings.LoggerKategorie,"Kollision mit Nach Rechts : "+mapelement.getM_3dobject().getX()+":"+mapelement.getM_3dobject().getY()+mapelement.getClass());
								unit.getM_3dobject().move(0, unit.getSpeed()[1]/10);
//								unit.getM_3dobject().setX(b+(3*durchmesserKugel));alt
								unit.getM_3dobject().setX(a+(durchmesserKugel)); 
								wasgefunden=true;
								
							}
						}
				}
				else if(unit.getSpeed()[0]>0) //Bewegung nach links
				{
					if(((x+durchmesserKugel)>=b))//Vor 
						if((x+(2*durchmesserKugel))<=a) //und RückSeite
						{
							if(y<=c&&y>=d) //Y Richtung beachten
							{
								Log.d(GeneralSettings.LoggerKategorie,"Kollision mit Nach Links: "+mapelement.getM_3dobject().getX()+":"+mapelement.getM_3dobject().getY()+mapelement.getClass());
								unit.getM_3dobject().move(0, unit.getSpeed()[1]/10);
								unit.getM_3dobject().setX(b-(durchmesserKugel));
								wasgefunden=true;
							}
						}
				}
				
				if(unit.getSpeed()[1]<0) //Bewegung nach von oben nach unten
				{
					if(((x)>=b))//Vor 
						if((x)<=a) //und RückSeite
						{
							if((y-durchmesserKugel)<=c&&(y-durchmesserKugel)>=d) //Y Richtung beachten
							{
								Log.d(GeneralSettings.LoggerKategorie,"Kollision mit Nach unten: "+mapelement.getM_3dobject().getX()+":"+mapelement.getM_3dobject().getY()+mapelement.getClass());
								unit.getM_3dobject().move( unit.getSpeed()[0]/10, 0);
								unit.getM_3dobject().setY(c+(durchmesserKugel));
								wasgefunden=true;
							}
						}
				}
				else if(unit.getSpeed()[1]>=0)//Bewegung nach oben von unten
				{
					if(((x)>=b))//Vor 
						if((x)<=a) //und RückSeite
						{
							if((y+durchmesserKugel)<=c&&(y+durchmesserKugel)>=d) //Y Richtung beachten
							{
								Log.d(GeneralSettings.LoggerKategorie,"Kollision mit Nach oben: "+mapelement.getM_3dobject().getX()+":"+mapelement.getM_3dobject().getY()+mapelement.getClass());
								unit.getM_3dobject().move(unit.getSpeed()[0]/10, 0);
								unit.getM_3dobject().setY(d-(durchmesserKugel));
								wasgefunden=true;
							}
						}
				}
//				else
//				{
//					return false;
//				}
			}
			
			
			
//			if(Kollision.vergleichObereRechteEcke(positionDerKugel,mapelement.getM_3dobject().getPosition()))
//			{
//Speed reduzieren und und...
				//Obere Rechte Ecke gucken
				
				
				
//			}
		}
		if(wasgefunden)
			return true;
		else
		return false;
	}
	

	
	
	/** Läd die schon geladene Map (Aus der Bitdatei) in die Logic und erzeugt die Objekte.
	 * 
	 */
	private void loadmap() {

		int nY = 0;
		int nX = 0;

		for (ArrayList<Feld> row_Y : this.m_map.getFelderY()) 
		{
			if (row_Y != null) {
				nY++;
				for (Feld col_X : row_Y) 
				{
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
								this.m_lstMapEmt.add(new Busch(emt));
									
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
								this.m_lstMapEmt.add(new Mauer(emt));
							} else if (col_X.getMapElement() instanceof Oelteppich) {
								nTextureID = R.raw.diffuse;
							} else if (col_X.getMapElement() instanceof Rand) {
								nTextureID = R.drawable.grass_tile;
								ObjParser parser = new ObjParser(
								mContext.getResources(),
								mTextureManager, R.raw.plane);
								parser.parse();
								BaseObject3D emt = parser.getParsedObject()
								.getChildByName("Plane");

								Bitmap texture = BitmapFactory.decodeResource(mContext.getResources(), nTextureID);
								emt.addTexture(mTextureManager.addTexture(texture, nTextureID));
							    emt.setRotation(0, 0, 0);
								emt.setScale(1.0f);
								        
								// emt.setPosition(-col_X.getFeldposX() +
								// (row_Y.size() / 2.0f), -col_X.getFeldposY() +
								// (this.m_map.getFelderY().size() / 2.0f),
								// 0.0f);
								emt.setPosition(
								2.0f * -col_X.getFeldposX()
								+ (row_Y.size()),
								2.0f* -col_X.getFeldposY()
								+ (this.m_map.getFelderY().size()), -0.75f);
								   this.m_lstMapEmt.add(new Rand(emt));
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

							this.m_lstMapEmt.add(new Sand(emt));
						}
					}
				}
			}
		}
		System.out.println("#######################");
		for(MapElement mapelement: m_lstMapEmt )
		{
			Log.d(GeneralSettings.LoggerKategorie,"Map Element:"+mapelement.getM_3dobject().getX()+":"+mapelement.getM_3dobject().getY()+" "+mapelement.getClass());
		}
		System.out.println("#######################");
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
