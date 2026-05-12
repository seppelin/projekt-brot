package src.game;

import java.io.Serial;
import java.io.Serializable;

public class Field implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    FieldType type;
    public Field(FieldType type) {
        this.type = type;
    }

    public boolean isWalkable() {
        return this.type.walkable;
    }
    
    public void setType(FieldType type) {
        this.type = type;
    }
}
