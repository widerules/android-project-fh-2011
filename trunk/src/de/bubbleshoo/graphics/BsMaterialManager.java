/**
 * 
 */
package de.bubbleshoo.graphics;

import java.util.HashMap;

/**
 * @author OLaudi
 *
 */
public class BsMaterialManager {
	/**
	 * Verschachtelte HashMap [Materialbezeichnung]-->[Material]
	 */
	private HashMap<String, AMaterial>	m_MaterialMap;
	
	/**
	 * Constructor
	 */
	public BsMaterialManager() {
		loadMaterials();
	}
	
	/**
	 * Läd alle Shader
	 */
	private void loadMaterials() {
		m_MaterialMap = new HashMap<String, AMaterial>();
		m_MaterialMap.put("SimpleMaterial", new SimpleMaterial());
	}
	
	/**
	 * Liefert das Material anhand der übergeben Shader
	 * @param strMaterial
	 * @return
	 */
	public AMaterial getMaterialFromShader(String strMaterial) {
		return new SimpleMaterial(m_MaterialMap.get(strMaterial));
	}
}
