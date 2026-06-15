package src.ui;

import src.math.RectangleI;
import src.math.Vector2I;

// Interface for layout containers
public interface LayoutInterface {
    // Set space allocated for this layout
    void setSpace(RectangleI rect);

    // Get minimum space required
    Vector2I minimum();

    /**
     * @return greediness for extra space (0=fixed, 1+=grows)
     */
    default float extraSpaceGreed() {
        return 1;
    }

    // Set space with validation
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
