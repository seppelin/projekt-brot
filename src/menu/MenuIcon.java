package src.menu;


import com.raylib.Colors;
import com.raylib.Raylib;
import src.math.RectangleI;
import src.ui.InputHandle;
import src.ui.UiHelper;


public class MenuIcon {
    private final Raylib.Texture texture;
    private final RectangleI rect;
    private boolean active;

    public MenuIcon(RectangleI rect, Raylib.Texture texture) {
        this.texture = texture;
        this.rect = rect;
        this.active = false;
    }

    public void update(InputHandle Ih, Raylib.Vector2 playerPos) {
        var dist = Raylib.Vector2Distance(rect.middle().rl(), playerPos);
        active = dist < 50;
        if (active) {

        }
    }

    public void draw() {
        var color = active ? Colors.YELLOW : Colors.WHITE;
        UiHelper.drawTextureRect(texture, rect.rl(), color);
    }
}