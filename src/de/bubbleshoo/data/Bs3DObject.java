/**
 * 
 */
package de.bubbleshoo.data;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import de.bubbleshoo.graphics.BsShader;

import android.R.bool;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

/**
 * @author oliverl
 *
 */
public class Bs3DObject {
	/*************************
     * PROPERTIES
     ************************/
	// Constants
	private static final int FLOAT_SIZE_BYTES 						= 4;
    private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES 	= 8 * FLOAT_SIZE_BYTES;
    private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET 		= 0;
    private static final int TRIANGLE_VERTICES_DATA_NOR_OFFSET 		= 3;
    private static final int TRIANGLE_VERTICES_DATA_TEX_OFFSET 		= 6;
    
	// light parameters
//    private float[] lightPos;
//    private float[] lightColor;
    /**
	 * @uml.property  name="lightAmbient" multiplicity="(0 -1)" dimension="1"
	 */
    private float[] lightAmbient;
    /**
	 * @uml.property  name="lightDiffuse" multiplicity="(0 -1)" dimension="1"
	 */
    private float[] lightDiffuse;
    // angle rotation for light
    /**
	 * @uml.property  name="angle"
	 */
    float angle = 0.0f;
    /**
	 * @uml.property  name="lightRotate"
	 */
    boolean lightRotate = true; 
    
    
//    // material properties
//    private float[] matAmbient;
//    private float[] matDiffuse;
//    private float[] matSpecular;
//    private float matShininess;
    
    // eye pos
    /**
	 * @uml.property  name="eyePos" multiplicity="(0 -1)" dimension="1"
	 */
    private float[] eyePos = {-5.0f, 0.0f, 0.0f};
    
	/**
	 * @uml.property  name="mScaleMatrix" multiplicity="(0 -1)" dimension="1"
	 */
	private float[] mScaleMatrix = new float[16];   // scaling
    /**
	 * @uml.property  name="mRotXMatrix" multiplicity="(0 -1)" dimension="1"
	 */
    private float[] mRotXMatrix = new float[16];    // rotation x
    /**
	 * @uml.property  name="mRotYMatrix" multiplicity="(0 -1)" dimension="1"
	 */
    private float[] mRotYMatrix = new float[16];    // rotation y
    /**
	 * @uml.property  name="mRotMatrix" multiplicity="(0 -1)" dimension="1"
	 */
    private float[] mRotMatrix = new float[16];		// Rotation
    /**
	 * @uml.property  name="mTransMatrix" multiplicity="(0 -1)" dimension="1"
	 */
    private float[] mTransMatrix = new float[16];	// Translation
    /**
	 * @uml.property  name="mMMatrix" multiplicity="(0 -1)" dimension="1"
	 */
    private float[] mMMatrix = new float[16];       // rotation
    /**
	 * @uml.property  name="normalMatrix" multiplicity="(0 -1)" dimension="1"
	 */
    private float[] normalMatrix = new float[16];   // modelview normal
    
	// scaling
    /**
	 * @uml.property  name="scaleX"
	 */
    private float scaleX = 1.0f;
    /**
	 * @uml.property  name="scaleY"
	 */
    private float scaleY = 1.0f;
    /**
	 * @uml.property  name="scaleZ"
	 */
    private float scaleZ = 1.0f;
    
    // rotation 
    /**
	 * @uml.property  name="mAngleX"
	 */
    private float mAngleX;
    /**
	 * @uml.property  name="mAngleY"
	 */
    private float mAngleY;
    
    // translating
    /**
	 * @uml.property  name="m_transX"
	 */
    private float m_transX = 0.0f;
    /**
	 * @uml.property  name="m_transY"
	 */
    private float m_transY = 0.0f;
    /**
	 * @uml.property  name="m_transZ"
	 */
    private float m_transZ = 0.0f;
    
    // Context
    /**
	 * @uml.property  name="context"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    Context context;
        
    // Mesh
    /**
	 * @uml.property  name="mesh"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    BsMesh mesh;                                              // The mesh of triangles
    /**
	 * @uml.property  name="meshID"
	 */
    int meshID;                                             // Mesh file (.OFF) from resources
    
    // texture
    /**
	 * @uml.property  name="hasTexture"
	 */
    private boolean hasTexture;
    /**
	 * @uml.property  name="texFiles" multiplicity="(0 -1)" dimension="1"
	 */
    private int[] texFiles;
    /**
	 * @uml.property  name="_texIDs" multiplicity="(0 -1)" dimension="1"
	 */
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
            this.context = context;
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
    public void drawObject(int nProgram, float[] muMVPMatrix, float[] mVMatrix, float[] mProjMatrix,
    		float[] lightPos, float[] lightColor, float[] matAmbient, float[] matDiffuse, float[] matSpecular, float matShininess) {
        // Start using the shader
        GLES20.glUseProgram(nProgram);
        checkGlError("glUseProgram");
        
        Matrix.multiplyMM(muMVPMatrix, 0, mVMatrix, 0, this.getModelMatrix(), 0);
        Matrix.multiplyMM(muMVPMatrix, 0, mProjMatrix, 0, muMVPMatrix, 0);

        // send to the shader
        GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(nProgram, "uMVPMatrix"), 1, false, muMVPMatrix, 0);

