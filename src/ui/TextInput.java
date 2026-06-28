package src.ui;

import com.raylib.Colors;
import com.raylib.Raylib;
import src.math.RectangleI;
import src.math.Vector2I;

import java.util.function.Consumer;
import java.util.function.Predicate;

// Text input field UI element
public class TextInput implements UiInterface, LayoutInterface {
    Predicate<String> validator;
    Consumer<String> onEnter;

    String shadowText;
    RectangleI rect;
    int maxLen;
    int fontSize;
    boolean isSelected;
    String text;
    boolean entered;
    boolean valid;

    public TextInput(int maxLen, int fontSize, String shadowText) {
        var height = fontSize + 8;
        var test = "m".repeat(Math.max(maxLen, shadowText.length()));
        var width = Raylib.MeasureText(test, fontSize) + 16;
        this.rect = new RectangleI(0, 0, width, height);
        this.maxLen = maxLen;
        this.fontSize = fontSize;
        this.isSelected = false;
        this.text = "";
        this.entered = false;
        this.valid = true;
        this.shadowText = shadowText;
    }

    public TextInput(int maxLen, int fontSize) {
        var height = fontSize + 8;
        var test = "m".repeat(maxLen);
        var width = Raylib.MeasureText(test, fontSize) + 16;
        this.rect = new RectangleI(0, 0, width, height);
        this.maxLen = maxLen;
        this.fontSize = fontSize;
        this.isSelected = false;
        this.text = "";
        this.entered = false;
        this.valid = true;
        this.shadowText = "";
    }

    public void setShadowText(String txt) {
        this.shadowText = txt;
    }

    public void setValidator(Predicate<String> validator) {
        this.validator = validator;
        this.valid = validator.test(this.text);
    }

    public void setOnEnter(Consumer<String> onEnter) {
        this.onEnter = onEnter;
    }

    // Clear input field
    public void resetInput() {
        this.text = "";
        this.isSelected = false;
        this.entered = false;
        validate();
    }

    private void validate() {
        if (validator != null) {
            this.valid = validator.test(this.text);
        }
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
        // Toggle selection on click
        if (Raylib.CheckCollisionPointRec(mousePos, rect.rl())) {
            if (Raylib.IsMouseButtonPressed(Raylib.MOUSE_BUTTON_LEFT) && inputHandle.tryTakeMouse()) {
                this.isSelected = !this.isSelected;
            }
        }

        entered = false;
        // Handle escape key to deselect
        if (isSelected && inputHandle.tryTakeEsc()) {
            if (Raylib.IsKeyPressed(Raylib.KEY_ESCAPE)) {
                this.isSelected = false;
            }
        }
        // Handle text input
        if (isSelected && inputHandle.tryTakeKeyBoard()) {
            char c = (char) Raylib.GetCharPressed();
            if (c != 0 && text.length() < maxLen) {
                text += c;
                validate();
            }
            if (Raylib.IsKeyPressed(Raylib.KEY_BACKSPACE) && !text.isEmpty()) {
                text = text.substring(0, text.length() - 1);
                validate();
            }
            if (Raylib.IsKeyPressed(Raylib.KEY_ENTER) && valid) {
                this.entered = true;
                if (this.onEnter != null) {
                    onEnter.accept(this.text);
                    this.resetInput();
                }
            }
        }
    }

    @Override
    public void draw() {
        Raylib.DrawRectangleRec(rect.rl(), Colors.WHITE);
        if (text.isEmpty() && !shadowText.isEmpty()) {
            Raylib.DrawText(shadowText, rect.pos.x + 4, rect.pos.y + 4, fontSize, Colors.GRAY);
        } else {
            Raylib.DrawText(text, rect.pos.x + 4, rect.pos.y + 4, fontSize, Colors.BLACK);
        }
        var color = isSelected ? Colors.SKYBLUE : Colors.BLACK;
        if (isSelected && !valid) {
            color = Colors.RED;
        }
        Raylib.DrawRectangleLinesEx(rect.rl(), 2, color);
    }
}
