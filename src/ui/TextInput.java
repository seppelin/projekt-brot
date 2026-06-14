package src.ui;

import com.raylib.Colors;
import com.raylib.Raylib;
import src.math.RectangleI;
import src.math.Vector2I;

public class TextInput implements UiInterface, LayoutInterface {
    RectangleI rect;
    int maxLen;
    int fontSize;
    boolean isSelected;
    String text;
    boolean entered;

    public TextInput(int maxLen, int fontSize) {
        var height = fontSize + 8;
        var test = "W".repeat(maxLen);
        var width = Raylib.MeasureText(test, fontSize) + 8;
        this.rect = new RectangleI(0, 0, width, height);
        this.maxLen = maxLen;
        this.fontSize = fontSize;
        this.isSelected = false;
        this.text = "";
        this.entered = false;
    }

    public void resetInput() {
        this.text = "";
        this.isSelected = false;
        this.entered = false;
    }

    public boolean isEntered() {
        return entered;
    }

    public String getText() {
        return text;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
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
    public float extraSpaceGreed() {
        return 0;
    }

    @Override
    public void update(InputHandle inputHandle) {
        var mousePos = Raylib.GetMousePosition();
        if (Raylib.CheckCollisionPointRec(mousePos, rect.rl())) {
            if (Raylib.IsMouseButtonPressed(Raylib.MOUSE_BUTTON_LEFT) && inputHandle.tryTakeMouse()) {
                this.isSelected = !this.isSelected;
            }
        }

        entered = false;
        if (isSelected && inputHandle.tryTakeEsc()) {
            if (Raylib.IsKeyPressed(Raylib.KEY_ESCAPE)) {
                this.isSelected = false;
            }
        }
        if (isSelected && inputHandle.tryTakeKeyBoard()) {
            char c = (char) Raylib.GetCharPressed();
            if (c != 0 && text.length() < maxLen) {
                text += c;
            }
            if (Raylib.IsKeyPressed(Raylib.KEY_ENTER)) {
                this.entered = true;
            }
            if (Raylib.IsKeyPressed(Raylib.KEY_BACKSPACE) && !text.isEmpty()) {
                text = text.substring(0, text.length() - 1);
            }
        }
    }

    @Override
    public void draw() {
        Raylib.DrawRectangleRec(rect.rl(), Colors.WHITE);
        Raylib.DrawText(text, rect.pos.x + 4, rect.pos.y + 4, fontSize, Colors.BLACK);
        Raylib.DrawRectangleLinesEx(rect.rl(), 2, isSelected ? Colors.SKYBLUE : Colors.BLACK);
    }
}
