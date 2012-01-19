package de.bubbleshoo.logic;

import java.util.ArrayList;
import java.util.Random;

import org.xml.sax.Parser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import de.bubbleshoo.data.BaseObject3D;
import de.bubbleshoo.data.BsParser;
import de.bubbleshoo.data.ObjParser;
import de.bubbleshoo.graphics.SimpleMaterial;
import de.bubbleshoo.main.R;
import de.bubbleshoo.map.Feld;
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
import de.bubbleshoo.settings.GeneralSettings;
import de.bubbleshoo.units.ExplosionsKugel;
import de.bubbleshoo.units.NormaleKugel;

public class LogikMapLaden {
	
	/** Läd die schon geladene Map (Aus der Bitdatei) in die Logic und erzeugt die Objekte.
	 * 
	 */
	public static void loadmap(BsParser	m_parser) {

		int nY = 0;
		int nX = 0;
		BaseObject3D emt = null;
		
		long start = 0;
		long end = 0;
		
		for (ArrayList<Feld> row_Y : LogicThread.m_map.getFelderY()) 
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
								
								emt = m_parser.getObjectByName("Plane");
								
//								start = System.nanoTime();
								emt.setShader(new SimpleMaterial());
								//emt.setShader(m_MatManager.getMaterialFromShader("SimpleMaterial"));
//								end = System.nanoTime() - start;
//								Log.d("bench1", " setShader: " + end);
								
								Bitmap texture = BitmapFactory.decodeResource(
										LogicThread.mContext.getResources(), nTextureID);
								
								emt.addTexture(LogicThread.mTextureManager.addTexture(
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
												+ (LogicThread.m_map.getFelderY()
														.size()), -0.75f);
								LogicThread.m_lstMapEmt.add(new Busch(emt));
									
							} else if (col_X.getMapElement() instanceof Felsen) {
								nTextureID = R.raw.fieldstone;
							} else if (col_X.getMapElement() instanceof Gras) {
								nTextureID = R.drawable.grass_tile;
							} else if (col_X.getMapElement() instanceof Mauer) {
								nTextureID = R.drawable.brick_tile;
								
								emt = m_parser.getObjectByName("Plane");
								
//								start = System.nanoTime();
								emt.setShader(new SimpleMaterial());
								//emt.setShader(m_MatManager.getMaterialFromShader("SimpleMaterial"));
//								end = System.nanoTime() - start;
//								Log.d("bench1", " setShader: " + end);
								
								Bitmap texture = BitmapFactory.decodeResource(
										LogicThread.mContext.getResources(), nTextureID);
								
								emt.addTexture(LogicThread.mTextureManager.addTexture(
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
												+ (LogicThread.m_map.getFelderY()
														.size()), -0.75f);
								LogicThread.m_lstMapEmt.add(new Mauer(emt));
							} else if (col_X.getMapElement() instanceof Oelteppich) {
								nTextureID = R.raw.diffuse;
							} else if (col_X.getMapElement() instanceof Rand) {
								nTextureID = R.drawable.grass_tile;
								
								emt = m_parser.getObjectByName("Plane");
								
//								start = System.nanoTime();
								emt.setShader(new SimpleMaterial());
								//emt.setShader(m_MatManager.getMaterialFromShader("SimpleMaterial"));
//								end = System.nanoTime() - start;
//								Log.d("bench1", " setShader: " + end);
								
								Bitmap texture = BitmapFactory.decodeResource(
										LogicThread.mContext.getResources(), nTextureID);
								
								emt.addTexture(LogicThread.mTextureManager.addTexture(
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
								2.0f* -col_X.getFeldposY()
								+ (LogicThread.m_map.getFelderY().size()), -0.75f);
								LogicThread.m_lstMapEmt.add(new Rand(emt));
							} else if (col_X.getMapElement() instanceof Sand) {
								nTextureID = R.drawable.sand_tile;
							} else if (col_X.getMapElement() instanceof Stacheln) {
								nTextureID = R.drawable.grass_tile;
							} else if (col_X.getMapElement() instanceof Start) {
								nTextureID = R.drawable.heightmap1;
								
//								ObjParser parser = new ObjParser(
//										LogicThread.mContext.getResources(),
//										LogicThread.mTextureManager, R.raw.sphere);
//								parser.parse();
								//BaseObject3D emt = m_parser.getObjectByName("Sphere");
								
								
//								BaseObject3D emt = parser.getParsedObject()
//										.getChildByName("Sphere");
//
//								Bitmap texture = BitmapFactory.decodeResource(
//										LogicThread.mContext.getResources(), nTextureID);
//								emt.addTexture(LogicThread.mTextureManager.addTexture(
//										texture, nTextureID));
//								emt.setRotation(0, 0, 0);
//								emt.setScale(1.0f);
//								// emt.setPosition(-col_X.getFeldposX() +
//								// (row_Y.size() / 2.0f), -col_X.getFeldposY() +
//								// (this.m_map.getFelderY().size() / 2.0f),
//								// 0.0f);
//								emt.setPosition(
//										2.0f * -col_X.getFeldposX()
//												+ (row_Y.size()),
//										2.0f
//												* -col_X.getFeldposY()
//												+ (LogicThread.m_map.getFelderY()
//														.size()), -0.75f);
//								LogicThread.m_lstUnit.add(new NormaleKugel(emt));
								addKugelnToMap(m_parser, nTextureID, col_X,row_Y);
								
//								LogicThread.m_lstUnit.add(new ExplosionsKugel(emt));
							} else if (col_X.getMapElement() instanceof Wasser) {
								nTextureID = R.drawable.grass_tile;
							} else if (col_X.getMapElement() instanceof Weg) {
								nTextureID = R.drawable.sand_tile;
							} else if (col_X.getMapElement() instanceof Ziel) {
								nTextureID = R.drawable.grass_tile;
								
								emt = m_parser.getObjectByName("Plane");
								
//								start = System.nanoTime();
								emt.setShader(new SimpleMaterial());
								//emt.setShader(m_MatManager.getMaterialFromShader("SimpleMaterial"));
//								end = System.nanoTime() - start;
//								Log.d("bench1", " setShader: " + end);
								
								Bitmap texture = BitmapFactory.decodeResource(
										LogicThread.mContext.getResources(), nTextureID);
								
								emt.addTexture(LogicThread.mTextureManager.addTexture(
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
								2.0f* -col_X.getFeldposY()
								+ (LogicThread.m_map.getFelderY().size()), -0.75f);
								
								LogicThread.m_lstMapEmt.add(new Ziel(emt));
							} else {
								/**
								 * Default
								 */
								nTextureID = R.drawable.grass_tile;
							}
							
							emt = m_parser.getObjectByName("Plane");
							
//							start = System.nanoTime();
							emt.setShader(new SimpleMaterial());
							//emt.setShader(m_MatManager.getMaterialFromShader("SimpleMaterial"));
//							end = System.nanoTime() - start;
//							Log.d("bench1", " setShader: " + end);
							
							Bitmap texture = BitmapFactory.decodeResource(
									LogicThread.mContext.getResources(), nTextureID);
							
							emt.addTexture(LogicThread.mTextureManager.addTexture(
									texture, nTextureID));
							
							emt.setRotation(0, 0, 0);
							emt.setScale(1.0f);
							// emt.setPosition(-col_X.getFeldposX() +
							// (row_Y.size() / 2.0f), -col_X.getFeldposY() +
							// (this.m_map.getFelderY().size() / 2.0f), 0.0f);
							emt.setPosition(
									2.0f * -col_X.getFeldposX()
											+ (row_Y.size()),
									2.0f * -col_X.getFeldposY()
											+ (LogicThread.m_map.getFelderY().size()),
									0.0f);

							LogicThread.m_lstMapEmt.add(new Sand(emt));
						}
					}
				}
			}
		}
		System.out.println("#######################");
		for(MapElement mapelement: LogicThread.m_lstMapEmt )
		{
			Log.d(GeneralSettings.LoggerKategorie,"Map Element:"+mapelement.getM_3dobject().getX()+":"+mapelement.getM_3dobject().getY()+" "+mapelement.getClass());
		}
		System.out.println("#######################");
	}
	
	public static void addKugelnToMap(BsParser parser, int nTextureID, Feld col_X, ArrayList<Feld> row_Y)
	{
		Random zufallszahl= new Random();
		
		for (int i=0; i<1;i++)
		{
			BaseObject3D emt = parser.getObjectByName("Sphere");
	
//			start = System.nanoTime();
			emt.setShader(new SimpleMaterial());
			//emt.setShader(m_MatManager.getMaterialFromShader("SimpleMaterial"));
//			end = System.nanoTime() - start;
//			Log.d("bench1", " setShader: " + end);
			
			Bitmap texture = BitmapFactory.decodeResource(
					LogicThread.mContext.getResources(), nTextureID);
			emt.addTexture(LogicThread.mTextureManager.addTexture(
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
							+ (LogicThread.m_map.getFelderY()
									.size()), -0.75f);
			int zahl =zufallszahl.nextInt(3);
			if(zahl==3)
				LogicThread.m_map.getKugeln().add(new ExplosionsKugel(emt));
			else if(zahl==2)
				LogicThread.m_map.getKugeln().add(new ExplosionsKugel(emt));
			else if(zahl==1)
				LogicThread.m_map.getKugeln().add(new NormaleKugel(emt));
			else 
				LogicThread.m_map.getKugeln().add(new NormaleKugel(emt));
				
		}
	}

}
