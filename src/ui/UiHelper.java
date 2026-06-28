package src.ui;

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;

// Helper functions for UI rendering
public class UiHelper {
    public static Raylib.Texture mapNameTexture(String name, int textSize) {
        int hash = name.hashCode();

        // Mask the hash to extract individual R, G, B bytes (0-255)
        int r = (hash & 0xFF0000) >> 16;
        int g = (hash & 0x00FF00) >> 8;
        int b = (hash & 0x0000FF);
        return textTexture(name, textSize, Helpers.newColor(r / 2, g / 2, b / 2, 255));
    }

    // Draw texture with scaling and tinting
    public static void drawTextureRect(Raylib.Texture texture, Raylib.Rectangle rect, Raylib.Color tint) {
        Raylib.DrawTexturePro(texture, Helpers.newRectangle(0, 0, texture.width(), texture.height()), rect, new Raylib.Vector2(), 0f, tint);
    }

    // Scale rectangle from center
    public static Raylib.Rectangle scaleCentered(Raylib.Rectangle rect, float scaleFactor) {
        var diffX = rect.width() * (scaleFactor - 1f);
        var diffY = rect.height() * (scaleFactor - 1f);

        rect.width(rect.width() + diffX);
        rect.height(rect.height() + diffY);
        rect.x(rect.x() - diffX / 2);
        rect.y(rect.y() - diffY / 2);
        return rect;
    }

    // Create image from text
    public static Raylib.Image textImage(String text, int textSize, Raylib.Color color) {
        int height = textSize + 8;
        int width = Raylib.MeasureText(text, textSize) + 8;
        width += width / 10;

        var img = Raylib.GenImageColor(width, height, Colors.BLANK);
        Raylib.ImageDrawText(img, text, 4, 4, textSize, color);
        return img;
    }

    // Create texture from text
    public static Raylib.Texture textTexture(String text, int textSize, Raylib.Color color) {
        return Raylib.LoadTextureFromImage(textImage(text, textSize, color));
    }

    public static Raylib.Texture textTextureEx(String text, int textSize, Raylib.Color color, Raylib.Color background, int outlineSize) {
        int height = textSize + 8;
        int width = Raylib.MeasureText(text, textSize) + 8;
        width += width / 10;

        var img = Raylib.GenImageColor(width, height, background);
        Raylib.ImageDrawText(img, text, 4, 4, textSize, color);
        Raylib.ImageDrawRectangleLines(img, Helpers.newRectangle(0, 0, width, height), outlineSize, color);
        return Raylib.LoadTextureFromImage(img);
    }
}
