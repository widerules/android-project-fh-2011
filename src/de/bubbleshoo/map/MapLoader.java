package de.bubbleshoo.map;

import android.content.res.Resources;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import de.bubbleshoo.main.BsMainActivity;
import de.bubbleshoo.main.R;
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
import de.bubbleshoo.mapElemente.Weg;
import de.bubbleshoo.mapElemente.Ziel;
import de.bubbleshoo.settings.GeneralSettings;
import de.bubbleshoo.units.Enemy;
import de.bubbleshoo.units.Unit;
import de.bubbleshoo.units.Vip;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MapLoader {
	public static final Bitmap.Config DEFAULT_BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
	public static final Bitmap.Config FAST_BITMAP_CONFIG = Bitmap.Config.RGB_565;
	
	//FarbCodierung:
	static int GRAS		=-14503604;
	static int GRAS2	=-14569910;
	static int GRAS3	=-14044590;
	
	static int STEIN	=-8421505;
	static int STEIN2	=-8684165;
	static int STEIN3	=-8093052;
	
	static int MAUER	=-1237980;
	static int MAUER2	=-1106911;
	static int MAUER3	=-1631199;
	static int MAUER4	=-1106903;
	
	static int OEL		=-16777216;
	static int OEL2		=-16777216;
	
	static int STACHEL	=-6075996;
	static int STACHEL2	=-5944923;
	
	static int SAND		=-3584;
	static int SAND2	=-4352;
	static int SAND3	=-3328;
	
	static int WEG		=-1;
	static int WEG2		=-1;
	
	static int ZIEL		=-14066;
	static int ZIEL2	=-13552;
	static int ZIEL3	=-14584;
	static int ZIEL4	=-14576;
	
	static int START	=-20791;
	static int START2	=-20794;
	
	static int VIP		=-7864299;

	
	static int ENEMY	=-12629812;

	
	/**
	 * L�d die Map in das Game. Die Hightmap muss x und y +1 gr��er sein. Jede Weitere Enemy Map muss gleich Gro� sein.
	 * 
	 * @param urlZumBild
	 * @throws Exception
	 * @return liefert die fertige Map
	 */
	public static Map laodMap(String urlZumBild) throws Exception {
 
		
		Map map = new Map();  

		//L�d die erste Map
		int mapid=BsMainActivity.getAnwendungsContex().getResources().getIdentifier( urlZumBild, "drawable", BsMainActivity.getAnwendungsContex().getPackageName() );
//		int heightid=BsMainActivity.getAnwendungsContex().getResources().getIdentifier( "height"+urlZumBild, "drawable", BsMainActivity.getAnwendungsContex().getPackageName() );
//		int enemyid=BsMainActivity.getAnwendungsContex().getResources().getIdentifier( "enemy"+urlZumBild, "drawable", BsMainActivity.getAnwendungsContex().getPackageName() );
//		

		Bitmap mBitmap = BitmapFactory.decodeResource(BsMainActivity
				.getAnwendungsContex().getResources(), mapid);
		
//		Bitmap heightmap = BitmapFactory.decodeResource(BsMainActivity
//				.getAnwendungsContex().getResources(), heightid);
//		
//		Bitmap enemymap = BitmapFactory.decodeResource(BsMainActivity
//				.getAnwendungsContex().getResources(), enemyid);
		
//		Resources r = BsMainActivity.getAnwendungsContex().getResources();
//		Bitmap mBitmap = loadBitmap(r.getDrawable(R.drawable.map1));
//		Bitmap heightmap =loadBitmap(r.getDrawable(R.drawable.heightmap1));
//		Bitmap enemymap =loadBitmap(r.getDrawable(R.drawable.enemymap1));
//		
		//Gr��e ermitteln
		int pic_width = mBitmap.getWidth();
		int pic_height = mBitmap.getHeight();

		//Brauchbare Funktionen:
//		System.out.println("Density: " + mBitmap.getDensity());
//		System.out.println("getRowBytes: " + mBitmap.getRowBytes());
//		System.out.println("getHeight: " + mBitmap.getHeight());
	
//		System.out.println("getWidth: " + mBitmap.getWidth());

		// Map erzeugen:
		map = new Map();

		//Liste mit Feldern erzeugen. (Jede Liste ist eine X-----> Koordinaten Reihe)
		ArrayList<Feld> liste = new ArrayList<Feld>();
				
		
		
		//Initieren
		MapElement mapElement = null;
		Feld neuesFeld = new Feld(" !" + 0 + " : " + 0 + " : " + 0
				+ "! ");
		int hoehe=0;
		int enemeColor=0;
		//Die 0te Reihe erzeugen, damit die Heightmap fr�her beginnt
		for (int i = 0; i < pic_width+1; i++) {
			
			neuesFeld = new Feld(" !" + 0 + " : " + 0 + " : " + i
					+ "! ");
//			hoehe= heightmap.getPixel(i, 0);
			mapElement = new Rand();
			neuesFeld.setMapElement(mapElement);
			neuesFeld.setFeldposX(i);
			neuesFeld.setFeldposY(0);
			neuesFeld.setFeldhoehe(Color.red(hoehe));
			
			liste.add(neuesFeld);
		}
		//0te Reihe hinzuf�gen
		map.getFelderY().add(liste);
		
		//Gesamte Map laden
		for (int i = 0; i < pic_height; i++) {
				
				//Linker Rand: Also das 0te Element erzeugen
				liste = new ArrayList<Feld>();
				
				neuesFeld = new Feld(" !" + (i+1) + " : " + 0 + " : " + 0
						+ "! ");
				
//				hoehe= heightmap.getPixel(0, i);
//				enemeColor= enemymap.getPixel(0, i);
			//Hier m�sste man eine weitere Map Hinzuf�gen
				
				mapElement = new Rand();
				neuesFeld.setMapElement(mapElement);
				neuesFeld.setFeldposX(0);
				neuesFeld.setFeldposY(i+1);
				neuesFeld.setFeldhoehe(Color.red(hoehe));
				
				Unit enemy = checkForEnemys(enemeColor);
			
				liste.add(neuesFeld);
			//Die Einheiten... Map durchgehen und in die Liste eintragen.
			for (int b = 0; b < pic_width; b++) {
				int c = mBitmap.getPixel(b, i);
				
//				hoehe= heightmap.getPixel(b+1, i+1);
//				enemeColor= enemymap.getPixel(b, i);
			//Hier m�sste man eine weitere Map Hinzuf�gen
				
				
				enemy = checkForEnemys(enemeColor);
				neuesFeld = new Feld(" !!" + (i+1) + " : " +c + " : " + (b+1)
						+ "! ");
				if(enemy!=null)
					neuesFeld.addEnemy(enemy);
				
				mapElement = getMapElementTypeFromColor(c);
				neuesFeld.setMapElement(mapElement);
				neuesFeld.setFeldposX(b+1);
				neuesFeld.setFeldposY(i+1);
				
				//Werte von der hoehe:
				int gruenanteil= Color.green(hoehe);
				int blauanteil= Color.blue(hoehe);
				int rotanteil= Color.red(hoehe);
				neuesFeld.setFeldhoehe(rotanteil);
				
				liste.add(neuesFeld);
			}
			//Rechter Rand:
			
			map.getFelderY().add(liste);
		}
		printMap(map);

		return map;
	}
	
	public static Bitmap loadBitmap(Drawable sprite, Config bitmapConfig) {
		int width = sprite.getIntrinsicWidth();
		int height = sprite.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, bitmapConfig);
		Canvas canvas = new Canvas(bitmap);
		sprite.setBounds(0, 0, width, height);
		sprite.draw(canvas);
		return bitmap;
	}
	
	public static Bitmap loadBitmap(Drawable sprite) {
		return loadBitmap(sprite, FAST_BITMAP_CONFIG);
	}
	
	
	/** Sucht den Enemy bzw. VIp aus der Enemymap
	 * 
	 * @param enemy
	 * @return
	 */
	private static Unit checkForEnemys(int enemy) {
		if(enemy==ENEMY)
			return new Enemy();
		else if(enemy==VIP)
			return new Vip();

		return null;
	}
	
	

	/** liefert das MapElementObject zur passenden Farbe
	 * 
	 * @param c Farbe C
	 * @return liefert das MapElementObject zur passenden Farbe
	 */
	private static MapElement getMapElementTypeFromColor(int c) {
		if(c==GRAS||c==GRAS3||c==GRAS2)
			return new Gras();
		else if(c==STEIN||c==STEIN2||c==STEIN3)
			return new Felsen();
		else if(c==MAUER||c==MAUER4||c==MAUER3||c==MAUER2)
			return new Mauer();
		else if(c==OEL||c==OEL2||c==OEL)
			return new Oelteppich();
		else if(c==ZIEL||c==ZIEL3||c==ZIEL4||c==ZIEL2)
			return new Ziel();
		else if(c==START||c==START2)
			return new Start();
		else if(c==WEG||c==WEG2)
			return new Weg();
		else if(c==SAND||c==SAND3||c==SAND2)
			return new Sand();
		else if(c==STACHEL||c==STACHEL2)
			return new Stacheln();
		
		return new Weg();
	}

	public static void printMap(Map map) {
		System.out.println("###################################");
		for (ArrayList<Feld> felderY : map.getFelderY()) {
			// Log.e(GeneralSettings.LoggerKategorie, felderY.toString());

			System.out.println("");
			// System.out.print(felderY);

			for (Feld feldX : felderY) {
				Log.e(GeneralSettings.LoggerKategorie, feldX.getFeldname()+" "+feldX.getFeldhoehe()+" "+feldX.getFirstUnit()+feldX.getMapElement().getClass());
//				System.out.print(feldX.getFeldname());
			}
		}

	}
}
