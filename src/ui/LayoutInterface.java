package src.ui;

import src.math.RectangleI;
import src.math.Vector2I;

public interface LayoutInterface {
    void debugDraw();

    void setSpace(RectangleI rect);

    Vector2I minimum();

    /**
     * @return Should return the extra space above the minimum which could be assigned
     * When size is constant this is 0, when grow infinite this is very big
     */
    Vector2I variableSize();

    default Vector2I maximum() {
        return minimum().add(variableSize());
    }
}
