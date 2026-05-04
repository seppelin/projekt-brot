package src.scenes;

// Todo: make a simple editor for the map

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;
import src.game.Camera;
import src.game.FieldType;
import src.game.Map;
import src.ui.Button;
import src.ui.Selector;

public class EditScene implements Scene {
    Selector selector;
    Camera camera;
    Map map;
    Button saveButton;

    public EditScene() {
        this.selector = new Selector(Helpers.newVector2(500, 10), 2, FieldType.values(), 2);
        this.camera = new Camera(160, 160, 2);
        this.map = new Map(20, 20);
        this.saveButton = new Button(Helpers.newVector2(10, 10), "save", 28, Colors.BLACK, Colors.BLANK);
    }

    @Override
    public void setup(SceneManager sceneManager) {
        sceneManager.addUiElement(saveButton);
        sceneManager.addUiElement(selector);
    }

    @Override
    public void update(SceneManager sm) {
        if (saveButton.isClicked()) {
            sm.changeScene(new SaveScene());
        }
        map.update();
        camera.handleResize();
        camera.mouseMove();
        camera.scrollZoom();
    }

    @Override
    public void draw() {
        Raylib.BeginMode2D(camera);
        map.draw();
        Raylib.EndMode2D();
    }
}
