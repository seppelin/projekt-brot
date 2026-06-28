package src.game;

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;

import java.io.Serial;
import java.io.Serializable;

public record Field(FieldType type, BuildingType building) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public boolean isWalkable() {
        return this.type.walkable;
    }

    public Field withBuilding(BuildingType building) {
        return new Field(type, building);
    }

    public Field withType(FieldType type) {
        return new Field(type, building);
    }

    // Draw field and item on it
    public void draw(int x, int y) {
        Raylib.DrawTexture(type.texture, x, y, Colors.WHITE);
        if (building != null) {
            Raylib.DrawTexturePro(building.texture,
                    Helpers.newRectangle(0, 0, building.texture.width(), building.texture.height()),
                    Helpers.newRectangle(x, y, 16, 16),
                    Helpers.newVector2(0, 0), 0f, Colors.WHITE);
        }
    }
}
