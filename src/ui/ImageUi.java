package src.ui;

import com.raylib.Colors;
import com.raylib.Raylib;
import src.math.RectangleI;
import src.math.Vector2I;

public class ImageUi implements UiInterface, LayoutInterface {
    RectangleI rect;
    Raylib.Texture texture;

    public ImageUi(Raylib.Texture texture) {
        this.texture = texture;
        this.rect = new RectangleI(0, 0, texture.width(), texture.height());
    }

    public ImageUi(Raylib.Texture texture, RectangleI rect) {
        this.texture = texture;
        this.rect = rect;
    }

    @Override
    public void setSpace(RectangleI rect) {
        this.rect = rect;
    }

    @Override
    public Vector2I minimum() {
        return this.rect.size;
    }

    @Override
    public void update(InputHandle inputHandle) {
    }

    @Override
    public void draw() {
        UiHelper.drawTextureRect(texture, rect.rl(), Colors.WHITE);
    }

    @Override
    public float extraSpaceGreed() {
        return 0;
    }
}
