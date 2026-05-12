package src.scenes;

// Todo: make a simple editor for the map

import com.raylib.Colors;
import com.raylib.Raylib;
import src.game.Camera;
import src.game.FieldType;
import src.game.ItemType;
import src.game.Map;
import src.math.Vector2I;
import src.ui.*;

import java.io.*;

public class EditScene implements SceneInterface {
    Selector selector;
    Camera camera;
    Map map;
    Button saveButton;
    Button loadButton;
    Button changeButton;

    public EditScene() {
        this.selector = new Selector(new Vector2I(0, 0), 6, FieldType.values(), 2);
        this.camera = new Camera(160, 160, 2);
        this.map = new Map(20, 20);
        this.saveButton = new Button(new Vector2I(10, 10), "save", 28, Colors.BLACK, Colors.BLANK);
        this.loadButton = new Button(new Vector2I(100, 10), "load", 28, Colors.BLACK, Colors.BLANK);
        this.changeButton = new Button(new Vector2I(0, 0), "change", 28, Colors.BLACK, Colors.BLANK);
    }

    @Override
    public void setup(SceneManager sceneManager) {
        sceneManager.addUiElement(saveButton);
        sceneManager.addUiElement(loadButton);
        sceneManager.addUiElement(selector);
        sceneManager.addUiElement(changeButton);

        var layout = new AlignLayout(0, Align.Start, new Vector2I(10, 10));
        sceneManager.setRootLayout(layout);
        layout.add(saveButton, Align.Start);
        layout.add(loadButton, Align.Start);
        layout.add(changeButton, Align.End);
        layout.add(selector, Align.End);
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
        if (changeButton.isClicked()) {
            this.selector.setItems(ItemType.values());
        }

        var sel = this.selector.getSelected();
        if (sel instanceof FieldType) {
            map.updateEdits(inputHandle, (FieldType) sel, this.camera);
        }

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
