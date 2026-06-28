package src.math;

import com.raylib.Helpers;
import com.raylib.Raylib;

// Integer-based 2D vector class
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

    // Add vectors
    public Vector2I add(Vector2I other) {
        return new Vector2I(x + other.x, y + other.y);
    }

    // Subtract vectors
    public Vector2I sub(Vector2I other) {
        return new Vector2I(x - other.x, y - other.y);
    }

    // Get component by index (0=x, 1=y)
    public int get(int i) {
        return i == 0 ? x : y;
    }

    // Set component by index (0=x, 1=y)
    public void set(int i, int v) {
        if (i == 0) {
            x = v;
        } else {
            y = v;
        }
    }

    // Get minimum of two vectors
    public Vector2I min(Vector2I other) {
        return new Vector2I(Math.min(x, other.x), Math.min(y, other.y));
    }

    // Get maximum of two vectors
    public Vector2I max(Vector2I other) {
        return new Vector2I(Math.max(x, other.x), Math.max(y, other.y));
    }

    // Convert to raylib Vector2
    public Raylib.Vector2 rl() {
        return Helpers.newVector2(x, y);
    }

    public Raylib.Rectangle centeredRect(Raylib.Vector2 pos) {
        return Helpers.newRectangle(pos.x() - (float) x / 2, pos.y() - (float) y / 2, x, y);
    }

    public boolean equals(Vector2I other) {
        return x == other.x && y == other.y;
    }
}
