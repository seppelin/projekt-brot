package src.shop;

import com.raylib.Colors;
import com.raylib.Raylib;
import src.math.RectangleI;
import src.math.Vector2I;
import src.ui.InputHandle;
import src.ui.LayoutInterface;
import src.ui.UiHelper;
import src.ui.UiInterface;

public class ShopUi implements UiInterface, LayoutInterface {
    static Raylib.Texture texture = Raylib.LoadTexture("resources/Shop.png");
    RectangleI rect = new RectangleI(0, 0, 1600, 900);
    ShopSlot[] slots = new ShopSlot[12];

    public ShopUi() {
        for (int i = 0; i < slots.length; i++) {
            var x = i % 4;
            var y = i / 4;
            slots[i] = new ShopSlot(Raylib.LoadTexture("resources/Loadout.png"),
                    "none here", 100, new Vector2I(200 + x * 173, 185 + y * 146));
        }
    }

    @Override
    public void setSpace(RectangleI rect) {
        this.rect = rect;
    }

    @Override
    public Vector2I minimum() {
        return this.rect.size;
    }

    private Raylib.Vector2 getShopMousePosition() {
        var pos = Raylib.GetMousePosition();
        pos.x(pos.x() - this.rect.pos.x);
        pos.y(pos.y() - this.rect.pos.y);
        return pos;
    }

    @Override
    public void update(InputHandle inputHandle) {
        for (ShopSlot slot : slots) {
            slot.update(getShopMousePosition());
        }
    }

    @Override
    public void draw() {
        UiHelper.drawTextureRect(texture, rect.rl(), Colors.WHITE);
        for (ShopSlot slot : slots) {
            slot.draw(this.rect.pos);
        }
        var mousePos = getShopMousePosition();
        Raylib.DrawText("MousePos: x:" + (int) mousePos.x() + " y:" + (int) mousePos.y(), 10, 10, 28, Colors.BLACK);
    }

    @Override
    public float extraSpaceGreed() {
        return 0;
    }
}
