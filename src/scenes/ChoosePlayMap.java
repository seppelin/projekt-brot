package src.scenes;

import com.raylib.Colors;
import src.edit.MapLoader;
import src.game.GameInfo;
import src.leaderboard.DB;
import src.math.Vector2I;
import src.ui.*;

import java.util.ArrayList;

public class ChoosePlayMap implements SceneInterface {
    ArrayList<Entry> entries = new ArrayList<>();

    @Override
    public void setup(SceneManager sceneManager) {
        // Layout and labels
        var normalLayout = new AlignLayout(0, Align.Start, new Vector2I(10, 10));
        var rankedLayout = new AlignLayout(0, Align.Start, new Vector2I(10, 10));
        {
            var rLabel = new ImageUi(UiHelper.textTexture("Ranked maps:", 28, Colors.DARKPURPLE));
            rankedLayout.add(rLabel, Align.Start);
            sceneManager.addUiElement(rLabel);
        }
        {
            var nLabel = new ImageUi(UiHelper.textTexture("Normal maps:", 28, Colors.DARKBLUE));
            normalLayout.add(nLabel, Align.Start);
            sceneManager.addUiElement(nLabel);
        }

        // Maps
        var compNames = DB.getCompMaps();
        for (var name : MapLoader.getMapList()) {
            var b = new Button(UiHelper.mapNameTexture(name, 28));
            sceneManager.addUiElement(b);
            var e = new Entry(b, name, compNames.contains(name));
            entries.add(e);
            if (compNames.contains(name)) {
                rankedLayout.add(b, Align.Start);
            } else {
                normalLayout.add(b, Align.Start);
            }
        }

        // Bring it together
        var mapLayout = new AlignLayout(1, Align.Start, new Vector2I(0, 0));
        mapLayout.spaceGreed = 0;
        mapLayout.add(normalLayout, Align.Start);
        mapLayout.add(rankedLayout, Align.Start);
        sceneManager.setRootLayout(mapLayout);
    }

    @Override
    public void update(SceneManager sceneManager, InputHandle inputHandle) {
        for (var entry : entries) {
            if (entry.b.isClicked()) {
                var map = MapLoader.getMap(entry.name);
                sceneManager.changeScene(new PreGameScene(map, new GameInfo(map, entry.name, entry.comp)));
            }
        }
    }

    @Override
    public void draw() {

    }

    @Override
    public SceneInterface cloneScene() {
        return new ChoosePlayMap();
    }

    private record Entry(Button b, String name, boolean comp) {
    }
}
