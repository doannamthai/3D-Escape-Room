package game.Engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import game.Light.PointLight;
import game.Shaders.ShaderProgram;

public class LightRenderer {
	
	private ShaderProgram shader;
	private float specularPower;
	
	public LightRenderer(ShaderProgram shader, float specularPower) {
		this.shader = shader;
		this.specularPower = specularPower;
	}


	public void renderLights(Matrix4f viewMatrix, Vector3f ambientLight,
            PointLight[] pointLightList) {
		
		shader.setUniform("ambientLight", ambientLight);
        shader.setUniform("specularPower", specularPower);

        // Process Point Lights
        int numLights = pointLightList != null ? pointLightList.length : 0;
        for (int i = 0; i < numLights; i++) {
            // Get a copy of the point light object and transform its position to view coordinates
            PointLight currPointLight = new PointLight(pointLightList[i]);
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            shader.setUniform("pointLights", currPointLight, i);
        }
	}
}
