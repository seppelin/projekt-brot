package src.ui;

import src.math.RectangleI;
import src.math.Vector2I;

import java.util.ArrayList;
import java.util.List;

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
        if (e == this) {
            throw new IllegalArgumentException("Cannot add LayoutInterface to itself");
        }
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
        int spaceLeft = this.rect.size.get(direction) - this.padding.get(direction);
        for (var i : items) {
            var toAssign = i.iface.minimum().get(direction);
            i.assignedSpace = toAssign;
            spaceLeft -= toAssign + this.padding.get(direction);
        }

        if (spaceLeft < 0) {
            System.out.println("AlignLayout: too little space to distribute: " + spaceLeft);
            return;
        }

        float totalGreed = 0;
        for (var i : items) {
            totalGreed += i.iface.extraSpaceGreed();
        }

        if (totalGreed == 0) {
            return;
        }

        for (var i : items) {
            var share = i.iface.extraSpaceGreed() / totalGreed;
            var toTake = Math.round(share * spaceLeft);
            toTake = Math.min(toTake, spaceLeft);
            i.assignedSpace += toTake;
            spaceLeft -= toTake;
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
                var max = i.iface.extraSpaceGreed() == 0 ? i.iface.minimum() : rect.size.sub(this.padding).sub(this.padding);
                var crossAlignSize = getCrossAlignSize(max);
                var crossAlignPos = getCrossAlignPos(crossAlignSize);

                RectangleI rect;
                if (direction == 0) {
                    rect = new RectangleI(alignPos, crossAlignPos, i.assignedSpace, crossAlignSize);
                } else {
                    rect = new RectangleI(crossAlignPos, alignPos, crossAlignSize, i.assignedSpace);
                }
                rect.pos = rect.pos.add(this.rect.pos);
                i.iface.setSpace(rect);

                alignPos += i.assignedSpace;
                alignPos += padding.get(direction);
            }
        }
    }


    @Override
    public void setSpace(RectangleI rect) {
        this.rect = rect;

        distributeSpace();

        var startPos = padding.get(direction);
        var endPos = rect.size.get(direction) - getAlignSpace(Align.End);

        var minMiddle = startPos + getAlignSpace(Align.Start);
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
}
