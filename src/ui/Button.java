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

    public Button(Raylib.Texture texture) {
        this.rect = new RectangleI(0, 0, texture.width(), texture.height());
        this.texture = texture;
    }

    public Button(String text, int textSize) {
        int height = textSize + 8;
        int width = Raylib.MeasureText(text, textSize) + 8;
        width += width / 10;

        var img = Raylib.GenImageColor(width, height, Colors.BLANK);
        Raylib.ImageDrawText(img, text, 4, 4, textSize, Colors.BLACK);
        this.rect = new RectangleI(new Vector2I(0, 0), new Vector2I(width, height));
        this.texture = Raylib.LoadTextureFromImage(img);
    }

    public Button(Vector2I pos, String text, int textSize, Raylib.Color textColor, Raylib.Color backgroundColor) {
        int height = textSize + 8;
        int width = Raylib.MeasureText(text, textSize) + 8;

        var img = Raylib.GenImageColor(width, height, backgroundColor);
        Raylib.ImageDrawText(img, text, 4, 4, textSize, textColor);
        this.rect = new RectangleI(pos, new Vector2I(width, height));
        this.texture = Raylib.LoadTextureFromImage(img);
    }

    public RectangleI getRect() {
        return rect;
    }

    @Override
    public void update(InputHandle inputHandle) {
        var mousePos = Raylib.GetMousePosition();
        if (Raylib.CheckCollisionPointRec(mousePos, rect.rl())) {
            isHovered = true;
            isClicked = Raylib.IsMouseButtonPressed(Raylib.MOUSE_BUTTON_LEFT) && inputHandle.tryTakeMouse();
        } else {
            isHovered = false;
            isClicked = false;
        }
    }

    @Override
    public void draw() {
        var hoverRect = isHovered ? UiHelper.scaleCentered(rect.rl(), 1.1f) : rect.rl();
        UiHelper.drawTextureRect(texture, hoverRect, Colors.WHITE);
    }

    public void drawInactive() {
        var hoverRect = isHovered ? UiHelper.scaleCentered(rect.rl(), 1.1f) : rect.rl();
        UiHelper.drawTextureRect(texture, hoverRect, Raylib.Fade(Colors.WHITE, 0.3f));
    }

    public boolean isClicked() {
        return isClicked;
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
    public float extraSpaceGreed() {
        return 0;
    }
}
