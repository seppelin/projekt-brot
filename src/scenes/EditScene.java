package src.scenes;

// Todo: make a simple editor for the map

import com.raylib.Colors;
import com.raylib.Raylib;
import src.edit.EditSelect;
import src.game.Camera;
import src.game.FieldType;
import src.game.ItemType;
import src.game.Map;
import src.math.Vector2I;
import src.ui.*;

import java.io.*;

public class EditScene implements SceneInterface {
    Camera camera;
    Map map;
    Button saveButton;
    Button loadButton;
    TextInput mapNameInput;
    EditSelect editSelect;

    public EditScene() {
        this.editSelect = new EditSelect();
        this.camera = new Camera(160, 160, 2);
        this.map = new Map(20, 20);
        this.saveButton = new Button(new Vector2I(10, 10), "save", 28, Colors.BLACK, Colors.BLANK);
        this.loadButton = new Button(new Vector2I(100, 10), "load", 28, Colors.BLACK, Colors.BLANK);
        this.mapNameInput = new TextInput(20, 28);
    }

    private void loadMap(String name) {
        try (var in = new ObjectInputStream(new FileInputStream("resources/maps/" + name + ".mapdata"))) {
            this.map = (Map) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveMap(String name) {
        try (var out = new ObjectOutputStream(new FileOutputStream("resources/maps/" + name + ".mapdata"))) {
            out.writeObject(map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setup(SceneManager sceneManager) {
        sceneManager.addUiElement(saveButton);
        sceneManager.addUiElement(loadButton);
        sceneManager.addUiElement(editSelect);

        var align = new AlignLayout(0, Align.Start, new Vector2I(10, 10));
        align.add(saveButton, Align.Start);
        align.add(loadButton, Align.Start);
        align.add(editSelect, Align.End);

        var mapInputAlign = new AlignLayout(0, Align.Middle, new Vector2I(10, 10));
        mapInputAlign.add(mapNameInput, Align.Middle);

        var stack = new StackLayout(new AlignLayout[]{align, mapInputAlign});
        sceneManager.setRootLayout(stack);
    }

    @Override
    public void update(SceneManager sm, InputHandle inputHandle) {
        if (mapNameInput.isEntered()) {
            saveMap(mapNameInput.getText());
            mapNameInput.resetInput();
            sm.removeUiElement(mapNameInput);
        }
        if (saveButton.isClicked()) {
            mapNameInput.setSelected(true);
            sm.addUiElement(mapNameInput);
        }
        if (loadButton.isClicked()) {
            loadMap("default");
        }

        camera.handleResize();
        camera.mouseMove(inputHandle);
        camera.scrollZoom(inputHandle);
        map.update(inputHandle, camera, this::onMapFieldClick);
    }

    public void onMapFieldClick(Integer x, Integer y) {
        var sel = this.editSelect.selector.getSelected();
        switch (this.editSelect.state) {
            case FieldType -> map.getField(x, y).setType((FieldType) sel);
            case Building -> map.getField(x, y).setItem((ItemType) sel);
            case FillField -> map.batchUpdate(x, y, (FieldType) sel, null);
        }
    }

    @Override
    public void draw() {
        Raylib.BeginMode2D(camera);
        map.draw();
        Raylib.EndMode2D();
    }
}
