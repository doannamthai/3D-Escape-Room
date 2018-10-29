package GameGraphic;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import game.Camera.Camera;

import static GameGraphic.CoordsAndData.*;
/**
 * Mouse's position in 3D space
 * @author Thai Doan
 *
 */

public class Mouse3D {
	private Matrix4f projectionMatrix;
	private Matrix4f cameraViewMatrix;
	private float mouseX;
	private float mouseY;
	private float mouseZ;
	private float mouseW;
	
	public Mouse3D(Matrix4f projectionMatrix) {
		Matrix4f projMatrix = new Matrix4f(projectionMatrix);
		this.projectionMatrix = projMatrix;
	}
	
	public Mouse3D(Camera camera, Matrix4f projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
		this.cameraViewMatrix = camera.getCameraMatrix();
	}
	
	
	/**
	 * Transform 2D mouse's position to 3D position
	 * @param x - screen coordinates
	 * @param y 
	 */
	
	public Vector3f getMouseRay(Camera camera, float x, float y) {
		this.cameraViewMatrix = camera.getCameraMatrix();
		Vector4f normalizedCoords = setNormalizedSpace(x, y);
		Vector4f eyeCoords = setEyeCoords(normalizedCoords);
		Vector4f cameraViewCoords = setCameraViewCoords(eyeCoords);
		Vector3f viewCoords = new Vector3f(cameraViewCoords.x, cameraViewCoords.y, cameraViewCoords.z);
		return viewCoords.normalize();
	}
	
	
	/**
	 * Transform to the game's coordinates system
	 * @param x - screen coordinates
	 * @param y
	 * @return Vector4f - coordinates of mouse in 3D space
	 */
	public Vector4f setNormalizedSpace(float x, float y) {
		float mouseX = 2*x/WIDTH - 1f;
		float mouseY = -2*y/HEIGHT - 1f;
		float mouseZ = -1f;
		float mouseW = 1f;
		Vector4f mouseCoords = new Vector4f(mouseX, mouseY, mouseZ, mouseW);
		return mouseCoords;
	}
	/**
	 * Transform form normalized device coordinates to Eye clip coordinates by inverting projectionMatrix
	 * @param mouseCoords
	 */
	
	public Vector4f setEyeCoords(Vector4f mouseCoords) {
		Matrix4f invertedProjectionMatrix = this.projectionMatrix.invert();
		Vector4f eyeCoords = invertedProjectionMatrix.transform(mouseCoords);
		return eyeCoords;
	}
	
	/**
	 * Transform from Eye clip Coordinates to Camera view coordinates by inverting cameraMatrix
	 */
	public Vector4f setCameraViewCoords(Vector4f eyeCoords) {
		Matrix4f invertedCameraViewMatrix = this.cameraViewMatrix.invert();
		Vector4f cameraViewCoordinates = invertedCameraViewMatrix.transform(eyeCoords);
		return cameraViewCoordinates;
		
	}
	
}
