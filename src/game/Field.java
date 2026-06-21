package src.game;

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;

import java.io.Serial;
import java.io.Serializable;

public class Field implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public FieldType type;
    public BuildingType item;

    public Field(FieldType type) {
        this.type = type;
    }

    public boolean isWalkable() {
        return this.type.walkable;
    }

    // Draw field and item on it
    public void draw(int x, int y) {
        Raylib.DrawTexture(type.texture, x, y, Colors.WHITE);
        if (item != null) {
            Raylib.DrawTexturePro(item.texture,
                    Helpers.newRectangle(0, 0, item.texture.width(), item.texture.height()),
                    Helpers.newRectangle(x, y, 16, 16),
                    Helpers.newVector2(0, 0), 0f, Colors.WHITE);
        }
    }
}
