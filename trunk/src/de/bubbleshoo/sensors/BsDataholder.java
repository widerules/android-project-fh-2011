package de.bubbleshoo.sensors;

public class BsDataholder {
	/*
	 * Konstanten	
	 */
    // Touch Events
    /**
	 * @uml.property  name="nONE"
	 */
    private final int NONE = 0;
    /**
	 * @uml.property  name="dRAG"
	 */
    private final int DRAG = 0;
    /**
	 * @uml.property  name="zOOM"
	 */
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
	private static float[] 	m_kippX = new float[4];
    private static float[] 	m_kippY = new float[4];
    private static float[] 	m_kippZ = new float[4];
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
		if (m_kippX[3] == 0.0)
			return m_kippX[0];
		else
			return ((0.25f * m_kippX[0]) + (0.25f * m_kippX[1]) + (0.25f * m_kippX[2]) + (0.25f * m_kippX[3]));
	}
	public static void setHandykipplageX(float handykipplageX) {
		m_kippX[3] = m_kippX[2];
		m_kippX[2] = m_kippX[1];
		m_kippX[1] = m_kippX[0];
		m_kippX[0] = handykipplageX;
		//BsDataholder.handykipplageX = handykipplageX;
	}
	public static float getHandykipplageY() {
		if (m_kippY[3] == 0.0)
			return m_kippY[0];
		else
			return ((0.25f * m_kippY[0]) + (0.25f * m_kippY[1]) + (0.25f * m_kippY[2]) + (0.25f * m_kippY[3]));
	}
	public static void setHandykipplageY(float handykipplageY) {
		m_kippY[3] = m_kippY[2];
		m_kippY[2] = m_kippY[1];
		m_kippY[1] = m_kippY[0];
		m_kippY[0] = handykipplageY;
		//BsDataholder.handykipplageY = handykipplageY;
	}
	public static float getHandykipplageZ() {
		if (m_kippZ[3] == 0.0)
			return m_kippZ[0];
		else
			return ((0.25f * m_kippZ[0]) + (0.25f * m_kippZ[1]) + (0.25f * m_kippZ[2]) + (0.25f * m_kippZ[3]));
	}
	public static void setHandykipplageZ(float handykipplageZ) {
		m_kippZ[3] = m_kippZ[2];
		m_kippZ[2] = m_kippZ[1];
		m_kippZ[1] = m_kippZ[0];
		m_kippZ[0] = handykipplageZ;
		//BsDataholder.handykipplageZ = handykipplageZ;
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
