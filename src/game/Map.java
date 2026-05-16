package src.game;

import com.raylib.Helpers;
import src.ui.InputHandle;
import src.ui.SelItemInterface;

import java.io.Serial;
import java.io.Serializable;
import java.util.function.BiConsumer;

import static com.raylib.Raylib.*;

public class Map implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int width;
    private final int height;

    private final Field[][] fields;

    public BiConsumer<Integer, Integer> onFieldClick;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;

        fields = new Field[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                fields[x][y] = new Field(FieldType.GRASS);
            }
        }
    }

    public Field getField(int x, int y) {
        return fields[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void update(InputHandle ih, Camera camera) {
        var worldMousePos = GetScreenToWorld2D(GetMousePosition(), camera);
        if (!IsMouseButtonPressed(MOUSE_BUTTON_RIGHT)) {
            return;
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (CheckCollisionPointRec(worldMousePos, Helpers.newRectangle(x * 16, y * 16, 16, 16)) && ih.tryTakeMouse()) {
                    onFieldClick.accept(x, y);
                }
            }
        }
    }

    public void batchUpdate(int x, int y, FieldType type, FieldType old) {
        if (old == null) {
            old = fields[x][y].type;
        }
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return;
        }
        var field = fields[x][y];
        if (field.type == type || field.type != old) {
            return;
        }

        field.setType(type);

        batchUpdate(x, y - 1, type, old);
        batchUpdate(x, y + 1, type, old);
        batchUpdate(x - 1, y, type, old);
        batchUpdate(x + 1, y, type, old);
    }

    public void updateSelection(InputHandle inputHandle, SelItemInterface selected, Camera camera) {
        var worldMousePos = GetScreenToWorld2D(GetMousePosition(), camera);
        if (IsMouseButtonPressed(MOUSE_BUTTON_RIGHT)) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (CheckCollisionPointRec(worldMousePos, Helpers.newRectangle(x * 16, y * 16, 16, 16)) && inputHandle.tryTakeMouse()) {
                        if (selected instanceof FieldType) {
                            getField(x, y).type = (FieldType) selected;
                        } else {
                            getField(x, y).setItem((ItemType) selected);
                        }
                    }
                }
            }
        }
    }

    public void draw() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                getField(x, y).draw(x * 16, y * 16);
            }
        }
    }

    public Vector2 nearestValidPosition(Vector2 point) {
        int fieldX = (int) (point.x() / 16);
        int fieldY = (int) (point.y() / 16);
        for (int ringSize = 0; ringSize < this.width; ringSize++) {
            float distance = 10000;
            Vector2 validPos = null;
            for (int offsetX = 0; offsetX <= 2 * ringSize; offsetX++) {
                for (int offsetY = 0; offsetY <= 2 * ringSize; offsetY++) {
                    if (offsetX != 0 && offsetX != 2 * ringSize && offsetY != 0 && offsetY != 2 * ringSize) {
                        continue;
                    }
                    var x = fieldX + offsetX - ringSize;
                    var y = fieldY + offsetY - ringSize;
                    if (x >= width || y >= width || x < 0 || y < 0) {
                        continue;
                    }
                    var xPos = x * 16;
                    var yPos = y * 16;
                    if (getField(x, y).isWalkable()) {
                        var nearestPoint = Vector2Clamp(point, Helpers.newVector2(xPos, yPos), Helpers.newVector2(xPos + 16, yPos + 16));
                        var newDistance = Vector2Distance(nearestPoint, point);
                        if (distance > newDistance) {
                            validPos = nearestPoint;
                            distance = newDistance;
                        }
                    }
                }
            }
            if (validPos != null) {
                return validPos;
            }
        }
        return point;
    }
}
