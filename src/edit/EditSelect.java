package src.edit;

import com.raylib.Colors;
import com.raylib.Raylib;
import src.game.FieldType;
import src.math.RectangleI;
import src.math.Vector2I;
import src.ui.*;

public class EditSelect implements UiInterface, LayoutInterface {
    public EditSelectState state = EditSelectState.FieldType;
    public Selector selector = new Selector(new Vector2I(0, 0), 6, FieldType.values(), 2);
    Button[] buttons = {new Button("fields", 28), new Button("buildings", 28), new Button("fill", 28)};
    AlignLayout buttonLayout = new AlignLayout(1, Align.End, new Vector2I(0, 0));
    AlignLayout layout = new AlignLayout(0, Align.Start, new Vector2I(10, 0));

    public EditSelect() {
        for (var b : buttons) {
            this.buttonLayout.add(b, Align.Start);
        }
        this.layout.add(buttonLayout, Align.End);
        this.layout.add(selector, Align.End);
    }

    @Override
    public void setSpace(RectangleI rect) {
        this.layout.setSpace(rect);
    }

    @Override
    public Vector2I minimum() {
        return this.layout.minimum();
    }

    @Override
    public void update(InputHandle inputHandle) {
        selector.update(inputHandle);
        for (var button : buttons) {
            button.update(inputHandle);
        }
        for (var state : EditSelectState.values()) {
            if (buttons[state.ordinal()].isClicked()) {
                this.state = state;
                this.selector.setItems(state.getItems());
            }
        }
    }

    @Override
    public void draw() {
        selector.draw();
        for (var state : EditSelectState.values()) {
            var button = buttons[state.ordinal()];
            if (state == this.state) {
                Raylib.DrawRectangleRoundedLines(button.getRect().rl(), 0.5f, 2, Colors.BLACK);
            }
            button.draw();
        }
    }

    @Override
    public float extraSpaceGreed() {
        return 0;
    }
}
