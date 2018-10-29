package game.Texture;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.*;

import org.lwjgl.system.MemoryStack;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL30.*;
public class Texture {
    private final int id;
    private final int width;
    private final int height;
    
    // Constructor
    public Texture(String filePath) {
    	 ByteBuffer image;
    	 IntBuffer w;
    	 IntBuffer h;
    	 IntBuffer comp;
         try (MemoryStack stack = MemoryStack.stackPush()) {
             /* Prepare image buffers */
             w = stack.mallocInt(1);
             h = stack.mallocInt(1);
             comp = stack.mallocInt(1);
             /* Load image */
             //stbi_set_flip_vertically_on_load(true);
             image = stbi_load(filePath, w, h, comp, 4);
             if (image == null) {
                 throw new RuntimeException("Failed to load a texture file!"
                                            + System.lineSeparator() + stbi_failure_reason());
             }
         }
         /* Get width and height of image */
             width = w.get();
             height = h.get();
             id = glGenTextures();

             /* Binding textures */
             glBindTexture(GL_TEXTURE_2D, id);
             glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
             
             glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

             glGenerateMipmap(GL_TEXTURE_2D);

             /* Setting parameter */
             glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
             //glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
             //glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
             glTexParameterf(GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1);
             if (GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
            	 float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
            	 glTexParameterf(GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, amount);
             }
             else {
            	 System.out.println("[Texture]: Does not support");
             }
    }
    public Texture(int width, int height, int pixelFormat) {
        this.id = glGenTextures();
        this.width = width;
        this.height = height;
        glBindTexture(GL_TEXTURE_2D, this.id);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, this.width, this.height, 0, pixelFormat, GL_FLOAT, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    }
    
    public void bind() {
    	glBindTexture(GL_TEXTURE_2D, id);
    }
    
    
    public int getTexTureID() {
    	return id;
    }
    
    public int getWidth() {
    	return this.width;
    }
    
    public int getHeight() {
    	return this.height;
    }
    
    public void cleanup() {
        glDeleteTextures(id);
    }
}