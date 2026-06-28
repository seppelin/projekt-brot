package src.scenes;

import com.raylib.Colors;
import src.leaderboard.DB;
import src.math.RectangleI;
import src.math.Vector2I;
import src.ui.*;

import java.util.ArrayList;
import java.util.function.Consumer;

public class LeaderboardScene implements SceneInterface, LayoutInterface {
    RectangleI rect;
    ArrayList<ImageUi> entries = new ArrayList<>();
    LbSelect sel;
    AlignLayout layout = new AlignLayout(1, Align.Start, new Vector2I(10, 10));

    @Override
    public void setup(SceneManager sceneManager) {
        sel = new LbSelect();
        sel.setOnChange(name -> {
            layout.clear();
            entries.clear();
            layout.add(sel, Align.Start);
            var i = 1;
            for (var e : DB.getMatchEntries(name, 10)) {
                var ui = new ImageUi(UiHelper.textTexture(i + ". Gold: " + e.goldLeft() + ", Player: " + e.playerName(), 20, Colors.BLACK));
                layout.add(ui, Align.Start);
                entries.add(ui);
                i += 1;
            }
            // Quick hack against update flicker
            if (this.rect != null) {
                layout.setSpace(this.rect);
            }
        });
        sceneManager.addUiElement(sel);
        sceneManager.setRootLayout(this);
    }

    @Override
    public void update(SceneManager sceneManager, InputHandle inputHandle) {
        for (var entry : entries) {
            entry.update(inputHandle);
        }
    }

    @Override
    public void draw() {
        for (var entry : entries) {
            entry.draw();
        }
    }

    @Override
    public SceneInterface cloneScene() {
        return new LeaderboardScene();
    }

    @Override
    public void setSpace(RectangleI rect) {
        this.rect = rect;
        this.layout.setSpace(rect);
    }

    @Override
    public Vector2I minimum() {
        return this.layout.minimum();
    }

    private static class LbSelect implements UiInterface, LayoutInterface {
        AlignLayout layout = new AlignLayout(0, Align.Middle, new Vector2I(10, 10));
        ArrayList<Button> buttons = new ArrayList<>();
        int selected = 0;
        Consumer<String> onChange;
        ArrayList<String> compMaps;

        public LbSelect() {
            compMaps = DB.getCompMaps();
            for (var name : compMaps) {
                var b = new Button(UiHelper.mapNameTexture(name, 20));
                buttons.add(b);
                layout.add(b, Align.Middle);
            }
        }

        public void setOnChange(Consumer<String> onChange) {
            this.onChange = onChange;
            onChange.accept(compMaps.get(selected));
        }

        @Override
        public void setSpace(RectangleI rect) {
            layout.setSpace(rect);
        }

        @Override
        public Vector2I minimum() {
            return layout.minimum();
        }

        @Override
        public void update(InputHandle inputHandle) {
            var i = 0;
            for (var button : buttons) {
                button.update(inputHandle);
                if (button.isClicked()) {
                    if (onChange != null) {
                        onChange.accept(compMaps.get(i));
                    }
                    selected = i;
                }
                i += 1;
            }
        }

        @Override
        public void draw() {
            for (int i = 0; i < buttons.size(); i++) {
                if (i == selected) {
                    buttons.get(i).draw();
                } else {
                    buttons.get(i).drawInactive();
                }
            }
        }

        @Override
        public float extraSpaceGreed() {
            return 0;
        }
    }
}
