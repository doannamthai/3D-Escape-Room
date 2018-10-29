package game.Texture;

import java.util.HashMap;
import java.util.Map;

public class Cache {

    private static Cache INSTANCE;

    private Map<String, Texture> texturesMap;
    
    private Cache() {
        texturesMap = new HashMap<>();
    }
    
    public static synchronized Cache getInstance() {
        if ( INSTANCE == null ) {
            INSTANCE = new Cache();
        }
        return INSTANCE;
    }
    
    public Texture getTexture(String path) throws Exception {
        Texture texture = texturesMap.get(path);
        if ( texture == null ) {
            texture = new Texture(path);
            texturesMap.put(path, texture);
        }
        return texture;
    }
}
