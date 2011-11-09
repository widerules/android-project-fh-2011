/**
 * 
 */
package de.bubbleshoo.data;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

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
	        private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 8 * FLOAT_SIZE_BYTES;
	        private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
	        private static final int TRIANGLE_VERTICES_DATA_NOR_OFFSET = 3;
	        private static final int TRIANGLE_VERTICES_DATA_TEX_OFFSET = 6;
	        
	        private float[] mScaleMatrix = new float[16];   // scaling
	        private float[] mRotXMatrix = new float[16];    // rotation x
	        private float[] mRotYMatrix = new float[16];    // rotation y
	        private float[] mRotMatrix = new float[16];		// Rotation
	        private float[] mTransMatrix = new float[16];	// Translation
	        private float[] mMMatrix = new float[16];       // rotation
	        
	        /**
	    	 * Modelviewprojectionmatrix (proj * view)
	    	 */
	    	private float[] mMVPMatrix = new float[16];
	        
	        // scaling
	        private float scaleX = 1.0f;
	        private float scaleY = 1.0f;
	        private float scaleZ = 1.0f;
	        
	        // rotation 
	        private float mAngleX;
	        private float mAngleY;
	        
	        // translating
	        private float m_transX = 0.0f;
	        private float m_transY = 0.0f;
	        private float m_transZ = 0.0f;
	        
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
	        private float[] getModelMatrix() {
	        	float tempMatrix[] = new float[16];
	        	// scaling
                Matrix.setIdentityM(mScaleMatrix, 0);
                Matrix.scaleM(mScaleMatrix, 0, scaleX, scaleY, scaleZ);
                // Rotation along x
                Matrix.setRotateM(mRotXMatrix, 0, this.mAngleY, -1.0f, 0.0f, 0.0f);
                Matrix.setRotateM(mRotYMatrix, 0, this.mAngleX, 0.0f, 1.0f, 0.0f);
                Matrix.multiplyMM(mRotMatrix, 0, mRotYMatrix, 0, mRotXMatrix, 0);
                                
                Matrix.multiplyMM(tempMatrix, 0, mScaleMatrix, 0, mRotMatrix, 0);
                // Translate Object
                Matrix.translateM(mTransMatrix, 0, tempMatrix, 0, this.m_transX, this.m_transY, this.m_transZ);
                mMMatrix = mTransMatrix;
                return mMMatrix;
	        }
	        
	        public void drawObject(int nProgram, int muMVPMatrixHandle, float[] mVMatrix, float[] mProjMatrix) {
	        	GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);
	        	
	        	Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, this.getModelMatrix(), 0);
	            Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
	            
	        	// Add program to OpenGL environment
	            GLES20.glUseProgram(nProgram);
	            
	            // the vertex coordinates
	            FloatBuffer _vb = this.getMesh().get_vb();
	            _vb.position(TRIANGLE_VERTICES_DATA_POS_OFFSET);
	            GLES20.glVertexAttribPointer(GLES20.glGetAttribLocation(nProgram, "vPosition")/*shader.maPositionHandle*/, 
	            		3, GLES20.GL_FLOAT, false, TRIANGLE_VERTICES_DATA_STRIDE_BYTES, _vb);
	            GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(nProgram, "vPosition"));//shader.maPositionHandle);

	            // the normal info
//	            _vb.position(TRIANGLE_VERTICES_DATA_NOR_OFFSET);
//	            GLES20.glVertexAttribPointer(GLES20.glGetAttribLocation(this.mProgram, "aNormal")/*shader.maNormalHandle*/, 
//	            		3, GLES20.GL_FLOAT, false, TRIANGLE_VERTICES_DATA_STRIDE_BYTES, _vb);
//	            GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(this.mProgram, "aNormal"));//shader.maNormalHandle);
	            
	            
	            // Draw with indices
	            ShortBuffer _ib = this.getMesh().get_ib();
	            short[] _indices = this.getMesh().get_indices();
	            GLES20.glDrawElements(GLES20.GL_TRIANGLES, _indices.length, GLES20.GL_UNSIGNED_SHORT, _ib);
	        }
	        
	        /**
	         * Skaliert das Object
	         * @param x
	         * @param y
	         * @param z
	         */
	        public void setScale(float x, float y, float z) {
	        	scaleX = x;
	        	scaleY = y;
	        	scaleZ = z;
	        }
	        
	        /**
	         * Set the Scale to identity (1, 1, 1)
	         */
	        public void setScaleIdentity() {
	        	scaleX = 1.0f;
	        	scaleY = 1.0f;
	        	scaleZ = 1.0f;
	        }
	        
	        /**
			 * @return the m_transX
			 */
			public float getTransX() {
				return m_transX;
			}

			/**
			 * @return the m_transY
			 */
			public float getTransY() {
				return m_transY;
			}

			/**
			 * @return the m_transZ
			 */
			public float getTransZ() {
				return m_transZ;
			}
			
			/**
			 * translate the object to a position
			 * @param x
			 * @param y
			 * @param z
			 */
			public void setTranslate(float x, float y, float z) {
				this.m_transX += x;
				this.m_transY += y;
				this.m_transZ += z;
			}
			
			/**
			 * Set Translateidentity (0, 0, 0)
			 */
			public void setTranslateIdentity() {
				this.m_transX = 0.0f;
				this.m_transY = 0.0f;
				this.m_transZ = 0.0f;
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

