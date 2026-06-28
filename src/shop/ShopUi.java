package src.shop;

import com.raylib.Colors;
import com.raylib.Raylib;
import src.game.Loadout;
import src.game.SkinType;
import src.math.RectangleI;
import src.math.Vector2I;
import src.ui.InputHandle;
import src.ui.LayoutInterface;
import src.ui.UiHelper;
import src.ui.UiInterface;

import java.util.Arrays;

// Shop UI grid
public class ShopUi implements UiInterface, LayoutInterface {
    static Raylib.Texture texture = Raylib.LoadTexture("resources/Shop.png");
    RectangleI rect = new RectangleI(0, 0, 1600, 900);
    ShopSlot[] slots = new ShopSlot[12];
    SkinType[] skins = {SkinType.Salami, SkinType.Veganer, SkinType.Rainer, SkinType.KarateRainer};

    public ShopUi() {
        // Create shop slots in grid layout
        for (int i = 0; i < slots.length; i++) {
            var x = i % 4;
            var y = i / 4;
            var pos = new Vector2I(200 + x * 173, 185 + y * 146);
            if (i < 4) {
                var skin = skins[i];
                int finalI = i;
                var disabled = Arrays.asList(Loadout.getSkins()).contains(skin);
                slots[i] = new ShopSlot(skin.getTexture(), skin.name(), skin.getPrice(), pos, disabled, slot -> {
                    Loadout.addSkin(skin);
                    Loadout.addGems(-skin.getPrice());
                    slots[finalI].setDisabled(true);
                });
            } else {
                slots[i] = new ShopSlot(Raylib.LoadTexture("resources/none.png"),
                        "none", 100, pos,
                        true, s -> {
                });
            }
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

    // Get mouse position relative to shop area
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
        Raylib.DrawText("Gems: " + Loadout.getGems(), this.rect.pos.x + 40, this.rect.pos.y + 800, 28, Colors.BLACK);
    }

    @Override
    public float extraSpaceGreed() {
        return 0;
    }
}
