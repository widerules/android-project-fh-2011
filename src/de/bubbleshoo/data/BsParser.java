/**
 * 
 */
package de.bubbleshoo.data;

import java.util.HashMap;

import de.bubbleshoo.graphics.TextureManager;

/**
 * @author OLaudi
 *
 */
public class BsParser {

	/**
	 * Handle for the TextureManager
	 */
	protected TextureManager 				m_TextureManager;
	
	/**
	 * Contains all parsed Objects
	 * Access over Objetname
	 */
	protected HashMap<String, ObjParser> 	m_parsedObjMap;
	
	/**
	 * Constructor
	 * @param textureManager
	 */
	public BsParser(TextureManager textureManager) {
		m_TextureManager = textureManager;
	}
	
	/**
	 * loads all available Objects into the Hashmap m_parsedObjMap
	 */
	private void loadObjects() {
		// TODO
	}
	
	/**
	 * Returns the requested BaseObject3D by Name
	 * @param strName
	 * @return
	 */
	public BaseObject3D getObjectByName(String strName) {
		return m_parsedObjMap.get(strName).getParsedObject().getChildByName(strName);
	}
}
