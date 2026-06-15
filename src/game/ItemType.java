package src.game;

import com.raylib.Raylib;
import src.ui.SelItemInterface;

// Types of items that can be placed on fields
public enum ItemType implements SelItemInterface {
    Brot(0, "brot", "resources/buildings/brot.png"),
    Spawner(1, "spawner", "resources/buildings/spawner.png");
    
    public final int id;
    public final String name;
    public final Raylib.Texture texture;

    ItemType(int id, String name, String path) {
        this.id = id;
        this.name = name;
        this.texture = Raylib.LoadTexture(path);
    }

    @Override
    public Raylib.Texture getTexture() {
        return this.texture;
    }

    @Override
    public int getId() {
        return this.id;
    }
}
