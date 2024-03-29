/**
 * 
 */
package de.bubbleshoo.graphics;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

/**
 * @author oliverl
 *
 */
public class BsShader {
    /**
	 * PROPERTIES
	 * @uml.property  name="_program"
	 */
    
    // program/vertex/fragment handles
    private int _program;

	/**
	 * PROPERTIES
	 * @uml.property  name="_vertexShader"
	 */
	private int _vertexShader;

	/**
	 * PROPERTIES
	 * @uml.property  name="_pixelShader"
	 */
	private int _pixelShader;
    
    // other handles - position/texture/mvpmatrix/normal
    /**
	 * @uml.property  name="muMVPMatrixHandle"
	 */
    public int muMVPMatrixHandle;

	/**
	 * @uml.property  name="normalMatrixHandle"
	 */
	public int normalMatrixHandle;
    /**
	 * @uml.property  name="maPositionHandle"
	 */
    public int maPositionHandle;
    /**
	 * @uml.property  name="maNormalHandle"
	 */
    public int maNormalHandle;
    /**
	 * @uml.property  name="hasTextureHandle"
	 */
    public int hasTextureHandle;   
    /**
	 * @uml.property  name="maTextureHandle"
	 */
    public int maTextureHandle;
    /**
	 * @uml.property  name="eyeHandle"
	 */
    public int eyeHandle;
    // light
    /**
	 * @uml.property  name="lightPosHandle"
	 */
    public int lightPosHandle;

	/**
	 * @uml.property  name="lightColorHandle"
	 */
	public int lightColorHandle;
    // material
    /**
	 * @uml.property  name="matAmbientHandle"
	 */
    public int matAmbientHandle;

	/**
	 * @uml.property  name="matDiffuseHandle"
	 */
	public int matDiffuseHandle;

	/**
	 * @uml.property  name="matSpecularHandle"
	 */
	public int matSpecularHandle;

	/**
	 * @uml.property  name="matShininessHandle"
	 */
	public int matShininessHandle;

    // The shaders
    /**
	 * @uml.property  name="_vertexS"
	 */
    private String _vertexS;

	/**
	 * @uml.property  name="_fragmentS"
	 */
	private String _fragmentS;
    
    // does it have textures?
    /**
	 * @uml.property  name="hasTextures"
	 */
    private boolean hasTextures;
    /**
	 * @uml.property  name="numTextures"
	 */
    private int numTextures;
    
/************************
 * CONSTRUCTOR(S)
 *************************/
    public BsShader() {
            
    }
    
    // Takes in Strings directly
    public BsShader(String vertexS, String fragmentS, boolean hasTextures, int numTextures) {
            setup(vertexS, fragmentS, hasTextures, numTextures);
    }
    
    // Takes in ids for files to be read
    public BsShader(int vID, int fID, Context context, boolean hasTextures, int numTextures) {
            StringBuffer vs = new StringBuffer();
            StringBuffer fs = new StringBuffer();
            
            // read the files
            try {
                    // Read the file from the resource
                    //Log.d("loadFile", "Trying to read vs");
                    // Read VS first
                    InputStream inputStream = context.getResources().openRawResource(vID);
                    // setup Bufferedreader
                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                
                String read = in.readLine();
                while (read != null) {
                    vs.append(read + "\n");
                    read = in.readLine();
                }
                
                vs.deleteCharAt(vs.length() - 1);
                //Log.d("StringBufferVS", vs.toString());
                
                // Now read FS
                //Log.d("loadFile", "Trying to read vs");
                    // Read VS first
                    inputStream = context.getResources().openRawResource(fID);
                    // setup Bufferedreader
                in = new BufferedReader(new InputStreamReader(inputStream));
                
                read = in.readLine();
                while (read != null) {
                    fs.append(read + "\n");
                    read = in.readLine();
                }
                
                fs.deleteCharAt(fs.length() - 1);
                //Log.d("StringBufferFS", fs.toString());
            } catch (Exception e) {
                    Log.d("ERROR-readingShader", "Could not read shader: " + e.getLocalizedMessage());
            }
            
            
            // Setup everything
            setup(vs.toString(), fs.toString(), hasTextures, numTextures);
    }
    
    
    /**************************
     * OTHER METHODS
     *************************/
    