        // Create the normal modelview matrix
        // Invert + transpose of mvpmatrix
        Matrix.invertM(normalMatrix, 0, muMVPMatrix, 0);
        Matrix.transposeM(normalMatrix, 0, normalMatrix, 0);

        // send to the shader
        GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(nProgram, "normalMatrix"), 1, false, normalMatrix, 0);

        // lighting variables
        // send to shaders
        GLES20.glUniform4fv(GLES20.glGetUniformLocation(nProgram, "lightPos"), 1, lightPos, 0);
        GLES20.glUniform4fv(GLES20.glGetUniformLocation(nProgram, "lightColor"), 1, lightColor, 0);

        // material 
        GLES20.glUniform4fv(GLES20.glGetUniformLocation(nProgram, "matAmbient"), 1, matAmbient, 0);
        GLES20.glUniform4fv(GLES20.glGetUniformLocation(nProgram, "matDiffuse"), 1, matDiffuse, 0);
        GLES20.glUniform4fv(GLES20.glGetUniformLocation(nProgram, "matSpecular"), 1, matSpecular, 0);
        GLES20.glUniform1f(GLES20.glGetUniformLocation(nProgram, "matShininess"), matShininess);

        // eyepos
        GLES20.glUniform3fv(GLES20.glGetUniformLocation(nProgram, "eyePos")/*shader.eyeHandle*/, 1, eyePos, 0);

        /*** DRAWING OBJECT **/
        // Get buffers from mesh
        BsMesh mesh = this.getMesh();
        FloatBuffer _vb = mesh.get_vb();
        ShortBuffer _ib = mesh.get_ib();

        short[] _indices = mesh.get_indices();

        // Textures

        // the vertex coordinates
        _vb.position(TRIANGLE_VERTICES_DATA_POS_OFFSET);
        GLES20.glVertexAttribPointer(GLES20.glGetAttribLocation(nProgram, "aPosition")/*shader.maPositionHandle*/, 3, GLES20.GL_FLOAT, false,
                        TRIANGLE_VERTICES_DATA_STRIDE_BYTES, _vb);
        GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(nProgram, "aPosition"));//shader.maPositionHandle);

        // the normal info
        _vb.position(TRIANGLE_VERTICES_DATA_NOR_OFFSET);
        GLES20.glVertexAttribPointer(GLES20.glGetAttribLocation(nProgram, "aNormal")/*shader.maNormalHandle*/, 3, GLES20.GL_FLOAT, false,
                        TRIANGLE_VERTICES_DATA_STRIDE_BYTES, _vb);
        GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(nProgram, "aNormal"));//shader.maNormalHandle);

        // Texture info

        // bind textures
        if (this.hasTexture()) {// && enableTexture) {
                // number of textures
                int[] texIDs = this.get_texID(); 
                
                //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);//_texIDs[0]);
                for(int i = 0; i < _texIDs.length; i++) {
                        //if (_currentShader != 2 && i == 1)
                        //      continue;
                        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + i);
                        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texIDs[i]);
                        GLES20.glUniform1i(GLES20.glGetUniformLocation(nProgram, "texture" + (i+1)), i);
                }
        }

        // enable texturing? [fix - sending float is waste]
        GLES20.glUniform1f(GLES20.glGetUniformLocation(nProgram, "hasTexture")/*shader.hasTextureHandle*/, this.hasTexture()? 2.0f : 0.0f);

        _vb.position(TRIANGLE_VERTICES_DATA_TEX_OFFSET);
        GLES20.glVertexAttribPointer(GLES20.glGetAttribLocation(nProgram, "textureCoord")/*shader.maTextureHandle*/, 2, GLES20.GL_FLOAT, false,
                        TRIANGLE_VERTICES_DATA_STRIDE_BYTES, _vb);
        GLES20.glEnableVertexAttribArray(GLES20.glGetAttribLocation(nProgram, "textureCoord"));//GLES20.glEnableVertexAttribArray(shader.maTextureHandle);

        // Draw with indices
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, _indices.length, GLES20.GL_UNSIGNED_SHORT, _ib);
        checkGlError("glDrawElements");
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

    /** Bewegt die Aktuelle Postion um die Parameter 
     * 
     * @param WieVielUmX Wieviel soll er um x sich bewegen
     * @param WieVielUmY wieviel soll er sich um y bewegen
     */
    public void move(float WieVielUmX,  float WieVielUmY)
    {
    	this.m_transX+=WieVielUmX;
    	this.m_transY+=WieVielUmY;
    }
    
    /**
	 * GET/SET
	 * @uml.property  name="mesh"
	 */
    public BsMesh getMesh() {
            return mesh;
    }

    /**
	 * @param mesh
	 * @uml.property  name="mesh"
	 */
    public void setMesh(BsMesh mesh) {
            this.mesh = mesh;
    }

    /**
	 * @return
	 * @uml.property  name="meshID"
	 */
    public int getMeshID() {
            return meshID;
    }

    /**
	 * @param meshID
	 * @uml.property  name="meshID"
	 */
    public void setMeshID(int meshID) {
            this.meshID = meshID;
    }

    public boolean hasTexture() {
            return hasTexture;
    }

    /**
	 * @param hasTexture
	 * @uml.property  name="hasTexture"
	 */
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
    
    // debugging opengl
    private void checkGlError(String op) {
            int error;
            while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
                    Log.e("Bs3DObject", op + ": glError " + error);
                    throw new RuntimeException(op + ": glError " + error);
            }
    }
}

