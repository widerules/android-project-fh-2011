/**
 * Defines a mesh for a 3D Object.
 * Mesh consists of triangular faces with normals
 */

package de.bubbleshoo.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.StringTokenizer;

import android.content.Context;
import android.util.Log;

/**
 * @author oliverl
 *
 */
public class BsMesh {
        /*************************
         * PROPERTIES
         ************************/
        int meshID; // The id of the stored mesh file (raw resource)

        // Constants
        private static final int FLOAT_SIZE_BYTES = 4;
        private static final int SHORT_SIZE_BYTES = 2;
        // the number of elements for each vertex
        // [coordx, coordy, coordz, normalx, normaly, normalz....]
        private final int VERTEX_ARRAY_SIZE = 8;
        
        // if tex coords exist
        private final int VERTEX_TC_ARRAY_SIZE = 8;

        // Vertices
        private float _vertices[];

        // Normals
        private float _normals[];
        
        // Texture coordinates
        private float _texCoords[];
        
        // Indices
        private short _indices[];       
        
        // Buffers - index, vertex, normals and texcoords
        private FloatBuffer _vb;
        private FloatBuffer _nb;
        private ShortBuffer _ib;
        private FloatBuffer _tcb;

        // Normals
        private float[] _faceNormals;
        private int[]   _surroundingFaces; // # of surrounding faces for each vertex

        // Store the context
        Context activity; 


        /***************************
         * CONSTRUCTOR(S)
         **************************/
        public BsMesh() {

        }

        public BsMesh(int meshID) {
                this(meshID, null);
        }

        public BsMesh(int meshID, Context activity) {
                this.meshID = meshID;
                this.activity = activity;

                loadFile();
        }

        /**************************
         * OTHER METHODS
         *************************/

