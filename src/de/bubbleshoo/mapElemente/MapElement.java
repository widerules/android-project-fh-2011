package de.bubbleshoo.mapElemente;

import de.bubbleshoo.data.BaseObject3D;

public class MapElement {
	
	/**
	 * @uml.property  name="m_3dobject"
	 * @uml.associationEnd  
	 */
	private BaseObject3D m_3dobject;

	/**
	 * 
	 */
	public MapElement()
	{
		m_3dobject=null;
	}
	/** Konstruktor der ein Mapelement erzeugt.
	 * 
	 * @param emt
	 */
	public MapElement(BaseObject3D emt) {
		m_3dobject=emt;

	}


	/*
	 * Getter / Setter
	 */
	
	/**
	 * @return
	 * @uml.property  name="m_3dobject"
	 */
	public BaseObject3D getM_3dobject() {
		return m_3dobject;
	}
	/**
	 * @param m_3dobject
	 * @uml.property  name="m_3dobject"
	 */
	public void setM_3dobject(BaseObject3D m_3dobject) {
		this.m_3dobject = m_3dobject;
	}

	
}
