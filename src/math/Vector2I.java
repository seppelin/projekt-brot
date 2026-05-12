package src.math;

import com.raylib.Helpers;
import com.raylib.Raylib;

public class Vector2I {
    public int x;
    public int y;

    public Vector2I(Raylib.Vector2 v) {
        this.x = (int) v.x();
        this.y = (int) v.y();
    }

    public Vector2I(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2I add(Vector2I other) {
        return new Vector2I(x + other.x, y + other.y);
    }

    public int get(int i) {
        if (i == 0) {
            return x;
        } else {
            return y;
        }
    }

    public void set(int i, int v) {
        if (i == 0) {
            x = v;
        } else {
            y = v;
        }
    }

    public Raylib.Vector2 rl() {
        return Helpers.newVector2(x, y);
    }
}
