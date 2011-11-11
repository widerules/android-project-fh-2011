/**
 * 
 */
package de.bubbleshoo.graphics;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import de.bubbleshoo.camera.BsCamera;
import de.bubbleshoo.data.Bs3DObject;
import de.bubbleshoo.main.R;
import de.bubbleshoo.sensors.BsDataholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

/**
 * @author oliverl
 *
 */
public class BsRenderer implements GLSurfaceView.Renderer{
	
	/**
	 *  shader constants
	 */
    private final int GOURAUD_SHADER = 0;
    private final int PHONG_SHADER = 1;
    private final int NORMALMAP_SHADER = 2;
    
 // array of shaders
    BsShader _shaders[] = new BsShader[4];
    private int _currentShader;
    
    /** Shader code **/
    private int[] vShaders;
    private int[] fShaders;
    
	private BsCamera m_camera = null;
	/**
	 * Needed for projectionmatrix
	 */
	private int muMVPMatrixHandle;
	
	/**
	 * Modelviewprojectionmatrix (proj * view)
	 */
	private float[] mMVPMatrix = new float[16];
	
	/**
	 * View Matrix
	 */
	private float[] mVMatrix = new float[16];
	
	/**
	 * Projectionmatrix
	 */
	private float[] mProjMatrix = new float[16];
	
	/**
	 * MotionMatrix
	 */
	private float[] mMMatrix = new float[16];
	
	/**
	 * Programm for Vertex- and Fragmentshader
	 */
	private int mProgram;
	
	/**
	 * Position for Elements manipulated by shader
	 */
    private int maPositionHandle;
    
    /**
     * Rotationswinkel
     */
    private float fAngle_X;
    private float fAngle_Y;
    
    private float MAXANGLEVIEW	= 15.0f;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    
    private Context mContext;
	/**
	 * Elements to draw
	 */
	private List<Bs3DObject>	m_lstElements;
	
	/**
	 *  Vertex shader
	 */
	private final String 	m_vertexShaderCode = 
		"uniform mat4 uMVPMatrix; \n" +
		"uniform mat4 normalMatrix; \n" +
		"uniform vec3 eyePos; \n" +
		"attribute vec4 aPosition; \n" +
		"attribute vec3 aNormal; \n" +
		"uniform float hasTexture; \n" +
		"varying float tex; \n" +
		"attribute vec2 textureCoord; \n" +
		"varying vec2 tCoord; \n" +
		"uniform vec4 lightPos; \n" +
		"uniform vec4 lightColor; \n" +
		"uniform vec4 matAmbient; \n" +
		"uniform vec4 matDiffuse; \n" +
		"uniform vec4 matSpecular; \n" +
		"uniform float matShininess; \n" +
		"varying vec4 color; \n" +
		"void main() { \n" +
		"vec3 eP = eyePos; \n" +
		"tex = hasTexture; \n" +
		"tCoord = textureCoord; \n" +
		"vec4 nm = normalMatrix * vec4(aNormal, 1.0); \n" +
		"vec3 EyespaceNormal = vec3(uMVPMatrix * vec4(aNormal, 1.0)); \n" +
		"vec4 posit = uMVPMatrix * aPosition; \n" +
		"vec3 lightDir = lightPos.xyz - posit.xyz; \n" +
		"vec3 eyeVec = -posit.xyz; \n" +
		"vec3 N = normalize(EyespaceNormal); \n" +
		"vec3 E = normalize(eyeVec); \n" +
		"vec3 L = normalize(lightDir); \n" +
		"vec3 reflectV = reflect(-L, N); \n" +
		"vec4 ambientTerm; \n" +
		"ambientTerm = matAmbient * lightColor; \n" +
		"vec4 diffuseTerm = matDiffuse * max(dot(N, L), 0.0); \n" +
		"vec4 specularTerm = matSpecular * pow(max(dot(reflectV, E), 0.0), matShininess); \n" +
		"color = ambientTerm + diffuseTerm + specularTerm; \n" +
		"gl_Position = uMVPMatrix * aPosition; \n" +
		"}";
		
//		// This matrix member variable provides a hook to manipulate
//        // the coordinates of the objects that use this vertex shader
//        "uniform mat4 uMVPMatrix;   \n" +
//
//        "attribute vec4 aPosition; \n" +
//        "void main(){              \n" +
//        //" normalVec = normalize (gl_NormalMatrix * gl_Normal);\n" +
//        " gl_Position = uMVPMatrix  * aPosition; \n" +
//        "}\n";
    
	
	/**
	 * Fragment Shader
	 */
    private final String 	m_fragmentShaderCode = 
    	"precision mediump float;" +
    	"uniform sampler2D texture1;" +
    	"varying float tex;" +
    	"varying vec2 tCoord;" +
    	"varying vec4 color;" +
    	"void main() {" + 
    	"	if (tex >= 1.0) {" +
    	"		gl_FragColor = texture2D(texture1, tCoord);" +
    	"	}" +
    	"	else" +
    	"		gl_FragColor = color;" + 
    	"}";
//        "precision mediump float;  \n" +
//        // texture variables
//        "uniform sampler2D texture1; \n" + 
//        "void main(){              \n" +
//        " gl_FragColor = vec4 (0.63671875, 0.76953125, 0.22265625, 1.0); \n" +
//        "}                         \n";
	
