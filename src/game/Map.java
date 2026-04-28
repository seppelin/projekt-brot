package game;

import com.raylib.Colors;

import static com.raylib.Raylib.*;

public class Map {
    private final int width;
    private final int height;

    private Player p;

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

        this.p = new Player(10, 10);
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
        p.handleInput();
        p.update();
    }

    public void draw() {
        BeginMode2D(p.getCamera());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                DrawTexture(getField(x, y).type.texture(), x*16, y*16, Colors.WHITE);
            }
        }
        p.draw();
        EndMode2D();
    }
}
