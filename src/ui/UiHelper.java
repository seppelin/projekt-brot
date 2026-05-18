package src.ui;

import com.raylib.Colors;
import com.raylib.Raylib;

public class UiHelper {
    public static void drawTextureHover(Raylib.Rectangle rect, Raylib.Texture texture, boolean isHovered) {
        drawTextureScale(rect, texture, 1, isHovered ? .1f : 0, Colors.WHITE);
    }

    public static void drawTextureScale(Raylib.Rectangle rect, Raylib.Texture texture, float defaultScale, float extraScale, Raylib.Color tint) {
        var pos_adjustment = extraScale / 2 / defaultScale;
        // Adjust the position of the button when getting bigger to stay centered
        // Position is top left of the texture
        Raylib.Vector2 pos = com.raylib.Helpers.newVector2(rect.x() - (pos_adjustment * rect.width()), rect.y() - (pos_adjustment * rect.height()));
        Raylib.DrawTextureEx(texture, pos, 0, defaultScale + extraScale, tint);
    }

    public static Raylib.Rectangle scaleRectCentered(Raylib.Rectangle rect, float scale) {
        var diffX = rect.width() * (scale - 1);
        var diffY = rect.height() * (scale - 1);

        rect.height(rect.height() + diffX);
        rect.width(rect.width() + diffY);
        rect.x(rect.x() - diffX / 2);
        rect.y(rect.y() - diffY / 2);

        return rect;
    }
}
