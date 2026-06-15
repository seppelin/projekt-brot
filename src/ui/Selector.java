package src.ui;

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;
import src.math.RectangleI;
import src.math.Vector2I;

// Grid selector for choosing items
public class Selector implements UiInterface, LayoutInterface {
    static Raylib.Texture outlineTexture;

    Vector2I pos;
    int itemsPerRow;
    SelItemInterface[] items;
    int hovered;
    int clicked;
    int selected;
    float scale;

    public Selector(Vector2I pos, int itemsPerRow, SelItemInterface[] items, float scale) {
        // Create outline texture once
        if (outlineTexture == null) {
            var img = Raylib.GenImageColor(16, 16, Colors.BLANK);
            Raylib.ImageDrawRectangleLines(img, Helpers.newRectangle(0, 0, 16, 16), 1, Colors.WHITE);
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

    // Change available items
    public void setItems(SelItemInterface[] items) {
        this.items = items;
        this.selected = 0;
        this.hovered = -1;
        this.clicked = -1;
    }

    public SelItemInterface getSelected() {
        return items[selected];
    }

    // Get local box for item at index
    private RectangleI getLocalBox(int i) {
        var x = i % this.itemsPerRow;
        var y = i / this.itemsPerRow;
        var size = Math.round(16 * scale);
        return new RectangleI(Math.round(x * 16 * 1.1F * scale), Math.round(y * 16 * 1.1F * scale), size, size);
    }

    // Get world box for item at index
    private RectangleI getBox(int i) {
        var rect = getLocalBox(i);
        rect.pos = rect.pos.add(pos);
        return rect;
    }

    @Override
    public void update(InputHandle inputHandle) {
        hovered = -1;
        clicked = -1;
        var mousePos = Raylib.GetMousePosition();
        for (int i = 0; i < items.length; i++) {
            if (Raylib.CheckCollisionPointRec(mousePos, getBox(i).rl())) {
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
            var rect = getBox(i).rl();
            var hoverRect = hovered == i ? UiHelper.scaleCentered(rect, 1.1f) : rect;
            // Selected item is black outline, others are gray
            var tint = this.selected == i ? Colors.BLACK : Colors.GRAY;
            UiHelper.drawTextureRect(this.items[i].getTexture(), hoverRect, Colors.WHITE);
            UiHelper.drawTextureRect(outlineTexture, hoverRect, tint);
        }
    }

    @Override
    public void setSpace(RectangleI rect) {
        this.pos = rect.pos;
    }

    @Override
    public Vector2I minimum() {
        var x = getLocalBox(itemsPerRow - 1);
        var y = getLocalBox(items.length);
        var width = x.pos.x + x.size.x;
        var height = y.pos.y + y.size.y;
        return new Vector2I(width, height);
    }

    @Override
    public float extraSpaceGreed() {
        return 0;
    }
}
