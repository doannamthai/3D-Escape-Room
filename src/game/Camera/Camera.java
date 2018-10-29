package game.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    private final Vector3f position;

    private final Vector3f rotation;
    
    private Matrix4f cameraViewMatrix;
    
    private final float velocity = 1/10f;

    public Camera() {
        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
    }

    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public Camera(Camera camera) {
    	this.position = camera.position;
    	this.rotation = camera.rotation;
    	this.cameraViewMatrix = new Matrix4f (camera.getCameraMatrix());
    }
    
    public Matrix4f getCameraMatrix() {
    	return this.cameraViewMatrix;
    }
    
    public Camera getCopy() {
    	Camera camera = new Camera(this);
    	return camera;
    }
    
    public void setCameraMatrix(Matrix4f cameraMatrix) {
    	this.cameraViewMatrix = cameraMatrix;
    }
    
    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        position.y += offsetY * velocity;
        if ( offsetZ != 0 ) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y)) * offsetZ ;
            position.z += (float)Math.cos(Math.toRadians(rotation.y)) * offsetZ ;
        }
        if ( offsetX != 0) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * offsetX;
            position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
    	if(goodAngle(offsetX)) {
    		rotation.x += offsetX;
    		rotation.y += offsetY;
    		rotation.z += offsetZ;
    	}
    }
    public boolean goodAngle(float deltaX) {
    	float maxAngle = 45.0f;
    	float minAngle = -45.0f;
    	float newAngle = rotation.x + deltaX;
    	return (minAngle <= newAngle && newAngle <= maxAngle);
    }
}