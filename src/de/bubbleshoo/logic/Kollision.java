package de.bubbleshoo.logic;


import java.util.List;

import android.util.Log;
import de.bubbleshoo.mapElemente.MapElement;
import de.bubbleshoo.mapElemente.Mauer;
import de.bubbleshoo.settings.GeneralSettings;
import de.bubbleshoo.units.Unit;

public class Kollision {
	/** Guckt ob eine Kugel an eine Mauer kollidiert
	 * 
	 * @param unit
	 * @return
	 */
	static boolean checkForWallKollisions(Unit unit, List<MapElement> m_lstMapEmt)
	{
		boolean wasgefunden=false;
		
		//ALle Elemente in der Gegend durchgehen und gucken ob eine Mauer in der n�he ist.
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
//				float durchmesserWuerfel  	 = (float) Math.sqrt(2.0f); //Wurzel 2... dank Torben
				float durchmesserWuerfel  	 = 1; //Wurzel 2... dank Torben Noch nen fehler irgendwo..
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
						if((x-durchmesserKugel)<=a) //und R�ckSeite
						{
							if(y<=c	&&y>=d) //Y richtung beachten
							{
//								Log.d(GeneralSettings.LoggerKategorie,"Kollision mit Nach Rechts : "+mapelement.getM_3dobject().getX()+":"+mapelement.getM_3dobject().getY()+mapelement.getClass());
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
						if((x+(2*durchmesserKugel))<=a) //und R�ckSeite
						{
							if(y<=c&&y>=d) //Y Richtung beachten
							{
//								Log.d(GeneralSettings.LoggerKategorie,"Kollision mit Nach Links: "+mapelement.getM_3dobject().getX()+":"+mapelement.getM_3dobject().getY()+mapelement.getClass());
								unit.getM_3dobject().move(0, unit.getSpeed()[1]/10);
								unit.getM_3dobject().setX(b-(durchmesserKugel));
								wasgefunden=true;
							}
						}
				}
	
				
				if(unit.getSpeed()[1]<0) //Bewegung nach von oben nach unten
				{
					if(((x)>=b))//Vor 
						if((x)<=a) //und R�ckSeite
						{
							if((y-durchmesserKugel)<=c&&(y-durchmesserKugel)>=d) //Y Richtung beachten
							{
//								Log.d(GeneralSettings.LoggerKategorie,"Kollision mit Nach unten: "+mapelement.getM_3dobject().getX()+":"+mapelement.getM_3dobject().getY()+mapelement.getClass());
								unit.getM_3dobject().move( unit.getSpeed()[0]/10, 0);
								unit.getM_3dobject().setY(c+(durchmesserKugel));
								wasgefunden=true;
							}
						}
				}
				else if(unit.getSpeed()[1]>=0)//Bewegung nach oben von unten
				{
					if(((x)>=b))//Vor 
						if((x)<=a) //und R�ckSeite
						{
							if((y+durchmesserKugel)<=c&&(y+durchmesserKugel)>=d) //Y Richtung beachten
							{
//								Log.d(GeneralSettings.LoggerKategorie,"Kollision mit Nach oben: "+mapelement.getM_3dobject().getX()+":"+mapelement.getM_3dobject().getY()+mapelement.getClass());
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
	
	
}
