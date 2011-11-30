/**
 * 
 */
package de.bubbleshoo.graphics;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import de.bubbleshoo.camera.BsCamera;
import de.bubbleshoo.camera.Camera3D;
import de.bubbleshoo.data.BaseObject3D;
import de.bubbleshoo.data.Bs3DObject;
import de.bubbleshoo.data.BsMesh;
import de.bubbleshoo.data.ObjParser;
import de.bubbleshoo.logic.LogicThread;
import de.bubbleshoo.main.BsMainActivity;
import de.bubbleshoo.main.R;
import de.bubbleshoo.map.Feld;
import de.bubbleshoo.map.Map;
import de.bubbleshoo.map.MapLoader;
import de.bubbleshoo.mapElemente.Baum;
import de.bubbleshoo.mapElemente.Busch;
import de.bubbleshoo.mapElemente.Felsen;
import de.bubbleshoo.mapElemente.Gras;
import de.bubbleshoo.mapElemente.MapElement;
import de.bubbleshoo.mapElemente.Mauer;
import de.bubbleshoo.mapElemente.Oelteppich;
import de.bubbleshoo.mapElemente.Rand;
import de.bubbleshoo.mapElemente.Sand;
import de.bubbleshoo.mapElemente.Stacheln;
import de.bubbleshoo.mapElemente.Start;
import de.bubbleshoo.mapElemente.Wasser;
import de.bubbleshoo.mapElemente.Weg;
import de.bubbleshoo.mapElemente.Ziel;
import de.bubbleshoo.sensors.BsDataholder;
import de.bubbleshoo.units.NormaleKugel;
import de.bubbleshoo.units.Unit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

/**
 * @author oliverl
 *
 */
public class BsRenderer implements GLSurfaceView.Renderer{
	
	/******************************
     * PROPERTIES
     ******************************/
	/**
	 * Needed for projectionmatrix
	 */
	private int muMVPMatrixHandle;
	protected TextureManager mTextureManager;
	
    // rotation 
    public float mAngleX;
    public float mAngleY;

    private static final int FLOAT_SIZE_BYTES = 4;
    private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 8 * FLOAT_SIZE_BYTES;
    private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
    private static final int TRIANGLE_VERTICES_DATA_NOR_OFFSET = 3;
    private static final int TRIANGLE_VERTICES_DATA_TEX_OFFSET = 6;

    // shader constants
    private final int GOURAUD_SHADER = 0;
    private final int PHONG_SHADER = 1;
    private final int NORMALMAP_SHADER = 2;
    //private static final int GOURAUD_SHADER = 0;

    // array of shaders
    BsShader _shaders[] = new BsShader[4];
    private int _currentShader;

    /** Shader code **/
    private int[] vShaders;
    private int[] fShaders;

    // object constants
    private final int OCTAHEDRON = 0;
    private final int TETRAHEDRON = 1;
    private final int CUBE = 2;

    // current object
    private int _currentObject;

    // Modelview/Projection matrices
    private float[] mMVPMatrix 		= new float[16];
    private float[] mProjMatrix 	= new float[16];
    private float[] mScaleMatrix 	= new float[16];   // scaling
    private float[] mRotXMatrix 	= new float[16];    // rotation x
    private float[] mRotYMatrix 	= new float[16];    // rotation x
    private float[] mMMatrix 		= new float[16];               // rotation
    private float[] mVMatrix 		= new float[16];               // modelview
    private float[] normalMatrix 	= new float[16];   // modelview normal

    // textures enabled?
    private boolean enableTexture = true;
    private int[] _texIDs;
    
    // light parameters
    private float[] lightPos;
    private float[] lightColor;
    private float[] lightAmbient;
    private float[] lightDiffuse;
    // angle rotation for light
    float angle = 0.0f;    
    
    // material properties
    private float[] matAmbient;
    private float[] matDiffuse;
    private float[] matSpecular;
    private float 	
    matShininess;

