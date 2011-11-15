package de.bubbleshoo.data;

import java.util.ArrayList;

import de.bubbleshoo.camera.Camera3D;
import de.bubbleshoo.graphics.AMaterial;
import de.bubbleshoo.graphics.TextureManager.TextureInfo;


public interface IObject3D {
	public void render(Camera3D camera, float[] projMatrix, float[] vMatrix);
	public void render(Camera3D camera, float[] projMatrix, float[] vMatrix, float[] parentMatrix);
	public void addTexture(TextureInfo textureInfo);
	public void setData(float[] vertices, float[] normals, float[] textureCoords, short[] indices);

	public void setPosition(float x, float y, float z);
	public void setScreenCoordinates(float x, float y, int viewportWidth, int viewportHeight, float eyeZ);
	public void setX(float x);
	public float getX();
	public void setY(float y);
	public float getY();
	public void setZ(float z);
	public float getZ();

	public void setRotation(float rotX, float rotY, float rotZ);
	public void setRotX(float rotX);
	public float getRotX();
	public void setRotY(float rotY);
	public float getRotY();
	public void setRotZ(float rotZ);
	public float getRotZ();

	public void setScale(float scale);
	public void setScale(float scaleX, float scaleY, float scaleZ);
	public void setScaleX(float scaleX);
	public float getScaleX();
	public void setScaleY(float scaleY);
	public float getScaleY();
	public void setScaleZ(float scaleZ);
	public float getScaleZ();

	public float[] getModelMatrix();
	
	public void addChild(BaseObject3D child);
	public int getNumChildren();
	public void setShader(AMaterial shader);
	
	public ArrayList<TextureInfo> getTextureInfoList();
}
