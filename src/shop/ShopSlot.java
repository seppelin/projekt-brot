package src.shop;

import com.raylib.Colors;
import com.raylib.Raylib;
import src.game.Loadout;
import src.math.RectangleI;
import src.math.Vector2I;
import src.ui.UiHelper;

// A slot in the shop displaying an item
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

    // Update hover and click state
    public void update(Raylib.Vector2 shopMousePos) {
        if (Raylib.CheckCollisionPointRec(shopMousePos, rect.rl())) {
            isHovered = true;
            isClicked = Raylib.IsMouseButtonPressed(Raylib.MOUSE_BUTTON_LEFT);
        } else {
            isHovered = false;
            isClicked = false;
        }
    }

    // Draw shop slot with price and description
    public void draw(Vector2I shopOffset) {
        var position = this.rect.pos.add(shopOffset);
        var scaleRect = new RectangleI(position, rect.size).rl();
        
        // Scale up if hovered
        if (this.isHovered) {
            UiHelper.scaleCentered(scaleRect, 1.1f);
        }
        
        UiHelper.drawTextureRect(texture, scaleRect, Colors.WHITE);
        Raylib.DrawText(desc, position.x, position.y - 20, 16, Colors.BLACK);
        // Show price in green if affordable, red if not
        var priceColor = Loadout.getGems() >= price ? Colors.DARKGREEN : Colors.RED;
        Raylib.DrawText(price + " Gems", position.x, position.y + 100, 16, priceColor);
    }
}
