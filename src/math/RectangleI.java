package src.math;

import com.raylib.Helpers;
import com.raylib.Raylib;

public class RectangleI {
    public Vector2I pos;
    public Vector2I size;

    public RectangleI(int x, int y, int width, int height) {
        pos = new Vector2I(x, y);
        size = new Vector2I(width, height);
    }

    public RectangleI(Vector2I pos, Vector2I size) {
        this.pos = pos;
        this.size = size;
    }

    public Raylib.Rectangle rl() {
        return Helpers.newRectangle(pos.x, pos.y, size.x, size.y);
    }
}
