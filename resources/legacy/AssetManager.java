package resources.legacy;

import com.raylib.Raylib;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class AssetManager {
    private static int nextAssetID = 0;
    private static final Map<String, AssetID> idMap = new HashMap<>();
    private static final Vector<Raylib.Texture> textures = new Vector<>();

    private AssetManager() {}

    /**
     * Should be called once after raylib init window, so that all the textures are in the opengl context
     */
    public static void init() {
        // fewer allocations
        textures.ensureCapacity(nextAssetID);

        idMap.forEach((assetName, assetID) -> {
            if (textures.get(assetID.id()) == null) {
                textures.set(assetID.id(), Raylib.LoadTexture(assetName));
            }
        });
    }

    /**
     * Is often called statically to then load the asset in the opengl context
     * @param assetName path of the assets like "resources/grass.png"
     * @return the ID to access the asset
     */
    public static AssetID registerAsset(String assetName) {
        var assetID = idMap.get(assetName);
        if (assetID == null) {
            assetID = new AssetID(nextAssetID++);
            idMap.put(assetName, assetID);
            textures.add(assetID.id(), null);
        }
        return assetID;
    }

    /**
     * Should be called after raylib init
     * @param assetName path of the asset to update or just a unique name
     * @param texture the texture you want to set
     * @return the id to access the texture
     */
    public static AssetID setAsset(String assetName, Raylib.Texture texture) {
        var assetID = idMap.get(assetName);
        if (assetID == null) {
            assetID = new AssetID(nextAssetID++);
            idMap.put(assetName, assetID);
            textures.add(assetID.id(), texture);
        } else {
            textures.set(assetID.id(), texture);
        }
        return assetID;
    }

    public static Raylib.Texture getTexture(AssetID assetID) {
        return textures.get(assetID.id());
    }
}