    /** 
     * Sets up everything
     * @param vs the vertex shader
     * @param fs the fragment shader 
     */
    private void setup(String vs, String fs, boolean hasTextures, int numTextures) {
            this._vertexS = vs;
            this._fragmentS = fs;
            
            // create the program
            int create = createProgram();
            
            // texture variables
            this.hasTextures = hasTextures;
            this.numTextures = numTextures;
    }
    
    /**
     * Creates a shader program.
     * @param vertexSource
     * @param fragmentSource
     * @return returns 1 if creation successful, 0 if not
     */
    private int createProgram() {
            // Vertex shader
    _vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, _vertexS);
    if (_vertexShader == 0) {
        return 0;
    }

    // pixel shader
    _pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, _fragmentS);
    if (_pixelShader == 0) {
        return 0;
    }

    // Create the program
    _program = GLES20.glCreateProgram();
    if (_program != 0) {
        GLES20.glAttachShader(_program, _vertexShader);
        //checkGlError("glAttachShader VS " + this.toString());
        GLES20.glAttachShader(_program, _pixelShader);
        //checkGlError("glAttachShader PS");
        GLES20.glLinkProgram(_program);
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(_program, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] != GLES20.GL_TRUE) {
            Log.e("Shader", "Could not link _program: ");
            Log.e("Shader", GLES20.glGetProgramInfoLog(_program));
            GLES20.glDeleteProgram(_program);
            _program = 0;
            return 0;
        }
    }
    else
            Log.d("CreateProgram", "Could not create program");
    
    return 1;
}
    
    /**
     * Sets up the handles for the inputs
     */
    public void setupHandles() {
             // The handles
    // position
    maPositionHandle = GLES20.glGetAttribLocation(_program, "aPosition");
    Log.d("ATTRIB LOCATION POSITION: ", maPositionHandle + "");
    checkGlError("glGetAttribLocation aPosition");
    if (maPositionHandle == -1) {
        throw new RuntimeException("Could not get attrib location for aPosition");
    }
    
    // normal
    maNormalHandle = GLES20.glGetAttribLocation(_program, "aNormal");
    Log.d("ATTRIB LOCATION Normal: ", maNormalHandle + "");
    checkGlError("glGetAttribLocation normal");
    if (maNormalHandle == -1) {
        throw new RuntimeException("Could not get attrib location for normal");
    }
    
    
    
    // texture
    // handle for whether textures are available or not
    hasTextureHandle = GLES20.glGetUniformLocation(_program, "hasTexture");
    checkGlError("glGetAttribLocation hasTextureHandle");
    if (hasTextureHandle == -1) {
        throw new RuntimeException("Could not get attrib location for hasTextureHandle");
    }
    
    if (hasTextures) {
            maTextureHandle = GLES20.glGetAttribLocation(_program, "textureCoord");
            checkGlError("glGetAttribLocation aTextureCoord");
            if (maTextureHandle == -1) {
                throw new RuntimeException("Could not get attrib location for aTextureCoord");
            }
            GLES20.glEnableVertexAttribArray(this.maTextureHandle);
    }

    // modelview/projection matrix
    /*muMVPMatrixHandle = GLES20.glGetUniformLocation(_program, "uMVPMatrix");
    checkGlError("glGetUniformLocation uMVPMatrix");
    if (muMVPMatrixHandle == -1) {
        throw new RuntimeException("Could not get attrib location for uMVPMatrix");
    }*/
    
    // normal matrix handle
    normalMatrixHandle = GLES20.glGetUniformLocation(_program, "normalMatrix");
    checkGlError("glGetUniformLocation uMVPMatrix");
    if (normalMatrixHandle == -1) {
        throw new RuntimeException("Could not get attrib location for normalMatrix");
    }
    
    // light position/color handles
    lightColorHandle = GLES20.glGetUniformLocation(_program, "lightColor");
    checkGlError("glGetUniformLocation lightColor");
    if (lightColorHandle == -1) {
        throw new RuntimeException("Could not get attrib location for lightColor");
    }
    
    lightPosHandle = GLES20.glGetUniformLocation(_program, "lightPos");
    checkGlError("glGetUniformLocation lightPos");
    if (lightPosHandle == -1) {
        throw new RuntimeException("Could not get attrib location for lightPos");
    }
    
    // material handles
    matAmbientHandle = GLES20.glGetUniformLocation(_program, "matAmbient");
    checkGlError("glGetUniformLocation matAmbient");
    if (matAmbientHandle == -1) {
        throw new RuntimeException("Could not get attrib location for matAmbient");
    }
    
    matDiffuseHandle = GLES20.glGetUniformLocation(_program, "matDiffuse");
    checkGlError("glGetUniformLocation matDiffuse");
    if (matDiffuseHandle == -1) {
        throw new RuntimeException("Could not get attrib location for matDiffuse");
    }
    
    matSpecularHandle = GLES20.glGetUniformLocation(_program, "matSpecular");
    checkGlError("glGetUniformLocation matDiffuse");
    if (matSpecularHandle == -1) {
        throw new RuntimeException("Could not get attrib location for matSpecular");
    }
    
    matShininessHandle = GLES20.glGetUniformLocation(_program, "matShininess");
    checkGlError("glGetUniformLocation matShininess");
    if (matShininessHandle == -1) {
        throw new RuntimeException("Could not get attrib location for matShininess");
    }
    
    // eye pos
    eyeHandle = GLES20.glGetUniformLocation(_program, "eyePos");
    checkGlError("glGetAttribLocation eyepos");
    if (eyeHandle == -1) {
        throw new RuntimeException("Could not get attrib location for eyePos");
    }
    
    
    // Enable all of the vertex attribute arrays
            GLES20.glEnableVertexAttribArray(this.maPositionHandle);
            GLES20.glEnableVertexAttribArray(this.eyeHandle);
            GLES20.glEnableVertexAttribArray(this.maNormalHandle);
            GLES20.glEnableVertexAttribArray(this.hasTextureHandle);
            //GLES20.glEnableVertexAttribArray(this.muMVPMatrixHandle);
            GLES20.glEnableVertexAttribArray(this.normalMatrixHandle);
            
            GLES20.glEnableVertexAttribArray(this.lightColorHandle);
            GLES20.glEnableVertexAttribArray(this.lightPosHandle);
            
            GLES20.glEnableVertexAttribArray(this.matAmbientHandle);
            GLES20.glEnableVertexAttribArray(this.matDiffuseHandle);
            GLES20.glEnableVertexAttribArray(this.matSpecularHandle);
            GLES20.glEnableVertexAttribArray(this.matShininessHandle);
    }
    
    /**
     * Loads a shader (either vertex or pixel) given the source
     * @param shaderType VERTEX or PIXEL
     * @param source The string data representing the shader code
     * @return handle for shader
     */
    private int loadShader(int shaderType, String source) {
    int shader = GLES20.glCreateShader(shaderType);
    if (shader != 0) {
        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            Log.e("Shader", "Could not compile shader " + shaderType + ":");
            Log.e("Shader", GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }
    }
    return shader;
}
    
    /**
     * Error for OpenGL
     * @param op
     */
    private void checkGlError(String op) {
    int error;
    while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
        Log.e("Shader", op + ": glError " + error);
        throw new RuntimeException(op + ": glError " + error);
    }
}

    /**
	 * GET/SET
	 * @uml.property  name="_program"
	 */
    public int get_program() {
            return _program;
    }

    /**
	 * @param _program
	 * @uml.property  name="_program"
	 */
    public void set_program(int _program) {
            this._program = _program;
    }

    /**
	 * @return
	 * @uml.property  name="_vertexShader"
	 */
    public int get_vertexShader() {
            return _vertexShader;
    }

    /**
	 * @param shader
	 * @uml.property  name="_vertexShader"
	 */
    public void set_vertexShader(int shader) {
            _vertexShader = shader;
    }

    /**
	 * @return
	 * @uml.property  name="_pixelShader"
	 */
    public int get_pixelShader() {
            return _pixelShader;
    }

    /**
	 * @param shader
	 * @uml.property  name="_pixelShader"
	 */
    public void set_pixelShader(int shader) {
            _pixelShader = shader;
    }

    /**
	 * @return
	 * @uml.property  name="_vertexS"
	 */
    public String get_vertexS() {
            return _vertexS;
    }

    /**
	 * @param _vertexs
	 * @uml.property  name="_vertexS"
	 */
    public void set_vertexS(String _vertexs) {
            _vertexS = _vertexs;
    }

    /**
	 * @return
	 * @uml.property  name="_fragmentS"
	 */
    public String get_fragmentS() {
            return _fragmentS;
    }

    /**
	 * @param _fragments
	 * @uml.property  name="_fragmentS"
	 */
    public void set_fragmentS(String _fragments) {
            _fragmentS = _fragments;
    }
}
