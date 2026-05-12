package src.ui;

import com.raylib.Raylib;

public interface LayoutElement {
    void debugDraw();

    void setSpace(Raylib.Rectangle rect);

    Raylib.Vector2 minimum();

    /**
     * @return Should return the extra space above the minimum which could be assigned
     * When size is constant this is 0, when grow infinite this is very big
     */
    Raylib.Vector2 variableSize();

    default Raylib.Vector2 maximum() {
        return Raylib.Vector2Add(minimum(), variableSize());
    }
}
