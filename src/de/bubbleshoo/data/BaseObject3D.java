package de.bubbleshoo.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import de.bubbleshoo.light.ALight;
import de.bubbleshoo.mapElemente.MapElement;
import de.bubbleshoo.units.Unit;
import de.bubbleshoo.camera.Camera3D;
import de.bubbleshoo.graphics.AMaterial;
import de.bubbleshoo.graphics.TextureManager.TextureInfo;

import android.opengl.GLES20;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.util.Log;

public class BaseObject3D implements IObject3D, Comparable<BaseObject3D> {
	
	public static String TAG = "BubbleShooTag";


	/**
	 * @uml.property  name="fLOAT_SIZE_BYTES"
	 */
	protected final int FLOAT_SIZE_BYTES = 4;
	/**
	 * @uml.property  name="sHORT_SIZE_BYTES"
	 */
	protected final int SHORT_SIZE_BYTES = 2;

	/**
	 * @uml.property  name="x"
	 */
	protected float x;


	/**
	 * @uml.property  name="y"
	 */
	protected float y;


	/**
	 * @uml.property  name="z"
	 */
	protected float z;


	/**
	 * @uml.property  name="rotX"
	 */
	protected float rotX;


	/**
	 * @uml.property  name="rotY"
	 */
	protected float rotY;


	/**
	 * @uml.property  name="rotZ"
	 */
	protected float rotZ;


	/**
	 * @uml.property  name="scaleX"
	 */
	protected float scaleX = 1.0f;


	/**
	 * @uml.property  name="scaleY"
	 */
	protected float scaleY = 1.0f;


	/**
	 * @uml.property  name="scaleZ"
	 */
	protected float scaleZ = 1.0f;
	/**
	 * @uml.property  name="numVertices"
	 */
	protected int numVertices;

	/**
	 * @uml.property  name="mMVPMatrix" multiplicity="(0 -1)" dimension="1"
	 */
	protected float[] mMVPMatrix = new float[16];
	/**
	 * @uml.property  name="mMMatrix" multiplicity="(0 -1)" dimension="1"
	 */
	protected float[] mMMatrix = new float[16];
	/**
	 * @uml.property  name="mProjMatrix" multiplicity="(0 -1)" dimension="1"
	 */
	protected float[] mProjMatrix;
	float[] tmpMatrix = new float[16];

	/**
	 * @uml.property  name="mScalematrix" multiplicity="(0 -1)" dimension="1"
	 */
	protected float[] mScalematrix = new float[16];
	/**
	 * @uml.property  name="mTranslateMatrix" multiplicity="(0 -1)" dimension="1"
	 */
	protected float[] mTranslateMatrix = new float[16];
	/**
	 * @uml.property  name="mRotateMatrix" multiplicity="(0 -1)" dimension="1"
	 */
	protected float[] mRotateMatrix = new float[16];

	/**
	 * @uml.property  name="mVertices"
	 */
	protected FloatBuffer mVertices;
	/**
	 * @uml.property  name="mNormals"
	 */
	protected FloatBuffer mNormals;
	/**
	 * @uml.property  name="mTextureCoords"
	 */
	protected FloatBuffer mTextureCoords;
	/**
	 * @uml.property  name="mIndices"
	 */
	protected ShortBuffer mIndices;
	/**
	 * @uml.property  name="mNumIndices"
	 */
	protected int mNumIndices;

	/**
	 * @uml.property  name="mMaterial"
	 * @uml.associationEnd  
	 */
	protected AMaterial mMaterial;
	/**
	 * @uml.property  name="mLight"
	 * @uml.associationEnd  
	 */
	protected ALight mLight;

	/**
	 * @uml.property  name="mChildren"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="de.bubbleshoo.data.BaseObject3D"
	 */
	protected ArrayList<BaseObject3D> mChildren;
	/**
	 * @uml.property  name="mNumChildren"
	 */
	protected int mNumChildren;
	/**
	 * @uml.property  name="mName"
	 */
	protected String mName;

	/**
	 * @uml.property  name="mDoubleSided"
	 */
	protected boolean mDoubleSided = false;
	/**
	 * @uml.property  name="mTransparent"
	 */
	protected boolean mTransparent = false;
	/**
	 * @uml.property  name="mForcedDepth"
	 */
	protected boolean mForcedDepth = false;
	/**
	 * @uml.property  name="mDrawingMode"
	 */
	protected int mDrawingMode = GLES20.GL_TRIANGLES;

