package game.Items;

import org.joml.Vector3f;

import game.Engine.Loader;
import game.Engine.OBJreader;
import game.Engine.StaticModelLoader;

public class SkyBox extends Item{	
	private Vector3f ambientLight;
	public SkyBox(String fileName, String texturePath, Loader loader) {
		StaticModelLoader itemLoader = new StaticModelLoader();
		itemLoader.addLoader(loader);
		Mesh[] skyboxMeshes = itemLoader.load(fileName, texturePath);
		this.ambientLight = new Vector3f(1f, 1f, 1f);
		super.setMesh(skyboxMeshes);
	}
	
	public Vector3f getAmbientLight() {
		return this.ambientLight;
	}
	
	public void setAmbientLight(Vector3f light) {
		this.ambientLight = light;
	}
}
