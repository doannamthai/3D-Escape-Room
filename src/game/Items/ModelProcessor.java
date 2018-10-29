package game.Items;

import org.joml.Vector3f;

/**
 * Calculate the position of center of models
 * @author Thai Doan
 *
 */
public class ModelProcessor {
	
	public Vector3f processModel(float[] vertices) {

		// Point 1 and 2 will be the nearest
		float minZ = 0, maxZ = 0;
		float minY = 0, maxY = 0;
		float minX = 0, maxX = 0;

		if( isValidVertices(vertices) ) {
			for (int i = 0; i < vertices.length/3; i++) {
				float x = vertices[i*3];
				float y = vertices[i*3 + 1];
				float z = vertices[i*3 + 2];
				if (minX > x) {
					minX = x;
				}
				if (maxX < x) {
					maxX = x;
				}
				if (minY > y) {
					minY = y;
				}
				if (maxY < y) {
					maxY = y;
				}
				if (minZ > z) {
					minZ = z;
				}
				if (maxZ < z) {
					maxZ = z;
				}
			}
		}
		Vector3f center = new Vector3f((maxX+minX)/2, (maxY+minY)/2, (maxZ+minZ)/2);
		return center;
	}
	
	public boolean isValidVertices(float[] vertices) {
		return vertices.length % 3 == 0;
	}
	
}
