package src.ui;

import src.math.RectangleI;
import src.math.Vector2I;

public interface LayoutInterface {
    void setSpace(RectangleI rect);

    Vector2I minimum();

    /**
     * @return returns the greed you want extra space with
     * if 0 you are fixed size
     * there is only fixed size and infinite growing
     * default is 1
     */
    default float extraSpaceGreed() {
        return 1;
    }

    default void setSpaceSafe(RectangleI rect) {
        Vector2I min = minimum();
        if (min.x > rect.size.x || min.y > rect.size.y) {
            throw new RuntimeException("Invalid rectangle: too small");
        }
        if (extraSpaceGreed() == 0) {
            rect.size.x = min.x;
            rect.size.y = min.y;
        }
        setSpace(rect);
    }
}
