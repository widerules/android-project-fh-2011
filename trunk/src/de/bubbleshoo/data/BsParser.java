/**
 * 
 */
package de.bubbleshoo.data;

import java.util.HashMap;

import android.content.Context;

import de.bubbleshoo.graphics.SimpleMaterial;
import de.bubbleshoo.graphics.TextureManager;
import de.bubbleshoo.logic.LogicThread;
import de.bubbleshoo.main.R;

/**
 * @author OLaudi
 *
 */
public class BsParser {

	/**
	 * Handle for the TextureManager und Context
	 */
	protected TextureManager 				m_TextureManager;
	protected Context 						m_context;
	
	/**
	 * Contains all parsed Objects
	 * Access over Objetname
	 */
	protected HashMap<String, BaseObject3D> m_parsedObjMap;
	
	/**
	 * Constructor
	 * @param textureManager
	 */
	public BsParser(Context context, TextureManager textureManager) {
		m_TextureManager = textureManager;
		m_context = context;
		loadObjects();
	}
	
	/**
	 * loads all available Objects into the Hashmap m_parsedObjMap
	 */
	private void loadObjects() {
		m_parsedObjMap = new HashMap<String, BaseObject3D>();
		
		// Add Plane (Cube)
		ObjParser tmpParser = new ObjParser(
				m_context.getResources(),
				m_TextureManager, R.raw.plane);
		tmpParser.parse();
		m_parsedObjMap.put("Plane", tmpParser.getParsedObject().getChildByName("Plane"));
		
		// Add Sphere
		tmpParser = new ObjParser(
				m_context.getResources(),
				m_TextureManager, R.raw.sphere);
		tmpParser.parse();
		m_parsedObjMap.put("Sphere", tmpParser.getParsedObject().getChildByName("Sphere"));
	}
	
	/**
	 * Returns the requested BaseObject3D by Name
	 * @param strName
	 * @return
	 */
	public BaseObject3D getObjectByName(String strName) {
		BaseObject3D emt = new BaseObject3D(m_parsedObjMap.get(strName).toSerializedObject3D());
		emt.setShader(new SimpleMaterial());
		return emt;
	}
}
