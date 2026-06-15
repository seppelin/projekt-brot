package src.game;

import com.raylib.Colors;
import com.raylib.Raylib;
import src.ui.SelItemInterface;

import java.io.Serializable;

// Types of map fields
public enum FieldType implements SelItemInterface, Serializable {
    GRASS(0, "grass", true, "resources/fields/grass.png"),
    WATER(1, "water", false, "resources/fields/water.png"),
    VOID(2, "void", false, ""),
    GRASSDARK(3, "dark-grass", true, "resources/fields/grasdunkel.png");

    public final int id;
    public final String name;
    public final boolean walkable;
    public final Raylib.Texture texture;

    FieldType(int id, String name, boolean walkable, String path) {
        this.id = id;
        this.name = name;
        this.walkable = walkable;
        // Load texture once during initialization
        if (path.isEmpty()) {
            var img = Raylib.GenImageColor(16, 16, Colors.BLANK);
            texture = Raylib.LoadTextureFromImage(img);
        } else {
            texture = Raylib.LoadTexture(path);
        }
    }

    // Get field type by id
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
