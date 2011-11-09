package de.bubbleshoo.sensors;

public class BsDataholder {
	/*
	 * Konstanten	
	 */
    // Touch Events
    private final int NONE = 0;
    private final int DRAG = 0;
    private final int ZOOM = 0;
    
    /*
     * Attribute
     */
    //Touch
	private static float 	touchX = 0;
	private static float 	touchY = 0;
	private static float 	abstandZwischenZweiFingern = 0;
	private static int 		touchModus = 0;

	//Accilerator
	private static float 	handykipplageX = 0;
	private static float 	handykipplageY = 0;
	private static float 	handykipplageZ = 0;
	
	//Kompass
	private static float 	kompassrichtung = 0;

	//Licht
	private static float 	raumhelligkeit = 0;


	/*
	 * Getter/Setter
	 */
	
	
	
	public static int getTouchModus() {
		return touchModus;
	}
	/** Liefert den Abstand zwischen zwei Fingern, wenn mit 2 Fingern das Display gedrückt wird.
	 * 
	 * @return
	 */
	public static float getAbstandZwischenZweiFingern() {
		return abstandZwischenZweiFingern;
	}
	public static void setAbstandZwischenZweiFingern(
			float abstandZwischenZweiFingern) {
		BsDataholder.abstandZwischenZweiFingern = abstandZwischenZweiFingern;
	}
	/** Liefer die Handyausrichtung anhand des Erdmagnetfeldes
	 * 
	 * @return Liefer 0-360 ich vermute 0 bedeutet Norden ist vom Handy oben
	 */
	public static float getKompassrichtung() {
		return kompassrichtung;
	}
	/** Setzt die Kompasrichtung
	 * 
	 * @param kompassrichtung
	 */
	public static void setKompassrichtung(float kompassrichtung) {
		BsDataholder.kompassrichtung = kompassrichtung;
	}
	
	/** Liefert die Raumhelligkeit.
	 * 
	 * @return Wert zwischen 10 und 360... Normales licht ist ca 140. Wenn man Telefon an Ohr hält 10. 
	 */
	public static float getRaumhelligkeit() {
		return raumhelligkeit;
	}
	public static void setRaumhelligkeit(float raumhelligkeit) {
		BsDataholder.raumhelligkeit = raumhelligkeit;
	}
	public static float getHandykipplageX() {
		return handykipplageX;
	}
	public static void setHandykipplageX(float handykipplageX) {
		BsDataholder.handykipplageX = handykipplageX;
	}
	public static float getHandykipplageY() {
		return handykipplageY;
	}
	public static void setHandykipplageY(float handykipplageY) {
		BsDataholder.handykipplageY = handykipplageY;
	}
	public static float getHandykipplageZ() {
		return handykipplageZ;
	}
	public static void setHandykipplageZ(float handykipplageZ) {
		BsDataholder.handykipplageZ = handykipplageZ;
	}
	public static void setTouchModus(int touchModus) {
		BsDataholder.touchModus = touchModus;
	}
	public static float getTouchX() {
		return touchX;
	}
	public static void setTouchX(float touchX) {
		BsDataholder.touchX = touchX;
	}
	public static float getTouchY() {
		return touchY;
	}
	public static void setTouchY(float touchY) {
		BsDataholder.touchY = touchY;
	}

}
