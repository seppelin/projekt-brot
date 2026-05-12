package src.ui;

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;

public class Selector implements UiElement, LayoutElement {
    static Raylib.Texture outlineTexture;

    Raylib.Vector2 pos;
    int itemsPerRow;
    SelectorItem[] items;
    int hovered;
    int clicked;
    int selected;
    float scale;

    public Selector(Raylib.Vector2 pos, int itemsPerRow, SelectorItem[] items, float scale) {
        if (outlineTexture == null) {
            var img = Raylib.GenImageColor(16, 16, Colors.BLANK);
            Raylib.ImageDrawRectangleLines(img, Helpers.newRectangle(0, 0, 16, 16), 1, Colors.BLACK);
            outlineTexture = Raylib.LoadTextureFromImage(img);
        }
        this.pos = pos;
        this.itemsPerRow = itemsPerRow;
        this.items = items;
        this.scale = scale;
        selected = 0;
        hovered = -1;
        clicked = -1;
    }

    public int getSelectedID() {
        return items[selected].getId();
    }

    private Raylib.Rectangle getLocalBox(int i) {

        var x = i % this.itemsPerRow;
        var y = i / this.itemsPerRow;
        return Helpers.newRectangle((x * 16 * 1.1F * scale), (y * 16 * 1.1F * scale), 16 * scale, 16 * scale);
    }

    private Raylib.Rectangle getBox(int i) {
        var rect = getLocalBox(i);
        rect.x(rect.x() + this.pos.x());
        rect.y(rect.y() + this.pos.y());
        return rect;
    }

    @Override
    public void update(InputHandle inputHandle) {
        hovered = -1;
        clicked = -1;
        var mousePos = Raylib.GetMousePosition();
        for (int i = 0; i < items.length; i++) {
            if (Raylib.CheckCollisionPointRec(mousePos, getBox(i))) {
                this.hovered = i;
                if (Raylib.IsMouseButtonPressed(Raylib.MOUSE_BUTTON_LEFT) && inputHandle.tryTakeMouse()) {
                    this.clicked = i;
                    this.selected = i;
                }
            }
        }
    }

    @Override
    public void draw() {
        for (int i = 0; i < items.length; i++) {
            var rect = getBox(i);
            float hoverScale = hovered == i ? .1f : 0f;
            var tint = this.selected == i ? Colors.WHITE : Colors.GRAY;
            UiHelper.drawTextureScale(rect, items[i].getTexture(), this.scale, hoverScale, tint);
            UiHelper.drawTextureScale(rect, outlineTexture, this.scale, hoverScale, Colors.WHITE);
        }
    }

    @Override
    public void debugDraw() {

    }

    @Override
    public void setSpace(Raylib.Rectangle rect) {
        this.pos.x(rect.x());
        this.pos.y(rect.y());
    }

    @Override
    public Raylib.Vector2 minimum() {
        var x = getLocalBox(Math.min(itemsPerRow - 1, items.length - 1));
        var y = getLocalBox(items.length);
        var width = x.x() + x.width();
        var height = y.y() + y.height();
        return Helpers.newVector2(width, height);
    }

    @Override
    public Raylib.Vector2 variableSize() {
        return new Raylib.Vector2();
    }
}
