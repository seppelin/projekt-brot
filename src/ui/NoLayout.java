package src.ui;

import src.math.RectangleI;
import src.math.Vector2I;

// Empty layout (no layout applied)
public class NoLayout implements LayoutInterface {
    @Override
    public void setSpace(RectangleI rect) {
    }

    @Override
    public Vector2I minimum() {
        return new Vector2I(0, 0);
    }
}
