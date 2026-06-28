package src.scenes;

import com.raylib.Colors;
import src.edit.MapLoader;
import src.leaderboard.DB;
import src.math.Vector2I;
import src.ui.*;

import java.util.ArrayList;

public class ChooseEditMap implements SceneInterface {
    ArrayList<Entry> entries = new ArrayList<>();

    Button create = new Button(UiHelper.textTexture("<create>", 32, Colors.DARKGREEN));

    @Override
    public void setup(SceneManager sceneManager) {
        // Layout and labels
        var normalLayout = new AlignLayout(0, Align.Start, new Vector2I(10, 10));
        {
            var nLabel = new ImageUi(UiHelper.textTexture("Choose map:", 28, Colors.DARKBLUE));
            normalLayout.add(nLabel, Align.Start);
            sceneManager.addUiElement(nLabel);
        }

        // Maps
        var compNames = DB.getCompMaps();
        for (var name : MapLoader.getMapList()) {
            if (!compNames.contains(name)) {
                var b = new Button(UiHelper.mapNameTexture(name, 28));
                sceneManager.addUiElement(b);
                var e = new Entry(b, name);
                entries.add(e);
                normalLayout.add(b, Align.Start);
            }
        }

        sceneManager.addUiElement(create);
        normalLayout.add(create, Align.Start);

        sceneManager.setRootLayout(normalLayout);
    }

    @Override
    public void update(SceneManager sceneManager, InputHandle inputHandle) {
        for (var entry : entries) {
            if (entry.b.isClicked()) {
                var map = MapLoader.getMap(entry.name);
                sceneManager.changeScene(new EditScene(map, entry.name));
            }
        }
        if (create.isClicked()) {
            sceneManager.pushScene(new CreateMapScene(name ->
                    sceneManager.changeScene(new EditScene(MapLoader.getMap(name), name))
            ));
        }
    }


    @Override
    public void draw() {

    }

    @Override
    public SceneInterface cloneScene() {
        return new ChooseEditMap();
    }

    private record Entry(Button b, String name) {
    }
}
