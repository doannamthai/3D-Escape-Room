
package game.Engine;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector3f;

import game.Items.Item;
import game.Items.Mesh;
import game.Items.ModelProcessor;

/**
 * OBJreader class. Loads in Wavefront .Model file in to the program.
 **/

public class OBJreader {
	private static String folder = "models";
	private static String fileType = ".obj";
	
	public static Mesh readFile(String fileName) {
		List<Vector3f> vertices = new ArrayList<>();
		List<Vector2f> textures = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Face> faces = new ArrayList<>();
        String line;
		FileReader data = null;
		BufferedReader reader = null;
			// Get the File Path
		String filePath = System.getProperty("user.dir") + "/" + folder +"/" + fileName + fileType;
        try {
        	File file = new File(filePath);
            data = new FileReader(file);
            reader = new BufferedReader(data);
            
            // Processing the data
            while ((line = reader.readLine()) != null) {
            	String[] splitedData = line.split("\\s+");     // Split all space
            	String type = splitedData[0];                  // Find the type by getting the first word   
            	
            	switch(type) {
            	case "v":
            		// Geometric vertex
            		Vector3f vertice = new Vector3f(
                            Float.parseFloat(splitedData[1]),  // Convert to float type
                            Float.parseFloat(splitedData[2]),
                            Float.parseFloat(splitedData[3]));
                    vertices.add(vertice);                     // Add to the list
                    break;
                    
            	case "vt":
            		// Texture coordinate
            		Vector2f textureCoord = new Vector2f(
            				Float.parseFloat(splitedData[1]),
            				Float.parseFloat(splitedData[2]));
            		textures.add(textureCoord);
            		break;
            	
            	case "vn":
            		// Vertex normal
                    Vector3f normal = new Vector3f(
                    		Float.parseFloat(splitedData[1]),
                    		Float.parseFloat(splitedData[2]),
                    		Float.parseFloat(splitedData[3]));
                    normals.add(normal);
                    break;
                    
            	case "f":
            		// Faces
                    Face face = new Face(splitedData[1], splitedData[2], splitedData[3]);
                    faces.add(face);
                    break;
                    
            	case "mtllib":
            		// Material
            		break;
                    
                default:
                    // Ignore other lines
                    break;
            	}
            }
        } catch (IOException e) {
            System.err.println("[OBJreader] Cannot read file " + fileName + " in " + folder + ".");
        }
        
        // Reorder The List
        return reorderLists(vertices, textures, normals, faces);
	}

	private static Mesh reorderLists(List<Vector3f> posList, List<Vector2f> textCoordList,
		    List<Vector3f> normList, List<Face> facesList) {

		    List<Integer> indices = new ArrayList<>();
		    // Create position array in the order it has been declared
		    float[] posArr = new float[posList.size() * 3];
		    int i = 0;
		    for (Vector3f pos : posList) {
		        posArr[i * 3] = pos.x;
		        posArr[i * 3 + 1] = pos.y;
		        posArr[i * 3 + 2] = pos.z;
		        i++;
		    }
		    float[] textCoordArr = new float[posList.size() * 2];
		    float[] normArr = new float[posList.size() * 3];

		    for (Face face : facesList) {
		        IdxGroup[] faceVertexIndices = face.getFaceVertexIndices();
		        for (IdxGroup indValue : faceVertexIndices) {
		            processFaceVertex(indValue, textCoordList, normList,
		                indices, textCoordArr, normArr);
		        }
		    }
		    int[] indicesArr = new int[indices.size()];
		    indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();
		    Loader loader = new Loader();

		    Mesh model = loader.bindToVAO(posArr, textCoordArr, normArr, indicesArr);
		    //ModelProcessor processor = new ModelProcessor();
		    //Vector3f center = processor.processModel(posArr);
		    //System.out.println("Center: (" + center.x + ", " + center.y + ", " + center.z + ")");
		    return model;
		}

	private static void processFaceVertex(IdxGroup indices, List<Vector2f> textCoordList,
		    List<Vector3f> normList, List<Integer> indicesList,
		    float[] texCoordArr, float[] normArr) {

		    // Set index for vertex coordinates
		    int posIndex = indices.idxPos;
		    indicesList.add(posIndex);

		    // Reorder texture coordinates
		    if (indices.idxTextCoord >= 0) {
		        Vector2f textCoord = textCoordList.get(indices.idxTextCoord);
		        texCoordArr[posIndex * 2] = textCoord.x;
		        texCoordArr[posIndex * 2 + 1] = 1 - textCoord.y;
		    }
		    if (indices.idxVecNormal >= 0) {
		        // Reorder vectornormals
		        Vector3f vecNorm = normList.get(indices.idxVecNormal);
		        normArr[posIndex * 3] = vecNorm.x;
		        normArr[posIndex * 3 + 1] = vecNorm.y;
		        normArr[posIndex * 3 + 2] = vecNorm.z;
		    }
		}
	
	protected static class IdxGroup {

        public static final int NO_VALUE = -1;

        public int idxPos;

        public int idxTextCoord;

        public int idxVecNormal;

        public IdxGroup() {
            idxPos = NO_VALUE;
            idxTextCoord = NO_VALUE;
            idxVecNormal = NO_VALUE;
            }
    }

	protected static class Face {

	    /**
	     * List of idxGroup groups for a face triangle (3 vertices per face).
	    */
	    private IdxGroup[] idxGroups = new IdxGroup[3];

	    public Face(String v1, String v2, String v3) {
	        idxGroups = new IdxGroup[3];
	        // Parse the lines
	        idxGroups[0] = parseLine(v1);
	        idxGroups[1] = parseLine(v2);
	        idxGroups[2] = parseLine(v3);
	    }

	    private IdxGroup parseLine(String line) {
	        IdxGroup idxGroup = new IdxGroup();

	        String[] lineTokens = line.split("/");
	        int length = lineTokens.length;
	        idxGroup.idxPos = Integer.parseInt(lineTokens[0]) - 1;
	        if (length > 1) {
	            // It can be empty if the obj does not define text coords
	            String textCoord = lineTokens[1];
	            idxGroup.idxTextCoord = textCoord.length() > 0 ? Integer.parseInt(textCoord) - 1 : IdxGroup.NO_VALUE;
	            if (length > 2) {
	                idxGroup.idxVecNormal = Integer.parseInt(lineTokens[2]) - 1;
	            }
	        }

	        return idxGroup;
	    }

	    public IdxGroup[] getFaceVertexIndices() {
	        return idxGroups;
	    }
	}
}
