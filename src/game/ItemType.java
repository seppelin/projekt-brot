package src.game;

import com.raylib.Raylib;
import src.ui.SelItemInterface;

public enum ItemType implements SelItemInterface {
    Brot(0, "brot", "resources/buildings/brot.png");

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
