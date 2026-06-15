package src.edit;

import src.game.FieldType;
import src.math.RectangleI;
import src.math.Vector2I;
import src.ui.*;

public class EditSelect implements UiInterface, LayoutInterface {
    // Current editing state (fields, buildings, or fill)
    public EditSelectState state = EditSelectState.FieldType;
    // Grid selector for items
    public Selector selector = new Selector(new Vector2I(0, 0), 6, FieldType.values(), 2);
    // Buttons to switch between modes
    Button[] buttons = {new Button("fields", 28), new Button("buildings", 28), new Button("fill", 28)};
    // Layout for buttons
    AlignLayout buttonLayout = new AlignLayout(1, Align.End, new Vector2I(0, 0));
    // Main layout
    AlignLayout layout = new AlignLayout(0, Align.Start, new Vector2I(10, 0));

    public EditSelect() {
        // Setup button layout
        for (var b : buttons) {
            this.buttonLayout.add(b, Align.Start);
        }
        // Setup main layout
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
        // Check if any button was clicked to switch state
        for (var s : EditSelectState.values()) {
            if (buttons[s.ordinal()].isClicked()) {
                this.state = s;
                this.selector.setItems(s.getItems());
            }
        }
    }

    @Override
    public void draw() {
        selector.draw();
        // Draw active button as highlighted, others as inactive
        for (var s : EditSelectState.values()) {
            var button = buttons[s.ordinal()];
            if (s == this.state) {
                button.draw();
            } else {
                button.drawInactive();
            }
        }
    }

    @Override
    public float extraSpaceGreed() {
        return 0;
    }
}
