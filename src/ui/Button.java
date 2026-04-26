package ui;

import com.raylib.Colors;
import com.raylib.Helpers;

import static com.raylib.Raylib.*;


public class Button {
    private Rectangle rect;
    private Texture texture;
    private boolean isHovered = false;

    public Button(Vector2 pos, Texture texture) {
        this.rect = Helpers.newRectangle(pos.x(), pos.y(), texture.width(), texture.height());
        this.texture = texture;
    }

    public Button(Vector2 pos, String text, int textSize, Color textColor, Color backgroundColor) {
        int height = textSize + 8;
        int width = MeasureText(text, textSize) + 8;

        var img = GenImageColor(width, height, backgroundColor);
        ImageDrawText(img, text, 4, 4, textSize, textColor);
        this.rect = Helpers.newRectangle(pos.x(), pos.y(), width, height);
        this.texture = LoadTextureFromImage(img);
    }

    /**
     * @param mousePos Screen position of the mouse
     * @return whether button is clicked
     */
    public boolean update(Vector2 mousePos) {
        if (CheckCollisionPointRec(mousePos, rect)) {
            isHovered = true;
            return IsMouseButtonPressed(MOUSE_BUTTON_LEFT);
        } else {
            isHovered = false;
            return false;
        }
    }

    public void draw() {
        float scale = 1;
        float pos_adjustment = 0;
        if (isHovered) {
            scale = 1.1F;
            pos_adjustment = (scale - 1) / 2;
        }
        // Adjust the position of the button when getting bigger to stay centered
        // Position is top left of the texture
        Vector2 pos = Helpers.newVector2(rect.x() - (pos_adjustment * rect.width()), rect.y() - (pos_adjustment * rect.height()));
        DrawTextureEx(texture, pos, 0, scale, Colors.WHITE);
    }
}
