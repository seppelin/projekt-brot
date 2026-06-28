package src.scenes;

import com.raylib.Colors;
import src.edit.MapLoader;
import src.game.Map;
import src.math.Vector2I;
import src.ui.*;

import java.util.ArrayList;
import java.util.function.Consumer;

public class CreateMapScene implements SceneInterface {
    TextInput name = new TextInput(20, 32);
    Consumer<String> onSelect;
    ArrayList<String> existingMaps;

    public CreateMapScene(Consumer<String> onSelect) {
        this.name.setSelected(true);
        this.onSelect = onSelect;
    }

    @Override
    public void setup(SceneManager sceneManager) {
        existingMaps = MapLoader.getMapList();
        var layout = new AlignLayout(1, Align.Middle, new Vector2I(10, 10));
        {
            var label = new ImageUi(UiHelper.textTexture("Enter map name:", 28, Colors.BLACK));
            layout.add(label, Align.Middle);
            sceneManager.addUiElement(label);
        }
        {
            layout.add(name, Align.Middle);
            sceneManager.addUiElement(name);
        }
        sceneManager.setRootLayout(layout);
        name.setValidator(text -> !existingMaps.contains(text));
    }

    @Override
    public void update(SceneManager sceneManager, InputHandle inputHandle) {
        // Create and save new map when name is entered
        if (name.isEntered()) {
            var map = new Map(20, 20);
            MapLoader.saveMap(name.getText(), map);
            onSelect.accept(name.getText());
        }
    }

    @Override
    public void draw() {
    }

    @Override
    public SceneInterface cloneScene() {
        return new CreateMapScene(onSelect);
    }
}
