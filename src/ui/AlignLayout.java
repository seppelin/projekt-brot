package src.ui;

import src.math.RectangleI;
import src.math.Vector2I;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class AlignLayout implements LayoutInterface {
    // 0 is x direction, 1 is y direction
    RectangleI rect;
    int direction;
    List<AlignItem> items;
    Align crossAlign;
    Vector2I padding;

    /**
     * @param direction if 0 this is an Alignment in x direction, if 1 it is y
     */
    public AlignLayout(int direction, Align crossAlign, Vector2I padding) {
        this.padding = padding;
        this.direction = direction;
        this.crossAlign = crossAlign;
        this.items = new ArrayList<>();
    }

    public void add(LayoutInterface e, Align align) {
        items.add(new AlignItem(e, align));
    }

    public void remove(LayoutInterface e) {
        items.removeIf(alignItem -> alignItem.iface == e);
    }

    private int getAlignSpace(Align align) {
        var size = 0;
        for (var i : items) {
            if (i.align == align) {
                size += padding.get(direction);
                size += i.assignedSpace;
            }
        }
        return size;
    }

    private void distributeSpace() {
        int spaceLeft = this.rect.size.get(direction);
        for (var i : items) {
            var toAssign = i.iface.minimum().get(direction);
            i.assignedSpace = toAssign;
            spaceLeft -= toAssign;
        }

        if (spaceLeft < 0) {
            System.out.println("AlignLayout: too little space to distribute: " + spaceLeft);
            return;
        }

        var mappedByVarSize = new ArrayList<>(IntStream.range(0, items.size()).boxed().toList());
        mappedByVarSize.sort((a, b) -> Integer.compare(
                items.get(a).iface.minimum().get(direction),
                items.get(b).iface.minimum().get(direction)
        ));

        for (int mapIndex = 0; mapIndex < mappedByVarSize.size(); mapIndex++) {
            var distributerCount = items.size() - mapIndex;

            var item = items.get(mappedByVarSize.get(mapIndex));
            var varSpace = item.iface.variableSize().get(direction);

            var totalSpace = Math.min(distributerCount * varSpace, spaceLeft);
            var spaceForEach = totalSpace / distributerCount;
            for (int distributeIndex = items.size() - 1; distributeIndex >= mapIndex; distributeIndex--) {
                items.get(distributeIndex).assignedSpace += spaceForEach;
                spaceLeft -= spaceForEach;
            }
        }
    }

    private int getCrossAlignSize(Vector2I max) {
        var rectNotDirSize = rect.size.get(direction ^ 1);
        return Math.min(max.get(direction ^ 1), rectNotDirSize);
    }

    private int getCrossAlignPos(int crossAlignSize) {
        var crossAlignPadding = padding.get(direction ^ 1);
        float align = 0;
        if (crossAlign == Align.Middle) {
            align = 0.5f;
        } else if (crossAlign == Align.End) {
            align = 1f;
        }
        int rectNotDirSize = rect.size.get(direction ^ 1);
        int empty = rectNotDirSize - (crossAlignSize + crossAlignPadding * 2);
        return Math.round(empty * align) + crossAlignPadding;
    }

    private void setSpaceAlign(int alignPos, Align align) {
        for (var i : items) {
            if (i.align == align) {
                var crossAlignSize = getCrossAlignSize(i.iface.maximum());
                var crossAlignPos = getCrossAlignPos(crossAlignSize);
                if (direction == 0) {
                    i.iface.setSpace(new RectangleI(alignPos, crossAlignPos, i.assignedSpace, crossAlignSize));
                } else {
                    i.iface.setSpace(new RectangleI(crossAlignPos, alignPos, crossAlignSize, i.assignedSpace));
                }
                alignPos += i.assignedSpace;
                alignPos += padding.get(direction);
            }
        }
    }

    @Override
    public void debugDraw() {

    }

    @Override
    public void setSpace(RectangleI rect) {
        this.rect = rect;

        distributeSpace();

        var startPos = padding.get(direction);
        var endPos = rect.size.get(direction) - getAlignSpace(Align.End);

        var minMiddle = getAlignSpace(Align.Start);
        var maxMiddle = endPos - getAlignSpace(Align.Middle);
        var optimalMiddle = (rect.size.get(direction) / 2) - (getAlignSpace(Align.Middle) / 2);
        var middlePos = Math.clamp(optimalMiddle, minMiddle, maxMiddle);

        setSpaceAlign(startPos, Align.Start);
        setSpaceAlign(middlePos, Align.Middle);
        setSpaceAlign(endPos, Align.End);
    }

    @Override
    public Vector2I minimum() {
        var min = new Vector2I(0, 0);
        min.set(direction, padding.get(direction));
        for (var i : items) {
            var itemMin = i.iface.minimum();
            min.set(direction, min.get(direction) + itemMin.get(direction) + padding.get(direction));
            min.set(direction ^ 1, Math.max(min.get(direction ^ 1), itemMin.get(direction ^ 1) + 2 * padding.get(direction ^ 1)));
        }
        return min;
    }

    @Override
    public Vector2I variableSize() {
        return new Vector2I(10000, 10000);
    }
}
