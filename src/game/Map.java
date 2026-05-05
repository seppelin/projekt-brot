package src.game;

import com.raylib.Colors;
import com.raylib.Helpers;
import src.scenes.SceneManager;
import src.ui.Button;
import src.ui.InputHandle;

import java.io.*;

import static com.raylib.Raylib.*;

public class Map implements Serializable {
    private final int width;
    private final int height;

    private Field[][] fields;

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

    public void update() {

    }

    public void updateEdits(InputHandle inputHandle, FieldType selected, Camera camera) {
        var worldMousePos = GetScreenToWorld2D(GetMousePosition(),  camera);
        if (IsMouseButtonPressed(MOUSE_BUTTON_RIGHT)) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (CheckCollisionPointRec(worldMousePos, Helpers.newRectangle(x*16, y*16, 16, 16)) && inputHandle.tryTakeMouse()) {
                        getField(x, y).type = selected;
                    }
                }
            }
        }
    }

    public void draw() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                DrawTexture(getField(x, y).type.texture, x*16, y*16, Colors.WHITE);
            }
        }
    }
}
