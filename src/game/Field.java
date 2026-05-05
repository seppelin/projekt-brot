package src.game;

import java.io.Serializable;

public class Field implements Serializable {
    FieldType type;
    public Field(FieldType type) {
        this.type = type;
    }

    public boolean isWalkable() {
        return this.type.walkable;
    }
}