	/**
	 * @uml.property  name="mIsContainerOnly"
	 */
	protected boolean mIsContainerOnly = true;
	
	protected boolean mMatricesChanged = true;

	public BaseObject3D() {
		mChildren = new ArrayList<BaseObject3D>();
		doTransformations(null);
	}

	public BaseObject3D(String name) {
		this();
		mName = name;
	}

	public BaseObject3D(BaseObject3D objSrc) {
		this();
		SerializedObject3D ser = objSrc.toSerializedObject3D();
		setData(ser.getVertices(), ser.getNormals(), ser.getTextureCoords(),
				ser.getIndices());
		this.setPosition(objSrc.getX(), objSrc.getY(), objSrc.getZ());
		this.setRotation(objSrc.getRotX(), objSrc.getRotY(), objSrc.getRotZ());
		this.setScale(objSrc.getScaleX(), objSrc.getScaleY(),
				objSrc.getScaleZ());
		this.setMaterial(objSrc.getMaterial(), true);
		this.setLight(objSrc.getLight());
		this.setName(objSrc.getName());
	}

	public BaseObject3D(SerializedObject3D ser) {
		this();
		setData(ser.getVertices(), ser.getNormals(), ser.getTextureCoords(),
				ser.getIndices());
	}

	public void setData(float[] vertices, float[] normals,
			float[] textureCoords, short[] indices) {
		mVertices = ByteBuffer
				.allocateDirect(vertices.length * FLOAT_SIZE_BYTES)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mVertices.put(vertices).position(0);

		mNormals = ByteBuffer.allocateDirect(normals.length * FLOAT_SIZE_BYTES)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mNormals.put(normals).position(0);

		mTextureCoords = ByteBuffer
				.allocateDirect(textureCoords.length * FLOAT_SIZE_BYTES)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mTextureCoords.put(textureCoords).position(0);

		mIndices = ByteBuffer.allocateDirect(indices.length * SHORT_SIZE_BYTES)
				.order(ByteOrder.nativeOrder()).asShortBuffer();
		mIndices.put(indices).position(0);

		mNumIndices = indices.length;

		mIsContainerOnly = false;
	}

	public void render(Camera3D camera, float[] projMatrix, float[] vMatrix) {
		render(camera, projMatrix, vMatrix, null);
	}

	public void render(Camera3D camera, float[] projMatrix, float[] vMatrix,
			final float[] parentMatrix) {
		if (!mIsContainerOnly) {
			mProjMatrix = projMatrix;
			if (!mDoubleSided)
				GLES20.glEnable(GLES20.GL_CULL_FACE);
			if (mTransparent) {
				GLES20.glEnable(GLES20.GL_BLEND);
				GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
				GLES20.glDepthMask(false);
			} else {
				GLES20.glEnable(GLES20.GL_DEPTH_TEST);
				GLES20.glDepthMask(true);
			}

			mMaterial.useProgram();
			mMaterial.bindTextures();
			mMaterial.setCamera(camera);
			mMaterial.setVertices(mVertices);
			mMaterial.setTextureCoords(mTextureCoords);
			mMaterial.setNormals(mNormals);

			setShaderParams();
		}

		// Standard-Transformations
		if (mMatricesChanged)
			doTransformations(parentMatrix);
		
		Matrix.multiplyMM(mMVPMatrix, 0, vMatrix, 0, mMMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, projMatrix, 0, mMVPMatrix, 0);

		if (!mIsContainerOnly) {
			mMaterial.setMVPMatrix(mMVPMatrix);
			mMaterial.setModelMatrix(mMMatrix);
			mMaterial.setViewMatrix(vMatrix);

			mIndices.position(0);
			GLES20.glDrawElements(mDrawingMode, mNumIndices,
					GLES20.GL_UNSIGNED_SHORT, mIndices);

			GLES20.glDisable(GLES20.GL_CULL_FACE);
			GLES20.glDisable(GLES20.GL_BLEND);
			GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		}
		
		int i;
		for (i = 0; i < mNumChildren; ++i) {
			mChildren.get(i).render(camera, projMatrix, vMatrix, mMMatrix);
		}
	}

	protected void setShaderParams() {
		mMaterial.setLight(mLight);
	};

