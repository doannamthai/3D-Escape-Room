package game.Shaders;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL20.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import game.Light.DirectionalLight;
import game.Light.PointLight;
import game.Light.SpotLight;
import game.Light.PointLight.Attenuation;
import game.Texture.Material;

public class ShaderProgram {

    private final int programId;

    private int vertexShaderId;

    private int fragmentShaderId;

    private final Map<String, Integer> uniforms;

    public ShaderProgram() {
        programId = glCreateProgram();
        if (programId == 0) {
            try {
				throw new Exception("Could not create Shader");
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        uniforms = new HashMap<>();
    }

    public void createUniform(String uniformName) {
        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            try {
				throw new Exception("Could not find uniform:" + uniformName);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        uniforms.put(uniformName, uniformLocation);
    }
    


    public void createVertexShader(String shaderCode){
        try {
			vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public void createFragmentShader(String shaderCode){
        try {
			fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    protected int createShader(String shaderCode, int shaderType) {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            try {
				throw new Exception("Error creating shader. Type: " + shaderType);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            try {
				throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }
    
    public void setVertexShader(int id) {
    	vertexShaderId = id;

    }
    
    public void setFragmentShader(int id) {
    	fragmentShaderId = id;

    }

    public void link() {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            try {
				throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }
    
    public int loadShader(String file, int type){
        StringBuilder shaderSource = new StringBuilder();
        try{
        	InputStream in = Class.class.getResourceAsStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = reader.readLine())!=null){
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);
        //GL20.glGetShaderi(arg0, arg1)
        if(glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
        glAttachShader(programId, shaderID);
        return shaderID;
    }

    
    /******* Lighting ********/
    public void createPointLightUniform(String uniformName) {
    	createUniform(uniformName + ".colour");
        createUniform(uniformName + ".position");
		createUniform(uniformName + ".intensity");
		createUniform(uniformName + ".att.constant");
        createUniform(uniformName + ".att.linear");
        createUniform(uniformName + ".att.exponent");
    }

    public void createMaterialUniform(String uniformName) {
		createUniform(uniformName + ".ambient");
		createUniform(uniformName + ".diffuse");
        createUniform(uniformName + ".specular");
        createUniform(uniformName + ".hasTexture");
       // createUniform(uniformName + ".hasNormalMap");
        createUniform(uniformName + ".reflectance");
        
    }
    
    public void createDirectionalLightUniform(String uniformName) {
        createUniform(uniformName + ".colour");
        createUniform(uniformName + ".direction");
        createUniform(uniformName + ".intensity");
    }

    
    public void createSpotLightUniform(String uniformName) {
    	createPointLightUniform(uniformName + ".pl");
        createUniform(uniformName + ".conedir");
        createUniform(uniformName + ".cutoff");
    }
    
    public void createPointLightListUniform(String uniformName, int size){
        for (int i = 0; i < size; i++) {
            createPointLightUniform(uniformName + "[" + i + "]");
        }
    }

    public void createSpotLightListUniform(String uniformName, int size){
        for (int i = 0; i < size; i++) {
            createSpotLightUniform(uniformName + "[" + i + "]");
        }
    }
    
    /***** Set Uniform *****/
    
    public void setUniform(String uniformName, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Dump the matrix into a float buffer
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
        }
    }

    public void setUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, Vector3f value) {
        glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }
    
    public void setUniform(String uniformName, float value) {
        glUniform1f(uniforms.get(uniformName), value);
    }


    public void setUniform(String uniformName, Vector4f value) {
        glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
    }
    
    public void setUniform(String uniformName, PointLight[] pointLights) {
        int numLights = pointLights != null ? pointLights.length : 0;
        for (int i = 0; i < numLights; i++) {
            setUniform(uniformName, pointLights[i], i);
        }
    }

    public void setUniform(String uniformName, PointLight pointLight, int pos) {
        setUniform(uniformName + "[" + pos + "]", pointLight);
    }

    public void setUniform(String uniformName, PointLight pointLight) {
        setUniform(uniformName + ".colour", pointLight.getColor());
        setUniform(uniformName + ".position", pointLight.getPosition());
        setUniform(uniformName + ".intensity", pointLight.getIntensity());
        PointLight.Attenuation att = pointLight.getAttenuation();
        setUniform(uniformName + ".att.constant", att.getConstant());
        setUniform(uniformName + ".att.linear", att.getLinear());
        setUniform(uniformName + ".att.exponent", att.getExponent());
    }

    public void setUniform(String uniformName, SpotLight[] spotLights) {
        int numLights = spotLights != null ? spotLights.length : 0;
        for (int i = 0; i < numLights; i++) {
            setUniform(uniformName, spotLights[i], i);
        }
    }

    public void setUniform(String uniformName, SpotLight spotLight, int pos) {
        setUniform(uniformName + "[" + pos + "]", spotLight);
    }

    public void setUniform(String uniformName, SpotLight spotLight) {
        setUniform(uniformName + ".pl", spotLight.getPointLight());
        setUniform(uniformName + ".conedir", spotLight.getConeDirection());
        setUniform(uniformName + ".cutoff", spotLight.getCutOff());
    }

    public void setUniform(String uniformName, DirectionalLight dirLight) {
        setUniform(uniformName + ".colour", dirLight.getColor());
        setUniform(uniformName + ".direction", dirLight.getDirection());
        setUniform(uniformName + ".intensity", dirLight.getIntensity());
    }

    public void setUniform(String uniformName, Material material) {
        setUniform(uniformName + ".ambient", material.getAmbientColour());
        setUniform(uniformName + ".diffuse", material.getDiffuseColour());
        setUniform(uniformName + ".specular", material.getSpecularColour());
        setUniform(uniformName + ".hasTexture", material.isTextured() ? 1 : 0);
        //setUniform(uniformName + ".hasNormalMap", material.hasNormalMap() ? 1 : 0);
        setUniform(uniformName + ".reflectance", material.getReflectance());
    }
}