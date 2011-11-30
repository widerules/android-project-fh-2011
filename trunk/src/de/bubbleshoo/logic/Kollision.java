package de.bubbleshoo.logic;

public class Kollision {
	/** Guckt ob das Unit mit dem mapteilchen kolidiert
	 * 
	 * @param unit
	 * @param mapteil
	 * @return
	 */
	public static boolean vergleichMitteOben(float [] position, float [] position2)
	{
		return false;
	}
	/** Guckt ob das Unit mit dem mapteilchen kolidiert
	 * 
	 * @param unit
	 * @param mapteil
	 * @return
	 */
	public static boolean vergleichObereRechteEcke(float [] position, float [] position2)
	{
		float x= position2[0];
		float y= position2[1];
		float a=position2[0]+1;
		float b=position2[0]-1;
		float c=position2[1]+1;
		float d=position2[1]-1;
		
		

		
		return false;
	}
	/** Guckt ob das Unit mit dem mapteilchen kolidiert
	 * 
	 * @param unit
	 * @param mapteil
	 * @return
	 */
	public static boolean vergleichMitteRechts(float [] position, float [] position2)
	{
		return false;
	}
	/** Guckt ob das Unit mit dem mapteilchen kolidiert
	 * 
	 * @param unit
	 * @param mapteil
	 * @return
	 */
	public static boolean vergleichUntereRechteEcke(float [] position, float [] position2)
	{
		return false;
	}
	/** Guckt ob das Unit mit dem mapteilchen kolidiert
	 * 
	 * @param unit
	 * @param mapteil
	 * @return
	 */
	public static boolean vergleichObereLinkeEcke(float [] position, float [] position2)
	{
		return false;
	}
	/** Guckt ob das Unit mit dem mapteilchen kolidiert
	 * 
	 * @param unit
	 * @param mapteil
	 * @return
	 */
	public static boolean vergleichMitteLinke(float [] position, float [] position2)
	{
		return false;
	}
	/** Guckt ob das Unit mit dem mapteilchen kolidiert
	 * 
	 * @param unit
	 * @param mapteil
	 * @return
	 */
	public static boolean vergleichUntereLinkeEcke(float [] position, float [] position2)
	{
		return false;
	}
	/** Guckt ob das Unit mit dem mapteilchen kolidiert
	 * 
	 * @param unit
	 * @param mapteil
	 * @return
	 */
	public static boolean vergleichMitteUntere(float [] position, float [] position2)
	{
		return false;
	}
	
}
