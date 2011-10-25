/**
 * 
 */
package de.bubbleshoo.data;

import java.io.IOException;
import java.io.InputStream;

import android.R.bool;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

/**
 * @author oliverl
 *
 */
public class Bs3DObject {
	/**
	 * Class represents a 3D object. 
	 * Consists of a mesh of triangles, any textures, lighting properties, etc. 
	 */

	        /*************************
	         * PROPERTIES
	         ************************/
	        // Context
	        Context context;
	        
	        // Constants
	        private static final int FLOAT_SIZE_BYTES = 4;
	        
	        private float[] mScaleMatrix = new float[16];   // scaling
	        private float[] mRotXMatrix = new float[16];    // rotation x
	        private float[] mRotYMatrix = new float[16];    // rotation x
	        private float[] mMMatrix = new float[16];       // rotation
	        
	        // scaling
	        float scaleX = 1.0f;
	        float scaleY = 1.0f;
	        float scaleZ = 1.0f;
	        
	        // rotation 
	        public float mAngleX;
	        public float mAngleY;
	        
	        // Mesh
	        BsMesh mesh;                                              // The mesh of triangles
	        int meshID;                                             // Mesh file (.OFF) from resources
	        
	        // texture
	        private boolean hasTexture;
	        private int[] texFiles;
	        private int[] _texIDs;
	        
	        // lighting
	        
	        // etc.
	        
	        /***************************
	         * CONSTRUCTOR(S)
	         **************************/
	        public Bs3DObject(int meshID, boolean hasTexture, Context context) {
	                this(new int[0], meshID, hasTexture, context);
	        }
	        
	        public Bs3DObject(int[] texFile, int meshID, boolean hasTexture, Context context) {
	                this.texFiles = texFile;
	                this.meshID = meshID;
	                this.hasTexture = hasTexture;
	                
	                // the mesh
	                mesh = new BsMesh(meshID, context);
	                
	                // texture
	                _texIDs = new int[texFiles.length];
	                setupTexture(context);
	        } 
	        
	        /**************************
	         * OTHER METHODS
	         *************************/
	        
	        /*
	         * Calls the mesh draw functions
	         */
	        public void drawMesh() {
	                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
	                //GLES20.glDr
	        }
	        
	        /*
	         * Sets up the texture
	         */
	        public void setupTexture(Context context) {
	                if (!hasTexture)
	                        return;
	                
	                // create new texture ids
	                /*int[] textures = new int[1];
	        GLES20.glGenTextures(1, textures, 0);

	        _texID = textures[0];
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, _texID);

	        // parameters
	        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
	                GLES20.GL_NEAREST);
	        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
	                GLES20.GL_TEXTURE_MAG_FILTER,
	                GLES20.GL_LINEAR);

	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
	                GLES20.GL_REPEAT);
	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
	                GLES20.GL_REPEAT);

	        InputStream is = context.getResources()
	            .openRawResource(texFile);
	        Bitmap bitmap;
	        try {
	            bitmap = BitmapFactory.decodeStream(is);
	        } finally {
	            try {
	                is.close();
	            } catch(IOException e) {
	                // Ignore.
	            }
	        }

	        // create it 
	        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
	        bitmap.recycle();
	        Log.d("SETUP IMAGE", _texID + "");*/
	        }

	        
	        
	        
	        /***************************
	         * GET/SET
	         *************************/
	        public BsMesh getMesh() {
	                return mesh;
	        }

	        public void setMesh(BsMesh mesh) {
	                this.mesh = mesh;
	        }

	        public int getMeshID() {
	                return meshID;
	        }

	        public void setMeshID(int meshID) {
	                this.meshID = meshID;
	        }

	        public boolean hasTexture() {
	                return hasTexture;
	        }

	        public void setHasTexture(boolean hasTexture) {
	                this.hasTexture = hasTexture;
	        }

	        public int[] getTexFile() {
	                return texFiles;
	        }

	        public void setTexFile(int[] texFile) {
	                this.texFiles = texFile;
	        }

	        public int[] get_texID() {
	                return _texIDs;
	        }

	        public void set_texID(int[] _texid) {
	                _texIDs = _texid;
	        }
	        
	        /**
	         * returns the Modelmatrix mMMatrix
	         * calulated from scale and rotation
	         * @return
	         */
	        public float[] getModelMatrix() {
	        	// scaling
                Matrix.setIdentityM(mScaleMatrix, 0);
                Matrix.scaleM(mScaleMatrix, 0, scaleX, scaleY, scaleZ);
                // Rotation along x
                Matrix.setRotateM(mRotXMatrix, 0, this.mAngleY, -1.0f, 0.0f, 0.0f);
                Matrix.setRotateM(mRotYMatrix, 0, this.mAngleX, 0.0f, 1.0f, 0.0f);
                
                float tempMatrix[] = new float[16]; 
                Matrix.multiplyMM(tempMatrix, 0, mRotYMatrix, 0, mRotXMatrix, 0);
                Matrix.multiplyMM(mMMatrix, 0, mScaleMatrix, 0, tempMatrix, 0);
                
                return mMMatrix;
	        }
	        
	        /**
	         * Skaliert das Object
	         * @param x
	         * @param y
	         * @param z
	         */
	        public void scale(float x, float y, float z) {
	        	scaleX = x;
	        	scaleY = y;
	        	scaleZ = z;
	        }
	        
	        /**
	         * Set the Scale to identity (1, 1, 1)
	         */
	        public void scaleIdentity() {
	        	scaleX = 1.0f;
	        	scaleY = 1.0f;
	        	scaleZ = 1.0f;
	        }
	        
	        /**
			 * @return the mAngleX
			 */
			public float getAngleX() {
				return mAngleX;
			}

			/**
			 * @param mAngleX the mAngleX to set
			 */
			public void setAngleX(float AngleX) {
				this.mAngleX = AngleX;
			}

			/**
			 * @return the mAngleY
			 */
			public float getAngleY() {
				return mAngleY;
			}

			/**
			 * @param mAngleY the mAngleY to set
			 */
			public void setAngleY(float AngleY) {
				this.mAngleY = AngleY;
			}
	}

