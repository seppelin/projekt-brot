package src.scenes;

// Todo: make a simple editor for the map

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;
import src.game.Camera;
import src.game.Map;
import src.ui.Button;

public class EditScene implements Scene {
    Camera camera;
    Map map;
    Button saveButton;

    public EditScene() {
        this.camera = new Camera(160, 160, 2);
        this.map = new Map(20, 20);
        this.saveButton = new Button(Helpers.newVector2(10, 10), "save", 28, Colors.BLACK, Colors.BLANK);
    }

    @Override
    public void setup(SceneManager sceneManager) {
        sceneManager.addUiElement(saveButton);
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
