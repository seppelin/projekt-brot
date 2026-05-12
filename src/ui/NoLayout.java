package src.ui;

import src.math.RectangleI;
import src.math.Vector2I;

public class NoLayout implements LayoutInterface {
    @Override
    public void debugDraw() {

    }

    @Override
    public void setSpace(RectangleI rect) {
    }

    @Override
    public Vector2I minimum() {
        return new Vector2I(0, 0);
    }

    @Override
    public Vector2I variableSize() {
        return new Vector2I(0, 0);
    }
}
