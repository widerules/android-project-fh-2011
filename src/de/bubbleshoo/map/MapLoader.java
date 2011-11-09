package de.bubbleshoo.map;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.FileInputStream;

import de.bubbleshoo.main.BsMainActivity;
import de.bubbleshoo.main.R;

public class MapLoader {

	
	/** Läd die Map in das Game
	 * 
	 * @param urlZumBild
	 * @throws Exception
	 * @return liefert die fertige Map
	 */
	 public static Map laodMap(String urlZumBild) throws Exception {
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
	 
	 
}
