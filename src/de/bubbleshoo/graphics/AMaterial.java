package de.bubbleshoo.graphics;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import de.bubbleshoo.camera.*;
import de.bubbleshoo.light.ALight;
import de.bubbleshoo.graphics.TextureManager.TextureInfo;

import android.opengl.GLES20;
import android.util.Log;


public abstract class AMaterial {
	/**
	 * @uml.property  name="mVertexShader"
	 */
	protected String mVertexShader;
 
	/**
	 * @uml.property  name="mFragmentShader"
	 */
	protected String mFragmentShader;
	
	/**
	 * @uml.property  name="mProgram"
	 */
	protected int mProgram;
	/**
	 * @uml.property  name="muMVPMatrixHandle"
	 */
	protected int muMVPMatrixHandle;
	/**
	 * @uml.property  name="maPositionHandle"
	 */
	protected int maPositionHandle;
	/**
	 * @uml.property  name="maTextureHandle"
	 */
	protected int maTextureHandle;
	/**
	 * @uml.property  name="maNormalHandle"
	 */
	protected int maNormalHandle;
	/**
	 * @uml.property  name="muCameraPositionHandle"
	 */
	protected int muCameraPositionHandle;
	/**
	 * @uml.property  name="muMMatrixHandle"
	 */
	protected int muMMatrixHandle;
	/**
	 * @uml.property  name="muVMatrixHandle"
	 */
	protected int muVMatrixHandle;
	/**
	 * @uml.property  name="mLight"
	 * @uml.associationEnd  
	 */
	protected ALight mLight;
	
	/**
	 * @uml.property  name="numTextures"
	 */
	protected int numTextures = 0;
	/**
	 * @uml.property  name="mModelViewMatrix" multiplicity="(0 -1)" dimension="1"
	 */
	protected float[] mModelViewMatrix;
	/**
	 * @uml.property  name="mViewMatrix" multiplicity="(0 -1)" dimension="1"
	 */
	protected float[] mViewMatrix;
	/**
	 * @uml.property  name="mTextureInfoList"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="de.bubbleshoo.graphics.TextureManager$TextureInfo"
	 */
	protected ArrayList<TextureInfo> mTextureInfoList;
	/**
	 * @uml.property  name="usesCubeMap"
	 */
	protected boolean usesCubeMap = false;
	
	public AMaterial() {
		mTextureInfoList = new ArrayList<TextureManager.TextureInfo>();
	}
	
	/**
	 * Copyconstructor
	 * @param aMat
	 */
	public AMaterial(AMaterial aMat) {
		mTextureInfoList = new ArrayList<TextureManager.TextureInfo>();
		
		mVertexShader = aMat.getmVertexShader();
		mFragmentShader = aMat.getmFragmentShader();
		
		
		
		mProgram = aMat.getmProgram();
		maPositionHandle = aMat.getMaPositionHandle();
		maNormalHandle = aMat.getMaNormalHandle();
		maTextureHandle = aMat.getMaTextureHandle();
		muCameraPositionHandle = aMat.getMuCameraPositionHandle();
		muMVPMatrixHandle = aMat.getMuMVPMatrixHandle();
		muMMatrixHandle = aMat.getMuMMatrixHandle();
		muVMatrixHandle = aMat.getMuVMatrixHandle();
	}
	
	public AMaterial(String vertexShader, String fragmentShader) {
		this();
		mVertexShader = vertexShader;
		mFragmentShader = fragmentShader;
		setShaders(vertexShader, fragmentShader);
	}
	
	public void setShaders(String vertexShader, String fragmentShader)
	{
		mProgram = createProgram(mVertexShader, fragmentShader);
		if(mProgram == 0) return;
		
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		if(maPositionHandle == -1) {
			throw new RuntimeException("Could not get attrib location for aPosition");
		}
		
		maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
		if(maNormalHandle == -1) {
			//Log.d(Wallpaper.TAG, "Could not get attrib location for aNormal " + getClass().toString());
		}

		maTextureHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");
		if(maTextureHandle == -1) {
			throw new RuntimeException("Could not get attrib location for aTextureCoord");
		}
		
		muCameraPositionHandle = GLES20.glGetUniformLocation(mProgram, "uCameraPosition");
		if(muCameraPositionHandle == -1) {
			//throw new RuntimeException("Could not get attrib location for uCameraPosition");
		}		
		
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		if(muMVPMatrixHandle == -1) {
			throw new RuntimeException("Could not get attrib location for uMVPMatrix");
		}
		
		muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
		if(muMMatrixHandle == -1) {
			//Log.d(Wallpaper.TAG, "Could not get attrib location for uMMatrix");
		}
		
		muVMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uVMatrix");
		if(muVMatrixHandle == -1) {
			//Log.d(Wallpaper.TAG, "Could not get attrib location for uVMatrix");
		}
	}
	
