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
    GRASSDARK(3, "dark-grass", true, "resources/fields/grasdunkel.png"),
    BLUEBRICK(4, "blue-brick", true, "resources/fields/bluebrick.png"),
    BRICK(5, "brick", true, "resources/fields/brick.png"),
    DARKSTONE(6, "darkstone", true, "resources/fields/darkstone.png"),
    STONESTRIPES(7, "stonestripes", false, "resources/fields/stonestripes.png"),
    MEDDL(8, "meddl", true, "resources/fields/meddl.png"),
    MOSSYSTONE(9, "mossystone", true, "resources/fields/mossystone.png"),
    REDSAND(10, "redsand", true, "resources/fields/redsand.png"),
    REDSTONE(11, "redstone", true, "resources/fields/redstone.png"),
    SAND(12, "sand", true, "resources/fields/sand.png"),
    STONE(13, "stone", true, "resources/fields/stone.png"),
    WOOD(14, "wood", true, "resources/fields/wood.png"),
    WOODFENCE(15, "woodfence", false, "resources/fields/woodfence.png"),
    ;

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
}
