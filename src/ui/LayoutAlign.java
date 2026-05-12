package src.ui;

import com.raylib.Helpers;
import com.raylib.Raylib;

import java.util.Arrays;

public class LayoutAlign implements LayoutElement {
    // 0 is x direction, 1 is y direction
    int direction;
    LayoutElement[] elements;
    Align crossAlign;

    /**
     * @param direction if 0 this is an Alignment in x direction, if 1 it is y
     */
    public LayoutAlign(int direction, Align crossAlign) {
        this.direction = direction;
        this.crossAlign = crossAlign;
        elements = new LayoutElement[3];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = new NoLayout();
        }
    }

    public void set(LayoutElement e, Align align) {
        elements[align.index()] = e;
    }

    public void remove(LayoutElement e) {
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] == e) {
                remove(Align.fromIndex(i));
            }
        }
    }

    public void remove(Align align) {
        elements[align.index()] = new NoLayout();
    }

    private float minimum(int direction) {
        var minimum = 0f;
        for (int i = 0; i < 3; i++) {
            var cellMin = vectorDirection(elements[i].minimum(), direction);
            if (direction == this.direction) {
                minimum += cellMin;
            } else {
                minimum = Math.max(minimum, cellMin);
            }
        }
        return minimum;
    }

    private float vectorDirection(Raylib.Vector2 vector, int direction) {
        if (direction == 0) {
            return vector.x();
        } else {
            return vector.y();
        }
    }

    private float getCrossAlignSize(Raylib.Rectangle space, int index) {
        var e = elements[index];
        var rectNotDirSize = vectorDirection(Helpers.newVector2(space.width(), space.height()), direction ^ 1);
        return Math.min(vectorDirection(e.maximum(), direction ^ 1), rectNotDirSize);
    }

    private float getCrossAlignPos(Raylib.Rectangle space, int index) {
        float align = 0;
        if (crossAlign == Align.Middle) {
            align = 0.5f;
        } else if (crossAlign == Align.End) {
            align = 1f;
        }
        var rectNotDirSize = vectorDirection(Helpers.newVector2(space.width(), space.height()), direction ^ 1);
        var empty = rectNotDirSize - getCrossAlignSize(space, index);
        return empty * align;
    }

    private Raylib.Rectangle getSpaceRect(float alignPos, float crossPos, float alignSize, float crossSize) {
        if (direction == 0) {
            return Helpers.newRectangle(alignPos, crossPos, alignSize, crossSize);
        } else {
            return Helpers.newRectangle(crossPos, alignPos, crossSize, alignSize);
        }
    }

    @Override
    public void debugDraw() {

    }

    @Override
    public void setSpace(Raylib.Rectangle rect) {
        int rectDirSize = (int) vectorDirection(Helpers.newVector2(rect.width(), rect.height()), direction);
        int alignSpaceLeft = rectDirSize;
        int[] spaces = {0, 0, 0};
        for (int i = 0; i < 3; i++) {
            var min = (int) vectorDirection(elements[i].minimum(), direction);
            spaces[i] = min;
            alignSpaceLeft -= min;
        }

        Integer[] bySpaceIndex = {0, 1, 2};
        Arrays.sort(bySpaceIndex, (a, b) ->
                Float.compare(
                        vectorDirection(elements[a].variableSize(), direction),
                        vectorDirection(elements[b].variableSize(), direction)
                )
        );
        for (int spaceIndex = 0; spaceIndex < bySpaceIndex.length; spaceIndex++) {
            var varSpace = (int) vectorDirection(elements[bySpaceIndex[spaceIndex]].variableSize(), direction);
            var spaceForAll = Math.min((3 - spaceIndex) * varSpace, alignSpaceLeft);
            alignSpaceLeft -= spaceForAll;

            var spaceForOne = spaceForAll / (3 - spaceIndex);
            for (int dist = 2; dist >= spaceIndex; dist--) {
                spaces[bySpaceIndex[dist]] += spaceForOne;
            }
        }

        var minMiddle = spaces[0];
        var maxMiddle = rectDirSize - spaces[2];
        var middle = (rectDirSize / 2) - (spaces[1] / 2);
        middle = Math.clamp(middle, minMiddle, maxMiddle - spaces[1]);

        elements[0].setSpace(getSpaceRect(0, getCrossAlignPos(rect, 0), spaces[0], getCrossAlignSize(rect, 0)));
        elements[1].setSpace(getSpaceRect(middle, getCrossAlignPos(rect, 1), spaces[1], getCrossAlignSize(rect, 1)));
        elements[2].setSpace(getSpaceRect(maxMiddle, getCrossAlignPos(rect, 2), spaces[2], getCrossAlignSize(rect, 2)));
    }

    @Override
    public Raylib.Vector2 minimum() {
        return Helpers.newVector2(minimum(0), minimum(1));
    }

    @Override
    public Raylib.Vector2 variableSize() {
        return Helpers.newVector2(10000, 10000);
    }
}
