package de.bubbleshoo.logic;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import de.bubbleshoo.data.BaseObject3D;
import de.bubbleshoo.data.ObjParser;
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
	/** L�d die schon geladene Map (Aus der Bitdatei) in die Logic und erzeugt die Objekte.
	 * 
	 */
	public static void loadmap() {

		int nY = 0;
		int nX = 0;

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
								
								ObjParser parser = new ObjParser(
										LogicThread.mContext.getResources(),
										LogicThread.mTextureManager, R.raw.plane);
								parser.parse();
								BaseObject3D emt = parser.getParsedObject()
										.getChildByName("Plane");

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
								ObjParser parser = new ObjParser(
										LogicThread.mContext.getResources(),
										LogicThread.mTextureManager, R.raw.plane);
								parser.parse();
								BaseObject3D emt = parser.getParsedObject()
										.getChildByName("Plane");

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
								ObjParser parser = new ObjParser(
								LogicThread.mContext.getResources(),
								LogicThread.mTextureManager, R.raw.plane);
								parser.parse();
								BaseObject3D emt = parser.getParsedObject()
								.getChildByName("Plane");

								Bitmap texture = BitmapFactory.decodeResource(LogicThread.mContext.getResources(), nTextureID);
								emt.addTexture(LogicThread.mTextureManager.addTexture(texture, nTextureID));
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
								ObjParser parser = new ObjParser(
										LogicThread.mContext.getResources(),
										LogicThread.mTextureManager, R.raw.sphere);
								parser.parse();
								BaseObject3D emt = parser.getParsedObject()
										.getChildByName("Sphere");

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
//								LogicThread.m_lstUnit.add(new NormaleKugel(emt));
								LogicThread.m_lstUnit.add(new ExplosionsKugel(emt));
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
									LogicThread.mContext.getResources(), LogicThread.mTextureManager,
									R.raw.plane);
							parser.parse();
							BaseObject3D emt = parser.getParsedObject()
									.getChildByName("Plane");

							Bitmap texture = BitmapFactory.decodeResource(
									LogicThread.mContext.getResources(), nTextureID);
							emt.addTexture(LogicThread.mTextureManager.addTexture(texture,
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

}
