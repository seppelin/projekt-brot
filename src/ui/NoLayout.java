package src.ui;

import com.raylib.Raylib;

public class NoLayout implements LayoutElement {
    @Override
    public void debugDraw() {
        
    }

    @Override
    public void setSpace(Raylib.Rectangle rect) {
    }

    @Override
    public Raylib.Vector2 minimum() {
        return new Raylib.Vector2();
    }

    @Override
    public Raylib.Vector2 variableSize() {
        return new Raylib.Vector2();
    }
}