    // eye pos
    private float[] eyePos = {-5.0f, 0.0f, 0.0f};

    // scaling
    float scaleX = 1.0f;
    float scaleY = 1.0f;
    float scaleZ = 1.0f;
    
    /**
     * Rotationswinkel
     */
    private float fAngle_X;
    private float fAngle_Y;
    
    private float MAXANGLEVIEW	= 5.0f;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;

    /**
	 * Elements to draw
	 */
    protected Stack<BaseObject3D>	m_lstElements;
    
	protected Camera3D mCamera;
    private Context mContext;
    private static String TAG = "Renderer";
    protected Map m_map;

    //Logic 
    private LogicThread m_logic;
    
    /***************************
     * CONSTRUCTOR(S)
     **************************/
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
            
            /**
             * Camerasetings
             */
            mCamera = new Camera3D();
    		mCamera.setZ(-0.5f);

            // set current object and shader
            _currentObject = this.CUBE;
            _currentShader = this.NORMALMAP_SHADER;
    }


    /*****************************
     * GL FUNCTIONS
     ****************************/
    /*
     * Draw function - called for every frame
     */
    public void onDrawFrame(GL10 glUnused) {
            // Ignore the passed-in GL10 interface, and use the GLES20
            // class's static methods instead.
            GLES20.glClearColor(.0f, .0f, .0f, 1.0f);
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            
            GLES20.glUseProgram(0);
            
            fAngle_X = BsDataholder.getHandykipplageX() * TOUCH_SCALE_FACTOR;
    		fAngle_Y = BsDataholder.getHandykipplageY() * TOUCH_SCALE_FACTOR;
    		fAngle_X = BsDataholder.getHandykipplageX();
    		fAngle_Y = BsDataholder.getHandykipplageY();
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
    		
    		mCamera.setX(-fAngle_X);
    		mCamera.setY(-fAngle_Y);
    		mVMatrix = mCamera.getViewMatrix();
    		
            // MODELVIEW MATRIX
            long time = SystemClock.uptimeMillis() % 4000L;

            angle = ((360.0f - (BsDataholder.getKompassrichtung() - 180.0f)) / (360.0f)) * (2.0f * (float)Math.PI);
            
            // rotate light about z-axis
            float newPosX = (float)(30.0f * Math.cos(angle));
            float newPosY = (float)(30.0f * Math.sin(angle));
            lightPos[0] = newPosX;
            lightPos[1] = newPosY;
                        
            // Draw Meshs
            for (MapElement bsEmt : m_logic.getM_lstMapEmt()) {
            	
            	
//            	//NormaleKugelraussuchen
//            	if(bsEmt.getIsUnit() instanceof NormaleKugel)
//            	{
//            		
//            	}
//            	else
//            	{
            	bsEmt.getM_3dobject().render(mCamera, mProjMatrix, mVMatrix);
//            	bsEmt.drawObject(_shaders[this._currentShader].get_program(), mMVPMatrix, mVMatrix, mProjMatrix,
//            			lightPos, lightColor, matAmbient, matDiffuse, matSpecular, matShininess);
    		
//            	}
            } 
            
            for (Unit bsUnit : m_logic.getM_lstUnit()) {
            	
            	bsUnit.getM_3dobject().render(mCamera, mProjMatrix, mVMatrix);
      	
            }
            
            GLES20.glUseProgram(0);
            /** END DRAWING OBJECT ***/
    }

    /*
     * Called when viewport is changed
     * @see android.opengl.GLSurfaceView$Renderer#onSurfaceChanged(javax.microedition.khronos.opengles.GL10, int, int)
     */
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
            // Ignore the passed-in GL10 interface, and use the GLES20
            // class's static methods instead.
            GLES20.glViewport(0, 0, width, height);
            float ratio = (float) width / height;
            //Matrix.frustumM(mProjMatrix, 0, -5, 5, -1, 1, 0.5f, 6.0f);
            Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 0.5f, 80.0f);
            // reference the uMVPMatrix shader matrix variable
            muMVPMatrixHandle = GLES20.glGetUniformLocation(_shaders[this._currentShader].get_program(), "uMVPMatrix");
            // define a camera view matrix
            // Set Viewpoint 5 back and 1 up in the scene
            
            mCamera.setZ(-10.0f);
            mCamera.setLookAt(0.0f, 0.0f, 0.0f);
    		mVMatrix = mCamera.getViewMatrix();
    }

    /**
     * Initialization function
     */
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
            // Generate all the shader programs
            // initialize shaders
            try {
                    _shaders[GOURAUD_SHADER] = new BsShader(vShaders[GOURAUD_SHADER], fShaders[GOURAUD_SHADER], mContext, false, 0); // gouraud
                    _shaders[PHONG_SHADER] = new BsShader(vShaders[PHONG_SHADER], fShaders[PHONG_SHADER], mContext, false, 0); // phong
                    _shaders[NORMALMAP_SHADER] = new BsShader(vShaders[NORMALMAP_SHADER], fShaders[NORMALMAP_SHADER], mContext, false, 0); // normal map
             } catch (Exception e) {
                    Log.d("SHADER 0 SETUP", e.getLocalizedMessage());
            }

            // activate depthbuffer to hide faces wich are behind others
            GLES20.glEnable ( GLES20.GL_DEPTH_TEST );
            GLES20.glClearDepthf(1.0f);
            GLES20.glDepthFunc( GLES20.GL_LEQUAL );
            GLES20.glDepthMask( true );

            // cull backface
            GLES20.glEnable( GLES20.GL_CULL_FACE );
            GLES20.glCullFace(GLES20.GL_BACK);
            
            // Visible Faces (Clock|Countercloxkwise)
            GLES20.glFrontFace(GLES20.GL_CCW);

            // light variables
            float[] lightP = {1.0f, 1.0f, -10.0f, 1};
            this.lightPos = lightP;

            float[] lightC = {0.8f, 0.5f, 0.5f};
            this.lightColor = lightC;

            // material properties
            float[] mA = {1.0f, 0.5f, 0.5f, 1.0f};
            matAmbient = mA;

            float[] mD = {0.5f, 0.5f, 0.5f, 1.0f};
            matDiffuse = mD;

            float[] mS =  {1.0f, 1.0f, 1.0f, 1.0f};
            matSpecular = mS;

            matShininess = 5.0f;

            if(mTextureManager == null) 
            	mTextureManager = new TextureManager();
            else 
            	mTextureManager.reset();
            
            // Draw Meshs
            for (BaseObject3D bsEmt : this.m_lstElements) {
//            	setupTextures(bsEmt);
    		}
            
            //Logic starten
            this.m_logic = new LogicThread(this.mContext,mTextureManager);
           
            try {
				this.m_map = m_logic.getM_map();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            this.m_logic.start();
            
            
            
//			TextMsg.showTxtMsg(mContext, "Initialise Maps...");
            
//			int nY = 0;
//			int nX = 0;
		
//            
//			for (ArrayList<Feld> row_Y: this.m_map.getFelderY()) {
//				if (row_Y != null) {
//					nY++;
//					for (Feld col_X : row_Y) {
//						if (col_X != null) {
//							nX++;
//							Log.d("Mapinit", "X: " + col_X.getFeldposX() + "; Y: " + col_X.getFeldposY());
//							if ((col_X.getFeldposX() > 0) && (col_X.getFeldposY() > 0)) {
//								
//								int nTextureID;
//								if (col_X.getMapElement() instanceof Baum){
//									nTextureID = R.drawable.grass_tile;
//								} else if (col_X.getMapElement() instanceof Busch) {
//									nTextureID = R.drawable.bush_tile;
//									ObjParser parser = new ObjParser(mContext.getResources(), mTextureManager, R.raw.plane);
//						            parser.parse();
//									BaseObject3D emt = parser.getParsedObject().getChildByName("Plane");
//									
//						    		Bitmap texture = BitmapFactory.decodeResource(mContext.getResources(), nTextureID);
//						    		emt.addTexture(mTextureManager.addTexture(texture, nTextureID));
//						    		emt.setRotation(0, 0, 0);
//						    		emt.setScale(1.0f);
//						    		emt.setIsMapElement(col_X.getMapElement());
//						    		//emt.setPosition(-col_X.getFeldposX() + (row_Y.size() / 2.0f), -col_X.getFeldposY() + (this.m_map.getFelderY().size() / 2.0f), 0.0f);
//						    		emt.setPosition(2.0f * -col_X.getFeldposX() + (row_Y.size()), 2.0f * -col_X.getFeldposY() + (this.m_map.getFelderY().size()), -0.75f);
//						    		this.m_lstElements.add(emt);
//								} else if (col_X.getMapElement() instanceof Felsen){
//									nTextureID = R.raw.fieldstone;
//								} else if (col_X.getMapElement() instanceof Gras){
//									nTextureID = R.drawable.grass_tile;
//								} else if (col_X.getMapElement() instanceof Mauer){
//									nTextureID = R.drawable.brick_tile;
//									ObjParser parser = new ObjParser(mContext.getResources(), mTextureManager, R.raw.plane);
//						            parser.parse();
//									BaseObject3D emt = parser.getParsedObject().getChildByName("Plane");
//									
//						    		Bitmap texture = BitmapFactory.decodeResource(mContext.getResources(), nTextureID);
//						    		emt.addTexture(mTextureManager.addTexture(texture, nTextureID));
//						    		emt.setRotation(0, 0, 0);
//						    		emt.setScale(1.0f);
//						    		emt.setIsMapElement(col_X.getMapElement());
//						    		//emt.setPosition(-col_X.getFeldposX() + (row_Y.size() / 2.0f), -col_X.getFeldposY() + (this.m_map.getFelderY().size() / 2.0f), 0.0f);
//						    		emt.setPosition(2.0f * -col_X.getFeldposX() + (row_Y.size()), 2.0f * -col_X.getFeldposY() + (this.m_map.getFelderY().size()), -0.75f);
//						    		this.m_lstElements.add(emt);
//								} else if (col_X.getMapElement() instanceof Oelteppich){
//									nTextureID = R.raw.diffuse;
//								} else if (col_X.getMapElement() instanceof Rand){
//									nTextureID = R.drawable.grass_tile;
//								} else if (col_X.getMapElement() instanceof Sand){
//									nTextureID = R.drawable.sand_tile;
//								} else if (col_X.getMapElement() instanceof Stacheln){
//									nTextureID = R.drawable.grass_tile;
//								} else if (col_X.getMapElement() instanceof Start){
//									nTextureID = R.drawable.heightmap1;
//									ObjParser parser = new ObjParser(mContext.getResources(), mTextureManager, R.raw.sphere);
//						            parser.parse();
//									BaseObject3D emt = parser.getParsedObject().getChildByName("Sphere");
//									
//						    		Bitmap texture = BitmapFactory.decodeResource(mContext.getResources(), nTextureID);
//						    		emt.addTexture(mTextureManager.addTexture(texture, nTextureID));
//						    		emt.setRotation(0, 0, 0);
//						    		emt.setScale(1.0f);
//						    		emt.setIsUnit(new NormaleKugel());
//						    		//emt.setPosition(-col_X.getFeldposX() + (row_Y.size() / 2.0f), -col_X.getFeldposY() + (this.m_map.getFelderY().size() / 2.0f), 0.0f);
//						    		emt.setPosition(2.0f * -col_X.getFeldposX() + (row_Y.size()), 2.0f * -col_X.getFeldposY() + (this.m_map.getFelderY().size()), -0.75f);
//						    		this.m_lstElements.add(emt);
//								} else if (col_X.getMapElement() instanceof Wasser){
//									nTextureID = R.drawable.grass_tile;
//								} else if (col_X.getMapElement() instanceof Weg){
//									nTextureID = R.drawable.sand_tile;
//								} else if (col_X.getMapElement() instanceof Ziel){
//									nTextureID = R.drawable.grass_tile;
//								} else {
//									/**
//									 * Default
//									 */
//									nTextureID = R.drawable.grass_tile;
//								}
//								ObjParser parser = new ObjParser(mContext.getResources(), mTextureManager, R.raw.plane);
//					            parser.parse();
//								BaseObject3D emt = parser.getParsedObject().getChildByName("Plane");
//								
//					    		Bitmap texture = BitmapFactory.decodeResource(mContext.getResources(), nTextureID);
//					    		emt.addTexture(mTextureManager.addTexture(texture, nTextureID));
//					    		emt.setRotation(0, 0, 0);
//					    		emt.setScale(1.0f);
//					    		emt.setIsMapElement(col_X.getMapElement());
//					    		//emt.setPosition(-col_X.getFeldposX() + (row_Y.size() / 2.0f), -col_X.getFeldposY() + (this.m_map.getFelderY().size() / 2.0f), 0.0f);
//					    		emt.setPosition(2.0f * -col_X.getFeldposX() + (row_Y.size()), 2.0f * -col_X.getFeldposY() + (this.m_map.getFelderY().size()), 0.0f);
//					    		
//					    		this.m_lstElements.add(emt);
//							}
//						}
//					}
//				}
//			}
//    		            
            // set the view matrix
            mCamera.setZ(-10.0f);
            mCamera.setLookAt(0.0f, 0.0f, 0.0f);
    		mVMatrix = mCamera.getViewMatrix();
    }

    /**************************
     * OTHER METHODS
     *************************/

    /**
     * Changes the shader based on menu selection
     * @param represents the other shader 
     */
    public void setShader(int shader) {
            _currentShader = shader;
    }

    /**
     * Changes the object based on menu selection
     * @param represents the other object 
     */
    public void setObject(int object) {
            _currentObject = object;
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
                    _texIDs = new int[texIDs.length];
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
            }
    }

    /**
     * Scaling
     */
    public void changeScale(float scale) {
            if (scaleX * scale > 1.4f)
                    return;
            scaleX *= scale;scaleY *= scale;scaleZ *= scale;
            
            Log.d("SCALE: ", scaleX + "");
    }
    
    public void increaseScale() {
            scaleX += 0.01f;
            scaleY += 0.01f;
            scaleZ += 0.01f;
    }

    public void decreaseScale() {
            scaleX -= 0.01f;
            scaleY -= 0.01f;
            scaleZ -= 0.01f;

    }

    public void defaultScale() {
            scaleX = 1f;
            scaleY = 1f;
            scaleZ = 1f;
    }

    // debugging opengl
    private void checkGlError(String op) {
            int error;
            while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
                    Log.e(TAG, op + ": glError " + error);
                    throw new RuntimeException(op + ": glError " + error);
            }
    }
    
    /**
	 * @param m_lstElements the m_lstElements to set
	 */
	public void setLstElements(Stack<BaseObject3D> lstElements) {
		this.m_lstElements = lstElements;
	}

	/**
	 * @return the m_lstElements
	 */
	public Stack<BaseObject3D> getLstElements() {
		return m_lstElements;
	}
	
	/**
	 * Klasse zur Darstellung von Toastinfomsg
	 * @author Hans
	 *
	 */
	public static class TextMsg extends Thread {
	   public static void showTxtMsg (Context mContext, CharSequence csText) {
		   	int nDuration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(mContext, csText, nDuration);
			toast.show();
	   }
	}
} // END CLASS

