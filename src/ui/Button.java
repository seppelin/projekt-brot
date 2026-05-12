package src.ui;

import com.raylib.Colors;
import com.raylib.Raylib;
import src.math.RectangleI;
import src.math.Vector2I;

public class Button implements UiInterface, LayoutInterface {
    private final Raylib.Texture texture;
    private RectangleI rect;
    private boolean isHovered = false;
    private boolean isClicked = false;

    public Button(Vector2I pos, Raylib.Texture texture) {
        this.rect = new RectangleI(pos, new Vector2I(texture.width(), texture.height()));
        this.texture = texture;
    }

    public Button(Vector2I pos, String text, int textSize, Raylib.Color textColor, Raylib.Color backgroundColor) {
        int height = textSize + 8;
        int width = Raylib.MeasureText(text, textSize) + 8;

        var img = Raylib.GenImageColor(width, height, backgroundColor);
        Raylib.ImageDrawText(img, text, 4, 4, textSize, textColor);
        this.rect = new RectangleI(pos, new Vector2I(width, height));
        this.texture = Raylib.LoadTextureFromImage(img);
    }

    @Override
    public void update(InputHandle inputHandle) {
        var mousePos = Raylib.GetMousePosition();
        if (Raylib.CheckCollisionPointRec(mousePos, rect.rl())) {
            isHovered = true;
            if (Raylib.IsMouseButtonPressed(Raylib.MOUSE_BUTTON_LEFT) && inputHandle.tryTakeMouse()) {
                isClicked = true;
            }
        } else {
            isHovered = false;
            isClicked = false;
        }
    }

    @Override
    public void draw() {
        UiHelper.drawTextureHover(rect.rl(), texture, isHovered);
    }

    public boolean isClicked() {
        return isClicked;
    }

    @Override
    public void debugDraw() {
        Raylib.DrawRectangleLinesEx(rect.rl(), 1, Colors.RED);
    }

    @Override
    public void setSpace(RectangleI rect) {
        this.rect = rect;
    }

    @Override
    public Vector2I minimum() {
        return rect.size;
    }

    @Override
    public Vector2I variableSize() {
        return new Vector2I(0, 0);
    }
}