	protected void doTransformations(final float[] parentMatrix) {
		Matrix.setIdentityM(mMMatrix, 0);
		Matrix.setIdentityM(mScalematrix, 0);
		Matrix.scaleM(mScalematrix, 0, scaleX, scaleY, scaleZ);

		Matrix.setIdentityM(mRotateMatrix, 0);

		Matrix.rotateM(mRotateMatrix, 0, (float) rotX, 1.0f, 0.0f, 0.0f);
		Matrix.rotateM(mRotateMatrix, 0, (float) rotY, 0.0f, 1.0f, 0.0f);
		Matrix.rotateM(mRotateMatrix, 0, (float) rotZ, 0.0f, 0.0f, 1.0f);

		Matrix.translateM(mMMatrix, 0, x, y, z);
		
		Matrix.multiplyMM(tmpMatrix, 0, mMMatrix, 0, mScalematrix, 0);
		Matrix.multiplyMM(mMMatrix, 0, tmpMatrix, 0, mRotateMatrix, 0);
		
		if (parentMatrix != null) {
			Matrix.multiplyMM(tmpMatrix, 0, parentMatrix, 0, mMMatrix, 0);
			mMMatrix = tmpMatrix;
		}
		
		mMatricesChanged = false;
	}

	public void addTexture(TextureInfo textureInfo) {
		mMaterial.addTexture(textureInfo);
	}

