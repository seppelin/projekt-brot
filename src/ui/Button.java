package src.ui;

import com.raylib.Helpers;
import com.raylib.Raylib;

import static com.raylib.Raylib.*;

public class Button implements UiElement {
    private final Rectangle rect;
    private final Texture texture;
    private boolean isHovered = false;
    private boolean isClicked = false;

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

    @Override
    public void update(InputHandle inputHandle) {
        var mousePos = Raylib.GetMousePosition();
        if (CheckCollisionPointRec(mousePos, rect)) {
            isHovered = true;
            if (IsMouseButtonPressed(MOUSE_BUTTON_LEFT) && inputHandle.tryTakeMouse()) {
                isClicked = true;
            }
        } else {
            isHovered = false;
            isClicked = false;
        }
    }

    @Override
    public void draw() {
        UiHelper.drawTextureHover(rect, texture, isHovered);
    }

    public boolean isClicked() {
        return isClicked;
    }
}
