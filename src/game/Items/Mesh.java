package game.Items;

import org.joml.Vector3f;
import org.joml.Vector4f;

import game.Texture.Material;
import game.Texture.Texture;

public class Mesh {
	
	private int vaoID;
	private int vertexCount;
	private int pointCount;
	private Material material;

	public Mesh(int vaoID, int vertexCount, int pointCount){
		this.vaoID = vaoID;              // Save VAO ID
		this.vertexCount = vertexCount;  // Save number of vertex
		this.pointCount = pointCount;    // Save number of points
		this.material = new Material();
	}
	
	public Mesh(int vaoID, int pointCount) {
		this.vaoID = vaoID;              // Save VAO ID
		this.pointCount = pointCount;    // Save number of points
		this.material = new Material();
	}
	
	public int getVaoID() {
		return this.vaoID;
	}
	
	public int getVertexCount() {
		return this.vertexCount;
	}
	
	public int getPointCount() {
		return this.pointCount;
	}
	
	public Material getMaterial() {
		return this.material;
	}
	
	public void addMaterial(Material material) {
		this.material = material;
	}
		
	public void addMaterial(String nameFile) {
    	Texture texture = new Texture(nameFile);
    	Material material = new Material(texture);
    	this.material = material;
    }
   
    public void bindNormalMap() {
    	if (this.material != null) {
    		this.material.setNormalMap(this.material.getTexture());
    	}
    }
    
    public boolean hasTexture() {
		return this.material.getTexture() != null;
	}
}
