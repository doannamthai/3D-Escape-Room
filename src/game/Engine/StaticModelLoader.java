package game.Engine;
import static org.lwjgl.assimp.Assimp.*;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIString;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

import game.Items.Mesh;
import game.Texture.Cache;
import game.Texture.Material;
import game.Texture.Texture;
import game.Toolbox.Utils;
/**
 * mesh Loader
 * Wavefront files
 * Using ASSIMP Library
 */

public class StaticModelLoader {
	private Loader loader;
	
	public void addLoader(Loader loader) {
		this.loader = loader;
	}
	
	public Mesh[] load(String meshPath, String texturesDirectory){
	    return load(meshPath, texturesDirectory, aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_FixInfacingNormals);
	}
	
	
	public Mesh[] load(String resource, String textures, int flags){
		String resourcePath = resource;
		String texturesDir =  textures;
		AIScene aiScene = aiImportFile(resourcePath, flags);

        if (aiScene == null) {
            try {
				throw new Exception("Error loading mesh");
			} catch (Exception e) {
				e.printStackTrace();
			}
        }

        int numMaterials = aiScene.mNumMaterials();
        PointerBuffer aiMaterials = aiScene.mMaterials();
        List<Material> materials = new ArrayList<>();
        for (int i = 0; i < numMaterials; i++) {
            AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
            processMaterial(aiMaterial, materials, texturesDir);
        }

        int numMeshes = aiScene.mNumMeshes();
        PointerBuffer aiMeshes = aiScene.mMeshes();
        Mesh[] meshes = new Mesh[numMeshes];
        for (int i = 0; i < numMeshes; i++) {
            AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
            Mesh mesh = processMesh(loader, aiMesh, materials);
            meshes[i] = mesh;
        }

        return meshes;
	}
	
	/** 
	 * Process material
	 * @param aiMaterial
	 * @param materials
	 * @param texturesDir
	 * @throws Exception
	 */
	private static void processMaterial(AIMaterial aiMaterial, List<Material> materials, String texturesDir) {
	    AIColor4D colour = AIColor4D.create();
	    AIString path = AIString.calloc();
	    Assimp.aiGetMaterialTexture(aiMaterial, aiTextureType_DIFFUSE, 0, path, (IntBuffer) null, null, null, null, null, null);
	    String textPath = path.dataString();
	    Texture texture = null;

	    if (textPath != null && textPath.length() > 0) {
	        Cache textCache = Cache.getInstance();
	        try {
				texture = textCache.getTexture(textPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    else if (texturesDir.length() > 0){
	    	texture = new Texture(texturesDir);
	    }

	    Vector4f ambient = Material.DEFAULT_COLOUR;
	    int result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_AMBIENT, aiTextureType_NONE, 0, colour);
	    if (result == 0) {
	        ambient = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
	    }

	    Vector4f diffuse = Material.DEFAULT_COLOUR;
	    result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, colour);
	    if (result == 0) {
	        diffuse = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
	    }

	    Vector4f specular = Material.DEFAULT_COLOUR;
	    result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_SPECULAR, aiTextureType_NONE, 0, colour);
	    if (result == 0) {
	        specular = new Vector4f(colour.r(), colour.g(), colour.b(), colour.a());
	    }

	    Material material = new Material(ambient, diffuse, specular, 1.0f);
	    material.setTexture(texture);
	    materials.add(material);
	}
	
	/**
	 * Bind mesh's coordinates to VAO
	 * @param aiMesh
	 * @param materials
	 * @return mesh
	 */
	private static Mesh processMesh(Loader loader, AIMesh aiMesh, List<Material> materials) {
        List<Float> vertices = new ArrayList<>();
        List<Float> textures = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        Material material;


        processVertices(aiMesh, vertices);
        processNormals(aiMesh, normals);
        processTextCoords(aiMesh, textures);
        processIndices(aiMesh, indices);

        Mesh mesh = loader.bindToVAO(Utils.listFloatToArray(vertices),
                Utils.listFloatToArray(textures),
                Utils.listFloatToArray(normals),
                Utils.listIntToArray(indices)
        );
        
        int materialIdx = aiMesh.mMaterialIndex();
        if (materialIdx >= 0 && materialIdx < materials.size()) {
            material = materials.get(materialIdx);
        } else {
            material = new Material();
        }
        mesh.addMaterial(material);

        return mesh;
    }
	
	/**
	 * Add vertices coordinate to ASSIMP
	 * @param aiMesh
	 * @param vertices
	 */
	private static void processVertices(AIMesh aiMesh, List<Float> vertices) {
	    AIVector3D.Buffer aiVertices = aiMesh.mVertices();
	    while (aiVertices.remaining() > 0) {
	        AIVector3D aiVertex = aiVertices.get();
	        vertices.add(aiVertex.x());
	        vertices.add(aiVertex.y());
	        vertices.add(aiVertex.z());
	    }
	}
	/**
	 * Add normals to ASSIMP
	 * @param aiMesh
	 * @param normals
	 */
	private static void processNormals(AIMesh aiMesh, List<Float> normals) {
	    AIVector3D.Buffer aiNormals = aiMesh.mNormals();
	    while (aiNormals != null && aiNormals.remaining() > 0) {
	        AIVector3D aiNormal = aiNormals.get();
	        normals.add(aiNormal.x());
	        normals.add(aiNormal.y());
	        normals.add(aiNormal.z());
	    }
	}
	
	/**
	 * Add Texture coordinates to ASSIMP
	 * @param aiMesh
	 * @param textures
	 */
	
	private static void processTextCoords(AIMesh aiMesh, List<Float> textures) {
	    AIVector3D.Buffer textCoords = aiMesh.mTextureCoords(0);
	    int numTextCoords = textCoords != null ? textCoords.remaining() : 0;
	    for (int i = 0; i < numTextCoords; i++) {
	        AIVector3D textCoord = textCoords.get();
	        textures.add(textCoord.x());
	        textures.add(1 - textCoord.y());
	    }
	}
	
	/**
	 * Bind indices to ASSIMP
	 * @param aiMesh
	 * @param indices
	 */
	private static void processIndices(AIMesh aiMesh, List<Integer> indices) {
	    int numFaces = aiMesh.mNumFaces();
	    AIFace.Buffer aiFaces = aiMesh.mFaces();
	    for (int i = 0; i < numFaces; i++) {
	        AIFace aiFace = aiFaces.get(i);
	        IntBuffer buffer = aiFace.mIndices();
	        while (buffer.remaining() > 0) {
	            indices.add(buffer.get());
	        }
	    }
	}
}
