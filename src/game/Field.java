package src.game;

import com.raylib.Colors;
import com.raylib.Raylib;
import com.raylib.Helpers;

import src.scenes.PlayScene;

import java.io.Serial;
import java.io.Serializable;
import java.util.Vector;

public class Field implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    FieldType type;
    ItemType item;

    public Field(FieldType type) {
        this.type = type;
    }
    
    public void update(int x, int y, Vector<Enemy> enemies) {
        if (item == ItemType.Spawner && Math.random() > 0.999) {
            enemies.add(new Enemy(x, y));
        }
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
            Raylib.DrawTexturePro(item.texture,
                Helpers.newRectangle(0, 0, item.texture.width(), item.texture.height()),
                Helpers.newRectangle(x, y, 16, 16), 
                Helpers.newVector2(0, 0), 0f, Colors.WHITE);
        }
    }
}
