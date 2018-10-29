package game.Toolbox;

import static GameGraphic.CoordsAndData.HEIGHT;
import static GameGraphic.CoordsAndData.WIDTH;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import game.Camera.Camera;
import game.Items.Item;

public class Transformation {
	
	public static Matrix4f getCameraViewMatrix(Camera camera) {
		Matrix4f cameraViewMatrix = new Matrix4f();
	    Vector3f cameraPos = camera.getPosition();
	    Vector3f rotation = camera.getRotation();

	    //cameraViewMatrix.identity();
	    // Rotation
	    cameraViewMatrix.rotateX( (float)Math.toRadians(-rotation.x) );
	    cameraViewMatrix.rotateY( (float)Math.toRadians(-rotation.y) );
	    cameraViewMatrix.rotateZ( (float)Math.toRadians(-rotation.z) );

	    // Translation
	    cameraViewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
	    
	    // Save the matrix
	    camera.setCameraMatrix(cameraViewMatrix);
	    return cameraViewMatrix;
	}
	
	public static Matrix4f getModelViewMatrix(Item model, Camera camera) {
		Matrix4f cameraViewMatrix = camera.getCameraMatrix();
	    Vector3f rotation = model.getRotation();
	    Vector3f position = model.getPosition();
	    float scale       = model.getScale();
	    Matrix4f modelViewMatrix = new Matrix4f();
	   // modelViewMatrix.identity();
	    
	    modelViewMatrix.translate(position);
	    
	    modelViewMatrix.rotateX( (float)Math.toRadians(rotation.x) );
	    modelViewMatrix.rotateY( (float)Math.toRadians(rotation.y) );
	    modelViewMatrix.rotateZ( (float)Math.toRadians(rotation.z) );
	    modelViewMatrix.scale(scale);
	    Matrix4f currentMatrixView = new Matrix4f(cameraViewMatrix);
	    Matrix4f updatedMatrixView = currentMatrixView.mul(modelViewMatrix);
	    
	    return updatedMatrixView;
	}
	
    public static float transformX(float x) {
		float screenX = (x + 1f)*WIDTH/2;
		return screenX;
	}
	
	public static float transformY(float y) {
		float screenY = -(y - 1f)*HEIGHT/2;
		return screenY;
	}
	
	public static float transformHeight(float s) {
		float size = s*HEIGHT/2;
		return size;
	}
	
	public static float transformWidth(float w) {
		float width = w*WIDTH/2;
		return width;
	}
	
}
