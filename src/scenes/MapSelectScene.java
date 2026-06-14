package src.scenes;

import com.raylib.Colors;
import src.edit.MapLoader;
import src.math.Vector2I;
import src.ui.*;

import java.util.ArrayList;
import java.util.function.Consumer;

public class MapSelectScene implements SceneInterface {
    ArrayList<Button> mapButtons = new ArrayList<>();
    ArrayList<String> names;

    Button create;

    Consumer<String> onSelect;

    public MapSelectScene(Consumer<String> onSelect, boolean mapCreation) {
        this.onSelect = onSelect;
        if (mapCreation) {
            create = new Button(UiHelper.textTexture("<create>", 32, Colors.DARKGREEN));
        }
    }

    @Override
    public void setup(SceneManager sceneManager) {
        var mapLayout = new AlignLayout(0, Align.Start, new Vector2I(10, 10));
        {
            var label = new ImageUi(UiHelper.textTexture("Select map:", 28, Colors.DARKBLUE));
            mapLayout.add(label, Align.Start);
            sceneManager.addUiElement(label);
        }
        this.names = MapLoader.getMapList();
        for (var name : names) {
            var button = new Button(name, 32);
            mapButtons.add(button);
            sceneManager.addUiElement(button);
            mapLayout.add(button, Align.Start);
        }
        if (create != null) {
            mapLayout.add(create, Align.Start);
            sceneManager.addUiElement(create);
        }
        sceneManager.setRootLayout(mapLayout);
    }

    @Override
    public void update(SceneManager sceneManager, InputHandle inputHandle) {
        for (int i = 0; i < this.mapButtons.size(); i++) {
            var button = this.mapButtons.get(i);
            if (button.isClicked()) {
                onSelect.accept(this.names.get(i));
            }
        }
        if (create != null) {
            if (create.isClicked()) {
                sceneManager.pushScene(new CreateMapScene(this.onSelect));
            }
        }
    }

    @Override
    public void draw() {
    }

    @Override
    public SceneInterface cloneScene() {
        return new MapSelectScene(this.onSelect, this.create != null);
    }
}
