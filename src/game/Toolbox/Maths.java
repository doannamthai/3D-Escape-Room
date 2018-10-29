package game.Toolbox;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * ToolBox for Matrices
 * @author ThaiDoan
 * @CPSC233
 */
public class Maths {

	public static Matrix4f getTransformedMatrix(Vector3f offset,
			Vector3f rotation, float scale){
			Matrix4f matrix = new Matrix4f();
			matrix.identity();
			// Translation
			matrix.translate(offset);
			// Rotation
			matrix.rotateX( (float) Math.toRadians(rotation.x));
			matrix.rotateY( (float) Math.toRadians(rotation.y));
			matrix.rotateZ( (float) Math.toRadians(rotation.z));
			// Scalar
			matrix.scale(scale);
			return matrix;
		}
		
}
