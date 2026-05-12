package src.game;

import com.raylib.Colors;
import com.raylib.Raylib;

import java.io.Serial;
import java.io.Serializable;

public class Field implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    FieldType type;
    ItemType item;

    public Field(FieldType type) {
        this.type = type;
    }

    public boolean isWalkable() {
        return this.type.walkable;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public void setItem(ItemType item) {
        this.item = item;
    }

    public void draw(int x, int y) {
        Raylib.DrawTexture(type.texture, x, y, Colors.WHITE);
        if (item != null) {
            Raylib.DrawTexture(item.texture, x, y, Colors.WHITE);
        }
    }
}
