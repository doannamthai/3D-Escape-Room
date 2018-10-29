package game.Engine;


/**
 * Store VBOs in VAO
 */

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;
import game.Items.Item;
import game.Items.Mesh;

public class Loader {
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private ArrayList<Integer> textures = new ArrayList<Integer>();
	
	public Loader() {}
	
	// Position, Texture Coordinates, Normals and Indices
	public Mesh bindToVAO(float[] position, float[] texture, float[] normals, int[] indices) {
		int vaoID = createVAO();		//System.out.println(Arrays.toString(position));

		storeDataInList(0, 3, position);
		storeDataInList(1, 2, texture);
		storeDataInList(2, 3, normals);
		bindIndicesBuffer(indices);

		unbindVAO();
		return new Mesh(vaoID, indices.length, position.length/3);
	}

	
	// Position, Texture Coordinates and Indices
	public Mesh bindToVAO(float[] position, float[] texture, int[] indices) {
		int vaoID = createVAO();
		storeDataInList(0, 3, position);
		storeDataInList(1, 2, texture);
		bindIndicesBuffer(indices);
		unbindVAO();
		return new Mesh(vaoID, indices.length, position.length/3);
	}
	
	// Position and Indices
	public Mesh bindToVAO(float[] position, int[] indices) {
		int vaoID = createVAO();
		storeDataInList(0, 3, position);
		bindIndicesBuffer(indices);
		unbindVAO();
		return new Mesh(vaoID, indices.length, position.length/3);
	}
	
	// Position
	public Mesh bindToVAO(float[] position) {
		int vaoID = createVAO();
		storeDataInList(0, 3, position);
		unbindVAO();
		return new Mesh(vaoID, position.length/3);
	}
	
	private int createVAO() {
		// Get the ID of VAO
		int vaoID = GL30.glGenVertexArrays(); 
		// Add to the Array
		vaos.add(vaoID);
		// Bind the ID to the Array
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void storeDataInList(int attributeNum, int coordSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNum, coordSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);      
	}
	
	private void unbindVAO() {
		// Unbind vaoID from Array
		GL30.glBindVertexArray(0);
	}
	
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
		buffer.put(data);
		buffer.flip();    
		return buffer;
		
	}
	
	public void cleanup() {
		// Clean all buffers
		for (int vao:vaos) {
			// For every VAO in  Array
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo:vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		for (int texture:textures) {
			GL11.glDeleteTextures(texture);
		}
	}
}
