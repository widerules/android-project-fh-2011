package de.bubbleshoo.data;

import de.bubbleshoo.data.BaseObject3D;

/**
 * @author  Admin
 */
public interface IParser {
	public void parse();
	/**
	 * @uml.property  name="parsedObject"
	 * @uml.associationEnd  
	 */
	public BaseObject3D getParsedObject();
}
