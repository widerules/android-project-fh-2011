package de.bubbleshoo.data;

import de.bubbleshoo.data.BaseObject3D;
import de.bubbleshoo.graphics.TextureManager;
import android.content.res.Resources;


public abstract class AParser implements IParser {
	protected TextureManager mTextureManager;
	protected Resources mResources;
	protected int mResourceId;
	
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
