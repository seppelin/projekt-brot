package ui;

import com.raylib.Colors;
import com.raylib.Helpers;

import static com.raylib.Raylib.*;


public class Button {
    private Rectangle rect;
    private Texture texture;

    public Button(Rectangle rect, Texture texture) {
        this.rect = rect;
        this.texture = texture;
    }

    /**
     * @param rect the size and position of the button
     * @param text the text of the button
     */
    public Button(Rectangle rect, String text, Color textColor, Color backgroundColor) {
        var img = GenImageColor((int) rect.width(), (int) rect.height(), backgroundColor);
        ImageDrawText(img, text, 0, 0, 16, textColor);
        this.rect = rect;
        this.texture = LoadTextureFromImage(img);
    }

    /**
     * @param mousePos Screen position of the mouse
     * @return whether button is clicked
     */
    public boolean update(Vector2 mousePos) {
        if (CheckCollisionPointRec(mousePos, rect)) {
            return IsMouseButtonPressed(MOUSE_BUTTON_LEFT);
        }
        return false;
    }

    public void draw() {
        DrawTextureV(texture, Helpers.newVector2(rect.x(), rect.y()), Colors.WHITE);
    }
}
