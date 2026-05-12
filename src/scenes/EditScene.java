package src.scenes;

// Todo: make a simple editor for the map

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;
import src.game.Camera;
import src.game.FieldType;
import src.game.Map;
import src.ui.*;

import java.io.*;

public class EditScene implements Scene {
    Selector selector;
    Camera camera;
    Map map;
    Button saveButton;
    Button loadButton;

    public EditScene() {
        this.selector = new Selector(Helpers.newVector2(500, 10), 6, FieldType.values(), 2);
        this.camera = new Camera(160, 160, 2);
        this.map = new Map(20, 20);
        this.saveButton = new Button(Helpers.newVector2(10, 10), "save", 28, Colors.BLACK, Colors.BLANK);
        this.loadButton = new Button(Helpers.newVector2(100, 10), "load", 28, Colors.BLACK, Colors.BLANK);
    }

    @Override
    public void setup(SceneManager sceneManager) {
        sceneManager.addUiElement(saveButton);
        sceneManager.addUiElement(loadButton);
        sceneManager.addUiElement(selector);
        var layout = new LayoutAlign(0, Align.Start);
        sceneManager.setRootLayout(layout);
        layout.set(saveButton, Align.Start);
        layout.set(loadButton, Align.Middle);
        layout.set(selector, Align.End);
    }

    @Override
    public void update(SceneManager sm, InputHandle inputHandle) {
        if (saveButton.isClicked()) {
            try (var out = new ObjectOutputStream(new FileOutputStream("map.savefile"))) {
                out.writeObject(map);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (loadButton.isClicked()) {
            try (var in = new ObjectInputStream(new FileInputStream("map.savefile"))) {
                this.map = (Map) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        var fieldID = this.selector.getSelectedID();
        map.updateEdits(inputHandle, FieldType.getById(fieldID), this.camera);
        camera.handleResize();
        camera.mouseMove(inputHandle);
        camera.scrollZoom(inputHandle);
    }

    @Override
    public void draw() {
        Raylib.BeginMode2D(camera);
        map.draw();
        Raylib.EndMode2D();
    }
}