	public BsRenderer(Context context) {
		mContext = context;
		
		// setup all the shaders
        vShaders = new int[4];
        fShaders = new int[4];
        
        // basic - just gouraud shading
        vShaders[GOURAUD_SHADER] = R.raw.gouraud_vs;
        fShaders[GOURAUD_SHADER] = R.raw.gouraud_ps;

        // phong shading
        vShaders[PHONG_SHADER] = R.raw.phong_vs;
        fShaders[PHONG_SHADER] = R.raw.phong_ps;

        // normal mapping
        vShaders[NORMALMAP_SHADER] = R.raw.normalmap_vs;
        fShaders[NORMALMAP_SHADER] = R.raw.normalmap_ps;
        
        _currentShader = this.NORMALMAP_SHADER;
	}

	/**
	 * Is called for each redraw of the View
	 */
	public void onDrawFrame(GL10 gl) {
		// Redraw background Color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		fAngle_X = BsDataholder.getHandykipplageX() * TOUCH_SCALE_FACTOR;
		fAngle_Y = BsDataholder.getHandykipplageY() * TOUCH_SCALE_FACTOR;
		if (Math.abs(fAngle_X) > MAXANGLEVIEW) {
			if (fAngle_X < 0)
				fAngle_X = -MAXANGLEVIEW;
			else
				fAngle_X = MAXANGLEVIEW;
		}
		
		if (Math.abs(fAngle_Y) > MAXANGLEVIEW) {
			if (fAngle_Y < 0)
				fAngle_Y = -MAXANGLEVIEW;
			else
				fAngle_Y = MAXANGLEVIEW;
		}
		//mVMatrix = m_camera.setLookAtM(mVMatrix, 0, -fAngle_X, -fAngle_Y, -5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
		Matrix.setLookAtM(mVMatrix, 0, -fAngle_X, -fAngle_Y, -5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        
        // Draw Meshs
        for (Bs3DObject bsEmt : this.m_lstElements) {
        	bsEmt.drawObject(mProgram, muMVPMatrixHandle, mVMatrix, mProjMatrix);
		}
	}

	/**
	 *  Is called if the view-geometry changes, perhaps if the screen's orientation changes
	 *  between landsape an portrait
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		
		float fRatio = (float) width / height;
		// this projection matrix is applied to object coodinates
        // in the onDrawFrame() method
		// public static void frustumM (float[] m, int offset, float left, float right, float bottom, float top, float near, float far) 
        Matrix.frustumM(mProjMatrix, 0, -fRatio, fRatio, -1, 1, 2, 9);
        // reference the uMVPMatrix shader matrix variable
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        // define a camera view matrix
        // Set Viewpoint 5 back and 1 up in the scene
        
        m_camera.setLookAtM(mVMatrix, 0, 0.0f, 0.0f, -5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
	}

	/**
	 * Is called once to initalize the GLSurfaceView environmet
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, this.m_vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, this.m_fragmentShaderCode);
		// initialize shaders
        try {
                _shaders[GOURAUD_SHADER] = new BsShader(vShaders[GOURAUD_SHADER], fShaders[GOURAUD_SHADER], mContext, true, 0); // gouraud
                _shaders[PHONG_SHADER] = new BsShader(vShaders[PHONG_SHADER], fShaders[PHONG_SHADER], mContext, true, 0); // phong
                _shaders[NORMALMAP_SHADER] = new BsShader(vShaders[NORMALMAP_SHADER], fShaders[NORMALMAP_SHADER], mContext, true, 0); // normal map
        } catch (Exception e) {
                Log.d("SHADER 0 SETUP", e.getLocalizedMessage());
        }
     
        // the current shader
        mProgram = _shaders[this._currentShader].get_program();
        
        m_camera = new BsCamera(mVMatrix, 0, 0.0f, 0.0f, -5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

        //GLES20.glEnable   ( GLES20.GL_DEPTH_TEST );
        GLES20.glClearDepthf(1.0f);
        GLES20.glDepthFunc( GLES20.GL_LEQUAL );
        GLES20.glDepthMask( true );

        // cull backface
        GLES20.glEnable( GLES20.GL_CULL_FACE );
        GLES20.glCullFace(GLES20.GL_BACK); 
        
		// Set the background frame color
		GLES20.glClearColor(0.5390625f, 0.7109375f, 0.95703125f, 1.0f);
		
		this.mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(this.mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(this.mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(this.mProgram);                  // creates OpenGL program executables
        
        // get handle to the vertex shader's aPosition member
        this.maPositionHandle = GLES20.glGetAttribLocation(this.mProgram, "aPosition");
        
//        // setup textures for all objects
//        for (Bs3DObject bsEmt : this.m_lstElements) {
//                setupTextures(bsEmt);
//        }
	}
	
	/**
	 * loads shader from its code and type
	 * @param type
	 * @param shaderCode
	 * @return
	 */
	private int loadShader(int type, String shaderCode){
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type); 
        
        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        
        return shader;
    }
	