	protected void checkGlError(String op) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {

			Log.e(TAG, op + ": glError " + error + " in class "
					+ this.getClass().getName());
			throw new RuntimeException(op + ": glError " + error);
		}
	}

	public void setPosition(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setScreenCoordinates(float x, float y, int viewportWidth,
			int viewportHeight, float eyeZ) {
		float[] r1 = new float[16];
		int[] viewport = new int[] { 0, 0, viewportWidth, viewportHeight };
		float[] modelMatrix = new float[16];
		Matrix.setIdentityM(modelMatrix, 0);

		GLU.gluUnProject(x, viewportHeight - y, 0.0f, modelMatrix, 0,
				mProjMatrix, 0, viewport, 0, r1, 0);
		setPosition(r1[0] * eyeZ, r1[1] * -eyeZ, 0);
	}

	public int compareTo(BaseObject3D another) {
		if (mForcedDepth)
			return -1;
		if (z < another.z)
			return 1;
		else if (z > another.z)
			return -1;
		else
			return 0;
	}

	public void addChild(BaseObject3D child) {
		mChildren.add(child);
		mNumChildren++;
	}

	public SerializedObject3D toSerializedObject3D() {
		SerializedObject3D ser = new SerializedObject3D(mVertices.capacity(),
				mNormals.capacity(), mTextureCoords.capacity(),
				mIndices.capacity());

		int i;

		for (i = 0; i < mVertices.capacity(); i++)
			ser.getVertices()[i] = mVertices.get(i);
		for (i = 0; i < mNormals.capacity(); i++)
			ser.getNormals()[i] = mNormals.get(i);
		for (i = 0; i < mTextureCoords.capacity(); i++)
			ser.getTextureCoords()[i] = mTextureCoords.get(i);
		for (i = 0; i < mIndices.capacity(); i++)
			ser.getIndices()[i] = mIndices.get(i);

		return ser;
	}
	
	  /** Bewegt die Aktuelle Postion um die Parameter 
     * 
     * @param WieVielUmX Wieviel soll er um x sich bewegen
     * @param WieVielUmY wieviel soll er sich um y bewegen
     */
    public void move(float WieVielUmX,  float WieVielUmY)
    {
		mMatricesChanged = true;
    	this.x+=WieVielUmX;
    	this.y+=WieVielUmY;
    }
    
    /** liefert die x y und z Koordinaten.
     * 
     */
    public float[] getPosition()
    {
    	float[] returnfloat = {
    			this.x,
    			this.y,
    			this.z
    	};
    	return returnfloat;
    	
    }
    
	/*
	 * Getter /Setter
	 */
	public int getNumChildren() {
		return mNumChildren;
	}

	public BaseObject3D getChildAt(int index) {
		return mChildren.get(index);
	}

	public BaseObject3D getChildByName(String name) {
		for (int i = 0; i < mNumChildren; ++i)
			if (mChildren.get(i).getName().equals(name))
				return mChildren.get(i);

		return null;
	}

	public void setShader(AMaterial material) {
		setMaterial(material, true);
	}

	public void setMaterial(AMaterial material, boolean copyTextures) {
		if (mMaterial != null && copyTextures)
			mMaterial.copyTexturesTo(material);
		mMaterial = material;
	}

	public AMaterial getMaterial() {
		return mMaterial;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getName() {
		return mName;
	}

	public boolean isForcedDepth() {
		return mForcedDepth;
	}

	public void setForcedDepth(boolean forcedDepth) {
		this.mForcedDepth = forcedDepth;
	}

	public ArrayList<TextureInfo> getTextureInfoList() {
		int i;
		ArrayList<TextureInfo> ti = mMaterial.getTextureInfoList();

		for (i = 0; i < mNumChildren; ++i) {
			ti.addAll(mChildren.get(i).getTextureInfoList());
		}
		return ti;
	}

	public float[] getModelMatrix() {
		return mMMatrix;
	}

	/**
	 * @param x
	 * @uml.property  name="x"
	 */
	public void setX(float x) {
		mMatricesChanged = true;
		this.x = x;
	}

	/**
	 * @return
	 * @uml.property  name="x"
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param y
	 * @uml.property  name="y"
	 */
	public void setY(float y) {
		mMatricesChanged = true;
		this.y = y;
	}

	/**
	 * @return
	 * @uml.property  name="y"
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param z
	 * @uml.property  name="z"
	 */
	public void setZ(float z) {
		mMatricesChanged = true;
		this.z = z;
	}

	/**
	 * @return
	 * @uml.property  name="z"
	 */
	public float getZ() {
		return z;
	}

	public void setRotation(float rotX, float rotY, float rotZ) {
		mMatricesChanged = true;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
	}

	/**
	 * @param rotX
	 * @uml.property  name="rotX"
	 */
	public void setRotX(float rotX) {
		mMatricesChanged = true;
		this.rotX = rotX;
	}

	/**
	 * @return
	 * @uml.property  name="rotX"
	 */
	public float getRotX() {
		return rotX;
	}

	/**
	 * @param rotY
	 * @uml.property  name="rotY"
	 */
	public void setRotY(float rotY) {
		mMatricesChanged = true;
		this.rotY = rotY;
	}

	/**
	 * @return
	 * @uml.property  name="rotY"
	 */
	public float getRotY() {
		return rotY;
	}

	/**
	 * @param rotZ
	 * @uml.property  name="rotZ"
	 */
	public void setRotZ(float rotZ) {
		mMatricesChanged = true;
		this.rotZ = rotZ;
	}

	/**
	 * @return
	 * @uml.property  name="rotZ"
	 */
	public float getRotZ() {
		return rotZ;
	}

	public void setScale(float scale) {
		mMatricesChanged = true;
		this.scaleX = scale;
		this.scaleY = scale;
		this.scaleZ = scale;
	}

	public void setScale(float scaleX, float scaleY, float scaleZ) {
		mMatricesChanged = true;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.scaleZ = scaleZ;
	}

	/**
	 * @param scaleX
	 * @uml.property  name="scaleX"
	 */
	public void setScaleX(float scaleX) {
		mMatricesChanged = true;
		this.scaleX = scaleX;
	}

	/**
	 * @return
	 * @uml.property  name="scaleX"
	 */
	public float getScaleX() {
		return scaleX;
	}

	/**
	 * @param scaleY
	 * @uml.property  name="scaleY"
	 */
	public void setScaleY(float scaleY) {
		mMatricesChanged = true;
		this.scaleY = scaleY;
	}

	/**
	 * @return
	 * @uml.property  name="scaleY"
	 */
	public float getScaleY() {
		return scaleY;
	}

	/**
	 * @param scaleZ
	 * @uml.property  name="scaleZ"
	 */
	public void setScaleZ(float scaleZ) {
		mMatricesChanged = true;
		this.scaleZ = scaleZ;
	}

	/**
	 * @return
	 * @uml.property  name="scaleZ"
	 */
	public float getScaleZ() {
		return scaleZ;
	}

	public boolean isDoubleSided() {
		return mDoubleSided;
	}

	public void setDoubleSided(boolean doubleSided) {
		this.mDoubleSided = doubleSided;
	}

	public boolean isTransparent() {
		return mTransparent;
	}

	public void setTransparent(boolean transparent) {
		this.mTransparent = transparent;
	}

	public void setLight(ALight light) {
		mLight = light;
	}

	public ALight getLight() {
		return mLight;
	}

	public int getDrawingMode() {
		return mDrawingMode;
	}

	public void setDrawingMode(int drawingMode) {
		this.mDrawingMode = drawingMode;
	}
}