	protected int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0) {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    protected int createProgram(String vertexSource, String fragmentSource) {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }

        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (pixelShader == 0) {
            return 0;
        }

        int program = GLES20.glCreateProgram();
        if (program != 0) {
            GLES20.glAttachShader(program, vertexShader);
            GLES20.glAttachShader(program, pixelShader);
            GLES20.glLinkProgram(program);

            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    public void useProgram() {
    	GLES20.glUseProgram(mProgram);
    }
    
    public void bindTextures() {
    	int num = mTextureInfoList.size();
    	
        for(int i=0; i<num; i++)
        {
        	TextureInfo ti = mTextureInfoList.get(i);
        	int type = usesCubeMap ? GLES20.GL_TEXTURE_CUBE_MAP : GLES20.GL_TEXTURE_2D;
        	GLES20.glEnable(type);
            GLES20.glActiveTexture(ti.getTextureSlot());
            GLES20.glBindTexture(type, ti.getTextureId());
            GLES20.glUniform1i(ti.getUniformHandle(), ti.getTextureSlot() - GLES20.GL_TEXTURE0);
        }
    }
    
    public ArrayList<TextureInfo> getTextureInfoList() {
    	return mTextureInfoList;
    }
    
    public void addTexture(TextureInfo textureInfo) {
    	int count = mTextureInfoList.size();
        int textureHandle = GLES20.glGetUniformLocation(mProgram, "uTexture" + count);
		if(textureHandle == -1) {
			throw new RuntimeException("Could not get attrib location for uTexture" + count);
		}
        textureInfo.setUniformHandle(textureHandle);
        mTextureInfoList.add(textureInfo);
        numTextures++;
    }
    
    public void setVertices(FloatBuffer vertices) {
    	vertices.position(0);
    	GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertices);
    	GLES20.glEnableVertexAttribArray(maPositionHandle);
    }
    
    public void setTextureCoords(FloatBuffer textureCoords) {
    	textureCoords.position(0);
    	GLES20.glVertexAttribPointer(maTextureHandle, 2, GLES20.GL_FLOAT, false, 0, textureCoords);
        GLES20.glEnableVertexAttribArray(maTextureHandle);
    }
    
    public void setNormals(FloatBuffer normals) {
        if(maNormalHandle > -1)
        {
	        normals.position(0);
	        GLES20.glVertexAttribPointer(maNormalHandle, 3, GLES20.GL_FLOAT, false,
	        		0, normals);
	        GLES20.glEnableVertexAttribArray(maNormalHandle);
        }
    }
    
    public void setMVPMatrix(float[] mvpMatrix) {
    	GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mvpMatrix, 0);
    }
    
    public void setModelMatrix(float[] modelMatrix) {
    	mModelViewMatrix = modelMatrix;
        if(muMMatrixHandle > -1)
        	GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, modelMatrix, 0);
    }
    
    public void setViewMatrix(float[] viewMatrix) {
    	mViewMatrix = viewMatrix;
    	if(muVMatrixHandle > -1)
    		GLES20.glUniformMatrix4fv(muVMatrixHandle, 1, false, viewMatrix, 0);
    }
    
    public void setLight(ALight light)
    {
    	mLight = light;
    }
    
    public void setCamera(Camera3D camera) {
    	if(muCameraPositionHandle > -1)
    		GLES20.glUniform3fv(muCameraPositionHandle, 1, new float[]{ camera.getX(), camera.getY(), camera.getZ() }, 0);
    }
    
    public String toString() {
    	StringBuffer out = new StringBuffer();
    	out.append("____ VERTEX SHADER ____\n");
    	out.append(mVertexShader);
    	out.append("____ FRAGMENT SHADER ____\n");
    	out.append(mFragmentShader);
    	return out.toString();
    }
    
	public float[] getModelViewMatrix() {
		return mModelViewMatrix;
	}
	
	public void copyTexturesTo(AMaterial shader) {
		int num = mTextureInfoList.size();
		
		for(int i=0; i<num; ++i)
			shader.addTexture(mTextureInfoList.get(i));
	}
	
	/**
	 * @return the mVertexShader
	 */
	public String getmVertexShader() {
		return mVertexShader;
	}

	/**
	 * @return the mFragmentShader
	 */
	public String getmFragmentShader() {
		return mFragmentShader;
	}

	/**
	 * @return the mProgram
	 */
	public int getmProgram() {
		return mProgram;
	}

	/**
	 * @return the muMVPMatrixHandle
	 */
	public int getMuMVPMatrixHandle() {
		return muMVPMatrixHandle;
	}

	/**
	 * @return the maPositionHandle
	 */
	public int getMaPositionHandle() {
		return maPositionHandle;
	}

	/**
	 * @return the maTextureHandle
	 */
	public int getMaTextureHandle() {
		return maTextureHandle;
	}

	/**
	 * @return the maNormalHandle
	 */
	public int getMaNormalHandle() {
		return maNormalHandle;
	}

	/**
	 * @return the muCameraPositionHandle
	 */
	public int getMuCameraPositionHandle() {
		return muCameraPositionHandle;
	}

	/**
	 * @return the muMMatrixHandle
	 */
	public int getMuMMatrixHandle() {
		return muMMatrixHandle;
	}

	/**
	 * @return the muVMatrixHandle
	 */
	public int getMuVMatrixHandle() {
		return muVMatrixHandle;
	}
}
