package de.bubbleshoo.data;

import de.bubbleshoo.data.BaseObject3D;
import de.bubbleshoo.graphics.TextureManager;
import android.content.res.Resources;


public abstract class AParser implements IParser {
	/**
	 * @uml.property  name="mTextureManager"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected TextureManager mTextureManager;
	/**
	 * @uml.property  name="mResources"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected Resources mResources;
	/**
	 * @uml.property  name="mResourceId"
	 */
	protected int mResourceId;
	
	/**
	 * @uml.property  name="mRootObject"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected BaseObject3D mRootObject;

	public AParser(Resources resources, TextureManager textureManager, int resourceId) {
		mTextureManager = textureManager;
		mResources = resources;
		mResourceId = resourceId;
		mRootObject = new BaseObject3D();
	}
	
	
	public void parse() {
	}

	
	public BaseObject3D getParsedObject() {
		return mRootObject;
	}
}