	/**
     * Sets up texturing for the object
     */
    private void setupTextures(Bs3DObject ob) {
            // create new texture ids if object has them
            if (ob.hasTexture()) {
                    // number of textures
                    int[] texIDs = ob.get_texID();
                    int[] textures = new int[texIDs.length];
                    ob.set_texID(new int[texIDs.length]);
                    // texture file ids
                    int[] texFiles = ob.getTexFile();

                    Log.d("TEXFILES LENGTH: ", texFiles.length + "");
                    GLES20.glGenTextures(texIDs.length, textures, 0);
                    
                    for(int i = 0; i < texIDs.length; i++) {
                            texIDs[i] = textures[i];
                            
                            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texIDs[i]);

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

                            InputStream is = mContext.getResources()
                            .openRawResource(texFiles[i]);
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
                            
                            Log.d("ATTACHING TEXTURES: ", "Attached " + i);
                    }
                    ob.set_texID(texIDs);
            }
    }
	
	/**
	 * @param m_lstElements the m_lstElements to set
	 */
	public void setLstElements(List<Bs3DObject> lstElements) {
		this.m_lstElements = lstElements;
	}

	/**
	 * @return the m_lstElements
	 */
	public List<Bs3DObject> getLstElements() {
		return m_lstElements;
	}

	/**
	 * @param fAngle the fAngle to set
	 */
	public void setAngleX(float fAngle) {
		this.fAngle_X = fAngle;
	}

	/**
	 * @return the fAngle
	 */
	public float getAngleX() {
		return fAngle_X;
	}
	
	/**
	 * @param fAngle the fAngle to set
	 */
	public void setAngleY(float fAngle) {
		this.fAngle_Y = fAngle;
	}

	/**
	 * @return the fAngle
	 */
	public float getAngleY() {
		return fAngle_Y;
	}

}
