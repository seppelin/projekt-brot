package src.ui;

import src.math.RectangleI;
import src.math.Vector2I;

import java.util.ArrayList;
import java.util.List;

public class StackLayout implements LayoutInterface {
    List<LayoutInterface> items;

    public StackLayout(LayoutInterface[] items) {
        this.items = new ArrayList<LayoutInterface>();
        this.items.addAll(List.of(items));
    }

    public StackLayout() {
        this.items = new ArrayList<>();
    }

    public void add(LayoutInterface item) {
        this.items.add(item);
    }

    public void remove(LayoutInterface item) {
        this.items.remove(item);
    }

    @Override
    public void setSpace(RectangleI rect) {
        for (var i : items) {
            i.setSpaceSafe(rect);
        }
    }

    @Override
    public Vector2I minimum() {
        var min = new Vector2I(0, 0);
        for (var i : items) {
            min = i.minimum().max(min);
        }
        return min;
    }
}
