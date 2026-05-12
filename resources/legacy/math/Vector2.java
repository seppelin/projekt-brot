package resources.legacy.math;

import com.raylib.Raylib;

public class Vector2 extends Raylib.Vector2 {
    public Vector2(float x, float y) {
        super();
        x(x);
        y(y);
    }

    public float get(int i) {
        if (i == 0) {
            return x();
        } else {
            return y();
        }
    }

    public void set(int i, float v) {
        if (i == 0) {
            x(v);
        } else {
            y(v);
        }
    }
}
