package src.shop;

import com.raylib.Colors;
import com.raylib.Raylib;
import src.game.Loadout;
import src.math.RectangleI;
import src.math.Vector2I;
import src.ui.UiHelper;

public class ShopSlot {
    Raylib.Texture texture;
    String desc;
    int price;
    RectangleI rect;
    boolean isClicked;
    boolean isHovered;

    public ShopSlot(Raylib.Texture texture, String desc, int price, Vector2I shopPos) {
        this.texture = texture;
        this.desc = desc;
        this.price = price;
        this.rect = new RectangleI(shopPos, new Vector2I(108, 91));
    }

    public void update(Raylib.Vector2 shopMousePos) {
        if (Raylib.CheckCollisionPointRec(shopMousePos, rect.rl())) {
            isHovered = true;
            isClicked = Raylib.IsMouseButtonPressed(Raylib.MOUSE_BUTTON_LEFT);
        } else {
            isHovered = false;
            isClicked = false;
        }
    }

    public void draw(Vector2I shopOffset) {
        var position = this.rect.pos.add(shopOffset);

        var scaleRect = new RectangleI(position, rect.size).rl();
        if (this.isHovered) {
            UiHelper.scaleCentered(scaleRect, 1.1f);
        }
        UiHelper.drawTextureRect(texture, scaleRect, Colors.WHITE);
        Raylib.DrawText(desc, position.x, position.y - 20, 16, Colors.BLACK);
        var priceColor = Loadout.getGems() >= price ? Colors.DARKGREEN : Colors.RED;
        Raylib.DrawText(price + " Gems", position.x, position.y + 100, 16, priceColor);
    }
}
