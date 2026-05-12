package resources.legacy.math;

import com.raylib.Raylib;

public class Rectangle extends Raylib.Rectangle {
    public Rectangle(float x, float y, float width, float height) {
        super();
        x(x);
        y(y);
        width(width);
        height(height);
    }

    public Rectangle(Vector2 pos, Vector2 size) {
        super();
        x(pos.x());
        y(pos.y());
        width(size.x());
        height(size.y());
    }

    public Vector2 positionV() {
        return new Vector2(x(), y());
    }

    public Vector2 sizeV() {
        return new Vector2(width(), height());
    }
}
