package src.ui;

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;
import src.math.RectangleI;
import src.math.Vector2I;

public class BuySelector implements LayoutInterface {
    static Raylib.Texture outlineTexture;
    Vector2I padding;
    Vector2I pos;
    Vector2I size;
    String currencyName;
    BuyItemInterface[] items;
    int itemsPerRow;
    int hovered;
    int clicked;
    int selected;

    public BuySelector(Vector2I pos, Vector2I size, Vector2I padding, int itemsPerRow, String currencyName, BuyItemInterface[] items) {
        // Create outline texture once
        if (outlineTexture == null) {
            var img = Raylib.GenImageColor(16, 16, Colors.BLANK);
            Raylib.ImageDrawRectangleLines(img, Helpers.newRectangle(0, 0, 16, 16), 1, Colors.WHITE);
            outlineTexture = Raylib.LoadTextureFromImage(img);
        }
        this.padding = padding;
        this.pos = pos;
        this.size = size;
        this.currencyName = currencyName;
        this.items = items;
        this.itemsPerRow = itemsPerRow;
        this.hovered = -1;
        this.selected = -1;
        this.clicked = -1;
    }

    // Change available items
    public void setItems(BuyItemInterface[] items) {
        this.items = items;
        this.selected = -1;
        this.hovered = -1;
        this.clicked = -1;
    }

    public BuyItemInterface getSelected() {
        if (selected == -1) {
            return null;
        }
        return items[selected];
    }

    public BuyItemInterface getClicked() {
        if (clicked == -1) {
            return null;
        }
        return items[clicked];
    }

    // Get local box for item at index
    private RectangleI getLocalBox(int i) {
        var x = i % this.itemsPerRow;
        var y = i / this.itemsPerRow;
        return new RectangleI(x * (size.x + padding.x), y * (size.y + padding.y), size.x, size.y);
    }

    // Get world box for item at index
    private RectangleI getBox(int i) {
        var rect = getLocalBox(i);
        rect.pos = rect.pos.add(pos);
        return rect;
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

    public void update(InputHandle inputHandle, int money) {
        hovered = -1;
        clicked = -1;
        var sel = getSelected();
        if (sel != null) {
            if (sel.getPrice() > money) {
                this.selected = -1;
            }
        }
        var mousePos = Raylib.GetMousePosition();
        for (int i = 0; i < items.length; i++) {
            if (Raylib.CheckCollisionPointRec(mousePos, getBox(i).rl())) {
                if (money >= items[i].getPrice()) {
                    this.hovered = i;
                    if (Raylib.IsMouseButtonPressed(Raylib.MOUSE_BUTTON_LEFT) && inputHandle.tryTakeMouse()) {
                        this.clicked = i;
                        this.selected = i;
                    }
                }
            }
        }
    }

    public void draw(int money) {
        for (int i = 0; i < items.length; i++) {
            var rect = getBox(i).rl();
            var hoverRect = hovered == i ? UiHelper.scaleCentered(rect, 1.1f) : rect;
            var outlineTint = this.selected == i ? Colors.BLACK : Colors.GRAY;
            var textureTint = items[i].getPrice() > money ? Colors.GRAY : Colors.WHITE;
            UiHelper.drawTextureRect(this.items[i].getTexture(), hoverRect, textureTint);
            UiHelper.drawTextureRect(outlineTexture, hoverRect, outlineTint);

            var priceColor = items[i].getPrice() > money ? Colors.RED : Colors.DARKGREEN;
            var priceText = items[i].getPrice() + " " + currencyName;
            Raylib.DrawText(priceText, (int) rect.x(), (int) (rect.y() + rect.height() + 4), 12, priceColor);
        }
    }

    @Override
    public float extraSpaceGreed() {
        return 0;
    }
}
