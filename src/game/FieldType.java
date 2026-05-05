package src.game;


import com.raylib.Raylib;
import src.ui.SelectorItem;

public enum FieldType implements SelectorItem {
    GRASS(0, "grass", true, "resources/grass.png"),
    WATER(1, "water", false, "resources/water.png");

    public final int id;
    public final String name;
    public final boolean walkable;
    public final Raylib.Texture texture;

    FieldType(int id, String name, boolean walkable, String path) {
        this.id = id;
        this.name = name;
        this.walkable = walkable;
        // This only loads once when the game starts
        this.texture = Raylib.LoadTexture(path);
    }

    public static FieldType getById(int id) {
        for (FieldType type : FieldType.values()) {
            if (type.id == id) {
                return type;
            }
        }
        return null;
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
