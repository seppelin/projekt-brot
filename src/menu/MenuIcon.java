package src.menu;

import com.raylib.Colors;
import com.raylib.Raylib;
import src.math.RectangleI;
import src.ui.InputHandle;
import src.ui.UiHelper;

// Interactive icon in the menu
public class MenuIcon {
    private final Raylib.Texture texture;
    private final RectangleI rect;
    private boolean active;
    private boolean interacted;

    public MenuIcon(RectangleI rect, Raylib.Texture texture) {
        this.texture = texture;
        this.rect = rect;
        this.active = false;
    }

    public boolean isInteracted() {
        return interacted;
    }

    // Update icon state based on player proximity
    public void update(InputHandle ih, Raylib.Vector2 playerPos) {
        var dist = Raylib.Vector2Distance(rect.middle().rl(), playerPos);
        active = dist < 50;
        if (active && ih.tryTakeKeyBoard()) interacted = Raylib.IsKeyPressed(Raylib.KEY_E);
    }

    // Draw icon with highlight if active
    public void draw() {
        var color = active ? Colors.YELLOW : Colors.WHITE;
        UiHelper.drawTextureRect(texture, rect.rl(), color);
        if (active) {
            Raylib.DrawText("Press E to enter", rect.pos.x, rect.pos.y - 10, 6, Colors.BLACK);
        }
    }
}
