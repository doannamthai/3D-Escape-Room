package game.Items;

import java.util.List;

import org.joml.Vector3f;
import org.joml.Vector4f;

import game.Texture.Material;
import game.Texture.Texture;

/**
 * Model Class
 * @author ThaiDoan
 * @CPSC233
 */
public class Item {
    
	private String name;
	private Vector3f position;
	private Vector3f rotation;
	private float scale;
	private Mesh[] mesh;
	
	//CONSTRUCTORS
    public Item() {
    	this.position = new Vector3f(0.0f, 0.0f, 0.0f);
    	this.rotation = new Vector3f(0.0f, 0.0f, 0.0f);
    	this.scale = 1f;
    }
    
    public Item(Mesh[] mesh){
    	this.mesh = mesh;
    	this.position = new Vector3f(0.0f, 0.0f, 0.0f);
    	this.rotation = new Vector3f(0.0f, 0.0f, 0.0f);
    }
    
    public void setMesh(Mesh[] mesh) {
    	this.mesh = mesh;
    }
	
    public Mesh[] getMeshes() {
    	return this.mesh;
    }
    
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
    
    public void scale(float scale) {
    	this.scale = scale;
    }
    
    public void translate(Vector3f offset) {
    	this.position.x += offset.x;
    	this.position.y += offset.y;
    	this.position.z += offset.z;
    }
    
    public void setPosition(Vector3f pos) {
    	this.position.x = pos.x;
    	this.position.y = pos.y;
    	this.position.z = pos.z;
    }
    
    public void rotate(Vector3f rotation) {
    	this.rotation.x = rotation.x;
    	this.rotation.y = rotation.y;
    	this.rotation.z = rotation.z;
    }


	public Vector3f getPosition() {
		return new Vector3f(position);
	}
	

	public Vector3f getRotation() {
		return new Vector3f(rotation);
	}

	public float getScale() {
		return scale;
	}
   
}