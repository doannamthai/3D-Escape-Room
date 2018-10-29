package game.Engine;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import game.Camera.Camera;
import game.Items.Item;
import game.Items.Mesh;
import game.Shaders.ShaderProgram;
import game.Shaders.ShadowMap;
import game.Texture.Texture;
import game.Toolbox.Maths;
import game.Toolbox.Transformation;

/**
 * Renderer Class
 * @author Thai Doan
 *
 */
public class Renderer {
	
	public void render(Mesh mesh) {
			bindData(mesh);
			GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			//mesh.getMaterial().getTexture().cleanup();
			unbindData();
	}	
	
	public void bindData(Mesh mesh) {
		GL30.glBindVertexArray(mesh.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

		if (mesh.hasTexture()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, mesh.getMaterial().getTexture().getTexTureID());
		}		
	    if (mesh.getMaterial().hasNormalMap()) {
	        // Activate first texture bank
	        GL13.glActiveTexture(GL13.GL_TEXTURE1);
	        // Bind the texture
	        glBindTexture(GL_TEXTURE_2D, mesh.getMaterial().getNormalMap().getTexTureID());
	    }

	}

	public void unbindData() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
		glBindTexture(GL_TEXTURE_2D, 0);
		
	}

}
