package de.bubbleshoo.map;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import de.bubbleshoo.main.BsMainActivity;
import de.bubbleshoo.main.R;
import de.bubbleshoo.mapElemente.Felsen;
import de.bubbleshoo.mapElemente.Gras;
import de.bubbleshoo.mapElemente.MapElement;
import de.bubbleshoo.mapElemente.Mauer;
import de.bubbleshoo.mapElemente.Oelteppich;
import de.bubbleshoo.mapElemente.Rand;
import de.bubbleshoo.mapElemente.Sand;
import de.bubbleshoo.mapElemente.Stacheln;
import de.bubbleshoo.mapElemente.Start;
import de.bubbleshoo.mapElemente.Weg;
import de.bubbleshoo.mapElemente.Ziel;
import de.bubbleshoo.settings.GeneralSettings;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MapLoader {

	//FarbCodierung:
	static int GRAS		=-14503604;
	static int STEIN	=-8421505;
	static int MAUER	=-1237980;
	static int OEL		=-16777216;
	static int STACHEL	=-6075996;
	static int SAND		=-3584;
	static int WEG		=-1;
	static int ZIEL		=-14066;
	static int START	=-20791;
	
	
	/**
	 * Läd die Map in das Game
	 * 
	 * @param urlZumBild
	 * @throws Exception
	 * @return liefert die fertige Map
	 */
	public static Map laodMap(String urlZumBild) throws Exception {

		Map map = new Map();

		// To Do Bild einfügen
		Bitmap mBitmap = BitmapFactory.decodeResource(BsMainActivity
				.getAnwendungsContex().getResources(), R.drawable.map1);
		Bitmap heightmap = BitmapFactory.decodeResource(BsMainActivity
				.getAnwendungsContex().getResources(), R.drawable.height1map);

		int pic_width = mBitmap.getWidth();
		int pic_height = mBitmap.getHeight();

		
//		int c = mBitmap.getPixel(1, 2);

		System.out.println("Density: " + mBitmap.getDensity());
		System.out.println("getRowBytes: " + mBitmap.getRowBytes());
		System.out.println("getHeight: " + mBitmap.getHeight());
		System.out.println("getWidth: " + mBitmap.getWidth());
//		System.out.println("Farbe 1,2 : " + c);

		
		// Map laden:
		map = new Map();
		// Create Map
		
		ArrayList<Feld> liste = new ArrayList<Feld>();
		MapElement mapElement = null;
		Feld neuesFeld = new Feld(" !" + 0 + " : " + 0 + " : " + 0
				+ "! ");
		int hoehe=0;
		
		for (int i = 0; i < pic_width+1; i++) {
			
			neuesFeld = new Feld(" !" + 0 + " : " + 0 + " : " + i
					+ "! ");
			hoehe= heightmap.getPixel(i, 0);
			mapElement = new Rand();
			neuesFeld.setMapElement(mapElement);
			neuesFeld.setFeldposX(i);
			neuesFeld.setFeldposY(0);
			neuesFeld.setFeldhoehe(hoehe);
			
			liste.add(neuesFeld);
		}
		map.getFelderX().add(liste);
		
		for (int i = 0; i < pic_height; i++) {
				
				//Rechter Rand:
				liste = new ArrayList<Feld>();
			
				neuesFeld = new Feld(" !" + (i+1) + " : " + 0 + " : " + 0
						+ "! ");
				hoehe= heightmap.getPixel(0, i);
				mapElement = new Rand();
				neuesFeld.setMapElement(mapElement);
				neuesFeld.setFeldposX(0);
				neuesFeld.setFeldposY(i);
				neuesFeld.setFeldhoehe(hoehe);
				
				liste.add(neuesFeld);
			
			for (int b = 0; b < pic_width; b++) {
				int c = mBitmap.getPixel(b, i);
				hoehe= heightmap.getPixel(b+1, i+1);
				neuesFeld = new Feld(" !" + (i+1) + " : " + c + " : " + (b+1)
						+ "! ");
				
				mapElement = getMapElementTypeFromColor(c);
				neuesFeld.setMapElement(mapElement);
				neuesFeld.setFeldposX(i+1);
				neuesFeld.setFeldposY(b+1);
				neuesFeld.setFeldhoehe(hoehe);
				
				liste.add(neuesFeld);
			}
			//Rechter Rand:
			
			
			
			map.getFelderX().add(liste);
		}
		printMap(map);

		return null;
	}
	
	/** liefert das MapElementObject zur passenden Farbe
	 * 
	 * @param c Farbe C
	 * @return liefert das MapElementObject zur passenden Farbe
	 */
	private static MapElement getMapElementTypeFromColor(int c) {
		if(c==GRAS)
			return new Gras();
		else if(c==STEIN)
			return new Felsen();
		else if(c==MAUER)
			return new Mauer();
		else if(c==OEL)
			return new Oelteppich();
		else if(c==ZIEL)
			return new Ziel();
		else if(c==START)
			return new Start();
		else if(c==WEG)
			return new Weg();
		else if(c==SAND)
			return new Sand();
		else if(c==STACHEL)
			return new Stacheln();
		
		return null;
	}

	public static void printMap(Map map) {
		System.out.println("###################################");
		for (ArrayList<Feld> felderY : map.getFelderX()) {
			// Log.e(GeneralSettings.LoggerKategorie, felderY.toString());

			System.out.println("");
			// System.out.print(felderY);

			for (Feld feldX : felderY) {
				Log.e(GeneralSettings.LoggerKategorie, feldX.getFeldname()+" "+feldX.getFeldhoehe()+" "+feldX.getMapElement().getClass());
//				System.out.print(feldX.getFeldname());
			}
		}

	}
}