        /**
         * Tries to load a file - either a .OBJ or a .OFF
         * @return 1 if file was loaded properly, 0 if not 
         */
        private int loadFile() {
                //Log.d("Start-loadFile", "Starting loadFile");
                try {
                        // Read the file from the resource
                        //Log.d("loadFile", "Trying to buffer read");
                        InputStream inputStream = activity.getResources().openRawResource(meshID);

                        // setup Bufferedreader
                        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

                        // Try to parse the file
                        //Log.d("loadFile", "Trying to buffer read2");
                        String str = in.readLine();

                        // Make sure it's a .OFF file
                        if (str.equals("OFF"))
                                loadOFF(in);
                        else if (str.equals("OBJ"))
                                loadOBJ(in);
                        
                        // Generate your vertex, normal and index buffers
                        // vertex buffer
                        _vb = ByteBuffer.allocateDirect(_vertices.length
                                        * FLOAT_SIZE_BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
                        _vb.put(_vertices);
                        _vb.position(0);
                        
                        // normal buffer
                        /*_nb = ByteBuffer.allocateDirect(_normals.length
                                        * FLOAT_SIZE_BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
                        _nb.put(_normals);
                        _nb.position(0);*/

                        // index buffer
                        _ib = ByteBuffer.allocateDirect(_indices.length
                                        * SHORT_SIZE_BYTES).order(ByteOrder.nativeOrder()).asShortBuffer();
                        _ib.put(_indices);
                        _ib.position(0);

                        //Log.d("loadFile - size", _indices.length/3 + "," + _vertices.length);
                        // close the reader
                        in.close();
                        return 1;
                } catch (Exception e) {
                        //Log.d("Error-LoadFile", "FOUND ERROR: " + e.toString());
                        return 0;
                }
        }

        /**
         * Loads the .off file
         * 
         * OFF FORMAT:
         * ------------
         * Line 1
                OFF
           Line 2
                vertex_count face_count edge_count
           One line for each vertex:
                x y z 
                for vertex 0, 1, ..., vertex_count-1
           One line for each polygonal face:
                n v1 v2 ... vn, 
                the number of vertices, and the vertex indices for each face.
         * 
         * 
         * @return 1 if file was loaded properly, 0 if not 
         */
        private int loadOFF(BufferedReader in) throws Exception {
                try {
                        /* read # of vertices, faces, edges */
                        String str = in.readLine();
                        //Log.d("STR", str);

                        // tokenizer based on space
                        StringTokenizer tokenizer = new StringTokenizer(str);
                        int _numVertices = Integer.parseInt(tokenizer.nextToken());
                        int _numFaces = Integer.parseInt(tokenizer.nextToken());
                        //int _numEdges = Integer.parseInt(tokenizer.nextToken());

                        // read vertices - going to store vertex coordinates + normals
                        _vertices = new float[_numVertices * this.VERTEX_ARRAY_SIZE]; 
                        int i = 0;
                        for (i = 0; i < _numVertices; i++) {
                                str = in.readLine();

                                // tokenizer based on space
                                tokenizer = new StringTokenizer(str);
                                _vertices[i * this.VERTEX_ARRAY_SIZE]     = Float.parseFloat(tokenizer.nextToken());
                                _vertices[i * this.VERTEX_ARRAY_SIZE + 1] = Float.parseFloat(tokenizer.nextToken());
                                _vertices[i * this.VERTEX_ARRAY_SIZE + 2] = Float.parseFloat(tokenizer.nextToken());
                                //Log.d("Str vertices:", _vertices[i * this.VERTEX_ARRAY_SIZE + 0] + "," + _vertices[i * this.VERTEX_ARRAY_SIZE + 1] + "," + _vertices[i * this.VERTEX_ARRAY_SIZE + 2]);
                        }

                        //Log.d("ReadFile", "Read vertices");

                        // read faces and setup the index buffer
                        // array size
                        int arraySize = _numFaces * 3;
                        _indices = new short[arraySize];

                        // setup the normals
                        _normals = new float[_numVertices * this.VERTEX_ARRAY_SIZE]; 
                        _faceNormals = new float[arraySize]; // NEEDED?
                        _surroundingFaces = new int[_numVertices]; // # of surrounding faces for each vertex

                        // initialize to 0
                        for(int x = 0; x < _numVertices; x++) {
                                _vertices[x * this.VERTEX_ARRAY_SIZE + 3] = 0;
                                _vertices[x * this.VERTEX_ARRAY_SIZE + 4] = 0;
                                _vertices[x * this.VERTEX_ARRAY_SIZE + 5] = 0;
                                _surroundingFaces[x] = 0;
                        }

                        for (i = 0; i < _numFaces; i++) {
                                str = in.readLine();
                                // tokenizer based on space
                                tokenizer = new StringTokenizer(str);
                                // number of vertices for the face - make sure it's 3! [Might add support for 4 later]
                                short numV = Byte.parseByte(tokenizer.nextToken());
                                if (numV != 3)
                                        throw new IOException("TEST!!");

                                short firstV = Short.parseShort(tokenizer.nextToken());
                                short secondV = Short.parseShort(tokenizer.nextToken());
                                short thirdV = Short.parseShort(tokenizer.nextToken());

                                // Store in the index buffer
                                _indices[i * 3 + 0] = firstV;
                                _indices[i * 3 + 1] = secondV;
                                _indices[i * 3 + 2] = thirdV;

                                //Log.d("Reading Faces:", _indices[i * 3 + 0] + " " + _indices[i * 3 + 1] + " " + _indices[i * 3 + 2]);

                                // Calculate the face normal
                                setFaceNormal(i, firstV, secondV, thirdV);
                        }

                        // finally calculate the exact vertex normals
                        for(int x = 0; x < _numVertices; x++) {
                                _vertices[x * this.VERTEX_ARRAY_SIZE + 3] /= _surroundingFaces[x];
                                _vertices[x * this.VERTEX_ARRAY_SIZE + 4] /= _surroundingFaces[x];
                                _vertices[x * this.VERTEX_ARRAY_SIZE + 5] /= _surroundingFaces[x];

                                /*_normals[x * 3]     /= _surroundingFaces[x];
                                _normals[x * 3 + 1] /= _surroundingFaces[x];
                                _normals[x * 3 + 2] /= _surroundingFaces[x];*/
                                
                                //Log.d("SurroundingFaces: ", x + ": " + _surroundingFaces[x]);

                                ////Log.d("Exact normal: ", _normals[x * 3] + "," + _normals[x * 3 + 1] + "," + _normals[x * 3 + 2]);
                        }
                        
                        return 1;
                        
                } catch (Exception e) {
                        throw e;
                }
        }

        
        /**
         * Loads an OBJ file
         * OBJ FORMAT:
         * ----------
           list of vertices:
             v x y z
           list of tex coords:
             vt u v
           list of normals:
             vn x y z
           list of faces
             f pos1/tc1/n1 pos2/tc2/n2 pos3/tc3/n3
         * 
         * @param in The BufferedReader object
         * @return true = file properly parsed
         * @throws Exception
         */
        private int loadOBJ(BufferedReader in) throws Exception {
            try {
                //Log.d("In OBJ:", "First");
                /* read vertices first */
                String str = in.readLine();
                StringTokenizer t = new StringTokenizer(str);
                
                String type = t.nextToken();
                
                // keep reading vertices
                int numVertices = 0;
                int numVertNormals = 0;
                ArrayList<Float> vs = new ArrayList<Float>(100); // vertices
                ArrayList<Float> tc = new ArrayList<Float>(100); // texture coords
                ArrayList<Float> ns = new ArrayList<Float>(100); // normals
                
                while(type.equals("v")) {
                    //Log.d("In OBJ:", "V: " + str);
                    
                    vs.add(Float.parseFloat(t.nextToken()));        // x
                    vs.add(Float.parseFloat(t.nextToken()));        // y
                    vs.add(Float.parseFloat(t.nextToken()));        // z
            
                    // next vertex
                    str = in.readLine();
                    t = new StringTokenizer(str);
                    
                    type = t.nextToken();
                    numVertices++;
                }
                
                // read tex coords
                int numTexCoords = 0;
                if (type.equals("vt")) {
                    while(type.equals("vt")) {
                        //Log.d("In OBJ:", "VT: " + str);
                        tc.add(Float.parseFloat(t.nextToken()));        // u
                        tc.add(Float.parseFloat(t.nextToken()));        // v
                
                        // next texture coord
                        str = in.readLine();
                        t = new StringTokenizer(str);
                        
                        type = t.nextToken();
                        numTexCoords++;
                    }
                }
                
                // read vertex normals
                if (type.equals("vn")) {
                    while(type.equals("vn")) {
                        //Log.d("In OBJ:", "VN: " + str);
                        ns.add(Float.parseFloat(t.nextToken()));        // x
                        ns.add(Float.parseFloat(t.nextToken()));        // y
                        ns.add(Float.parseFloat(t.nextToken()));        // y
                        
                        // next texture coord
                        str = in.readLine();
                        t = new StringTokenizer(str);
                        
                        type = t.nextToken();
                        numVertNormals++;
                    }
                }
                
                
                // create the vertex buffer
                float[] _v = new float[numVertices * 3];
                // create the normal buffer
                float[] _n = new float[numVertNormals * 3];
                // texcoord
                _texCoords = new float[numTexCoords * 2];
                
                //Log.d("SETUP ARRAYS:", "DONE");
                
                // copy over data - INEFFICIENT [SHOULD BE A BETTER WAY]
                ////Log.d("SIZES: ", "V SIZE: " + numVertices + "; TC SIZE: " + numTexCoords);
                for(int i = 0; i < numVertices; i++) {
                        ////Log.d("VERTEX TRANSFER i: ", i + "");
                        _v[i * 3]        = vs.get(i * 3);
                        _v[i * 3 + 1] = vs.get(i * 3 + 1);
                        _v[i * 3 + 2] = vs.get(i * 3 + 2);
                        
                        ////Log.d("VERTEX TRANSFER: ", "1");
                        if (!ns.isEmpty()) {
                        	_n[i * 3 ]      = -ns.get(i * 3);
                            _n[i * 3 + 1] = -ns.get(i * 3 + 1);
                            _n[i * 3 + 2] = -ns.get(i * 3 + 2);
                        }
                        
                        ////Log.d("VERTEX TRANSFER: ", "2");
                        
                        // transfer tex coordinates
                        if (i < numTexCoords) {
                                _texCoords[i * 2]         = tc.get(i * 2);
                                _texCoords[i * 2 + 1] = tc.get(i * 2 + 1);
                        }
                        
                        ////Log.d("VERTEX TRANSFER: ", "3");
                }
                
                //Log.d("COMPLETED ARRAYS:", "Complete");
                
                // now read all the faces
                String fFace, sFace, tFace;
                ArrayList<Float> mainBuffer = new ArrayList<Float>(numVertices * 6);
                ArrayList<Short> indicesB = new ArrayList<Short>(numVertices * 3);
                StringTokenizer lt, ft; // the face tokenizer
                int numFaces = 0;
                short index = 0;
                if (type.equals("f")) {
                    while (type.equals("f")) {
                            
                        //Log.d("F LINE:", str);
                        // Each line: f v1/vt1/vn1 v2/vt2/vn2 
                        // Figure out all the vertices
                        for (int j = 0; j < 3; j++) {
                        	int vert = 0;
                            int texc = 0;
                            int vertN = 0;
                            fFace = t.nextToken();
                            //Log.d("FFACE TOKEN:", j + "," + fFace);
                            // Vertex/Normal (f v1//vn1 v2//vn2 v3//vn3 ...)
                            String[] faceTokens = fFace.split("//");
                            int st = faceTokens.length;
                            if (faceTokens.length == 2) {
                            	vert = Integer.parseInt(faceTokens[0]) - 1;
                            	vertN = Integer.parseInt(faceTokens[1]) - 1;
                            } else {
                            	// Vertex/texture (f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3 ...)
                                faceTokens = fFace.split("/");
                                if (faceTokens.length == 3) {
                                	vert = Integer.parseInt(faceTokens[0]) - 1;
                                    texc = Integer.parseInt(faceTokens[1]) - 1;
                                    vertN = Integer.parseInt(faceTokens[2]) - 1;
                                }
                            }                                                
                            
                            // Add to the index buffer
                            indicesB.add(index++);
                            
                            //Log.d("FFACE TOKEN:", "1");
                            
                            // Add all the vertex info
                            mainBuffer.add(_v[vert * 3]);    // x
                            mainBuffer.add(_v[vert * 3 + 1]);// y
                            mainBuffer.add(_v[vert * 3 + 2]);// z
                            //Log.d("FFACE TOKEN:", "2");
                            // add the normal info
                            mainBuffer.add(_n[vertN * 3]);    // x
                            mainBuffer.add(_n[vertN * 3 + 1]); // y
                            mainBuffer.add(_n[vertN * 3 + 2]); // z
                            //Log.d("FFACE TOKEN:", "3");
                            // add the tex coord info
                            if (_texCoords.length != 0) {
                            	mainBuffer.add(_texCoords[texc * 2]);     // u
                                mainBuffer.add(_texCoords[texc * 2 + 1]); // v
                            }
                            
                            Log.d("FFACE", String.valueOf(vert) + "//" +  String.valueOf(vertN));
                        }
                        
                        // next face
                        str = in.readLine();
                        if (str != null) {
                                t = new StringTokenizer(str);
                                numFaces++;
                                type = t.nextToken();
                        }
                        else
                                break;
                    }
                }
                
                mainBuffer.trimToSize();
                //Log.d("COMPLETED MAINBUFFER:", "" + mainBuffer.size());
                
                _vertices = new float[mainBuffer.size()];
                
                // copy over the mainbuffer to the vertex + normal array
                for(int i = 0; i < mainBuffer.size(); i++)
                        _vertices[i] = mainBuffer.get(i);
                
                //Log.d("COMPLETED TRANSFER:", "VERTICES: " + _vertices.length);
                
                // copy over indices buffer
                indicesB.trimToSize();
                _indices = new short[indicesB.size()];
                for(int i = 0; i < indicesB.size(); i++) {
                        _indices[i] = indicesB.get(i);
                }
                
                // print out all the vertices info
                /*Log.d("VERTEX OBJ LENGTH: ", _vertices.length + "");
                for(int i = 0; i < _vertices.length; i++) 
                        Log.d("Vertex INFO OBJ: ", i + "; " + _vertices[i]);*/
                
                return 1;
                    
            } catch(Exception e) {
                    throw e;
            }
        }
        /**
         * Sets the face normal of the i'th face
         * @param i the index of the face
         * @param firstV first vertex of the triangle
         * @param secondV second vertex of the triangle
         * @param thirdV third vertex of the triangle
         */
        private void setFaceNormal(int i, int firstV, int secondV, int thirdV) {
                // get coordinates of all the vertices
                float v1[] = {_vertices[firstV * VERTEX_ARRAY_SIZE], _vertices[firstV * VERTEX_ARRAY_SIZE + 1], _vertices[firstV * VERTEX_ARRAY_SIZE + 2]};
                float v2[] = {_vertices[secondV * VERTEX_ARRAY_SIZE], _vertices[secondV * VERTEX_ARRAY_SIZE + 1], _vertices[secondV * VERTEX_ARRAY_SIZE + 2]};
                float v3[] = {_vertices[thirdV * VERTEX_ARRAY_SIZE], _vertices[thirdV * VERTEX_ARRAY_SIZE + 1], _vertices[thirdV * VERTEX_ARRAY_SIZE + 2]};

                // calculate the cross product of v1-v2 and v2-v3
                float v1v2[] = {v1[0]-v2[0], v1[1]-v2[1], v1[2]-v2[2]};
                //float v3v2[] = {v2[0]-v3[0], v2[1]-v3[1], v2[2]-v3[2]};
                float v3v2[] = {v3[0]-v2[0], v3[1]-v2[1], v3[2]-v2[2]};

                //Log.d("V1V2: ", v1v2[0] + "," + v1v2[1] + "," + v1v2[2]);
                //Log.d("V3V2: ", v3v2[0] + "," + v3v2[1] + "," + v3v2[2]);
                float cp[] = crossProduct(v1v2, v3v2);

                // try normalizing here
                float sqrt = (float)Math.sqrt(cp[0] * cp[0] +
                                cp[1] * cp[1] +
                                cp[2] * cp[2]);

                cp[0] /= sqrt;
                cp[1] /= sqrt;
                cp[2] /= sqrt;
                
                if (cp[0] == -0.0f)
                        cp[0] = 0.0f;
                if (cp[1] == -0.0f)
                        cp[1] = 0.0f;
                if (cp[2] == -0.0f)
                        cp[2] = 0.0f;
                // end normalizing

                // set the normal
                _faceNormals[i * 3]     = cp[0];
                _faceNormals[i * 3 + 1] = cp[1];
                _faceNormals[i * 3 + 2] = cp[2];
                //Log.d("NORMAL:", cp[0] + "," + cp[1] + "," + cp[2]);

                // Setup for vertex normal construction;
                /*_normals[firstV * 3]     += _faceNormals[i * 3];
                _normals[firstV * 3 + 1] += _faceNormals[i * 3 + 1];
                _normals[firstV * 3 + 2] += _faceNormals[i * 3 + 2];
                
                _normals[secondV * 3]     += _faceNormals[i * 3];
                _normals[secondV * 3 + 1] += _faceNormals[i * 3 + 1];
                _normals[secondV * 3 + 2] += _faceNormals[i * 3 + 2];
                
                _normals[thirdV * 3]     += _faceNormals[i * 3];
                _normals[thirdV * 3 + 1] += _faceNormals[i * 3 + 1];
                _normals[thirdV * 3 + 2] += _faceNormals[i * 3 + 2];*/

                _vertices[firstV * this.VERTEX_ARRAY_SIZE + 3] += _faceNormals[i * 3];
                _vertices[firstV * this.VERTEX_ARRAY_SIZE + 4] += _faceNormals[i * 3 + 1];
                _vertices[firstV * this.VERTEX_ARRAY_SIZE + 5] += _faceNormals[i * 3 + 2];
                
                _vertices[secondV * this.VERTEX_ARRAY_SIZE + 3] += _faceNormals[i * 3];
                _vertices[secondV * this.VERTEX_ARRAY_SIZE + 4] += _faceNormals[i * 3 + 1];
                _vertices[secondV * this.VERTEX_ARRAY_SIZE + 5] += _faceNormals[i * 3 + 2];

                _vertices[thirdV * this.VERTEX_ARRAY_SIZE + 3] += _faceNormals[i * 3];
                _vertices[thirdV * this.VERTEX_ARRAY_SIZE + 4] += _faceNormals[i * 3 + 1];
                _vertices[thirdV * this.VERTEX_ARRAY_SIZE + 5] += _faceNormals[i * 3 + 2];

                // increment # of faces around the vertex
                _surroundingFaces[firstV]++;
                _surroundingFaces[secondV]++;
                _surroundingFaces[thirdV]++;
        }

        /**
         * Calculates the cross product of two 3d vectors
         */
        public float[] crossProduct(float[] v0, float[] v1)
        {
                float crossProduct[] = new float[3];

                crossProduct[0] = v0[1] * v1[2] - v0[2] * v1[1];
                crossProduct[1] = v0[2] * v1[0] - v0[0] * v1[2];
                crossProduct[2] = v0[0] * v1[1] - v0[1] * v1[0];

                return crossProduct;
        }



        /***************************
         * GET/SET
         *************************/

        public int getMeshID() {
                return meshID;
        }

        public void setMeshID(int meshID) {
                this.meshID = meshID;
        }

        public float[] get_vertices() {
                return _vertices;
        }

        public void set_vertices(float[] _vertices) {
                this._vertices = _vertices;
        }
        public short[] get_indices() {
                return _indices;
        }

        public FloatBuffer get_vb() {
                return this._vb;
        }
        
        public FloatBuffer get_nb() {
                return this._nb;
        }
        
        public ShortBuffer get_ib() {
                return this._ib;
        }

}
