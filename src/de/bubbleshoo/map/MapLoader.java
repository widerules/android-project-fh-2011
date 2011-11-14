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
import de.bubbleshoo.settings.GeneralSettings;

public class MapLoader {

	
	/** Läd die Map in das Game
	 * 
	 * @param urlZumBild
	 * @throws Exception
	 * @return liefert die fertige Map
	 */
	 public static Map laodMap(String urlZumBild) throws Exception {
		
		 Map map = new Map();
		 
		 //Create Map
		 for(int i=0; i<10;i++)
		 {
			 ArrayList liste = new ArrayList(); 
			 
			 for(int b=0;b<10;b++)
			 {
				 liste.add(new Feld("!"+i+" : "+b+"!"));
			 }
			 map.getFelderX().add(liste);
		 }
		 printMap(map);
		 
		 
		 
//To Do Bild einfügen	
		 Bitmap mBitmap = BitmapFactory.decodeResource(
				 	BsMainActivity.getAnwendungsContex().getResources(),
				 	R.drawable.icon);   
		 
		 int pic_width  = mBitmap.getWidth();
		 int pic_height = mBitmap.getHeight();
		 
		 int c = mBitmap.getPixel(1, 2);
		 
		 System.out.println("Density: "+mBitmap.getDensity());
		 System.out.println("getRowBytes: "+mBitmap.getRowBytes());
		 System.out.println("getHeight: "+mBitmap.getHeight());
		 System.out.println("getWidth: "+mBitmap.getWidth());
		 System.out.println("Farbe 1,2 : "+c);
		 

		 
		 
	        return null;
	    }
	 
	 
	 public static void printMap(Map map)
	 {
		 for(ArrayList<Feld> felderY : map.getFelderX())
		 {
//			 Log.e(GeneralSettings.LoggerKategorie, felderY.toString());
	        	
			 System.out.println("");
//			 System.out.print(felderY);
			 
			 for(Feld feldX: felderY)
			 {
//				 Log.e(GeneralSettings.LoggerKategorie, feldX.getFeldname());
				 System.out.print(feldX.getFeldname());
			 }
		 }
		 
		 
	 }
}
