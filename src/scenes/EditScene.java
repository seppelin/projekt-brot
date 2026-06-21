package src.scenes;

import com.raylib.Colors;
import com.raylib.Raylib;
import src.edit.EditSelect;
import src.edit.MapLoader;
import src.game.BuildingType;
import src.game.Camera;
import src.game.FieldType;
import src.game.Map;
import src.math.Vector2I;
import src.ui.*;

// Map editor scene
public class EditScene implements SceneInterface {
    Camera camera;
    Map map;
    String mapName;
    Button saveButton;
    Button settings;
    EditSelect editSelect;

    public EditScene(Map map, String mapName) {
        this.map = map;
        this.mapName = mapName;
        this.editSelect = new EditSelect();
        this.camera = new Camera(160, 160, 2);
        this.saveButton = new Button("save", 28);
        this.settings = new Button("settings", 28);
    }

    @Override
    public void setup(SceneManager sceneManager) {
        var nameImg = new ImageUi(UiHelper.textTexture("Map(" + this.mapName + "):", 32, Colors.DARKBLUE));

        sceneManager.addUiElement(saveButton);
        sceneManager.addUiElement(editSelect);
        sceneManager.addUiElement(nameImg);
        sceneManager.addUiElement(settings);

        var align = new AlignLayout(0, Align.Start, new Vector2I(10, 10));
        align.add(nameImg, Align.Start);
        align.add(saveButton, Align.Start);
        align.add(settings, Align.Start);
        align.add(editSelect, Align.End);

        sceneManager.setRootLayout(align);
    }

    @Override
    public void update(SceneManager sm, InputHandle inputHandle) {
        // Save map when button clicked
        if (saveButton.isClicked()) {
            MapLoader.saveMap(this.mapName, map);
        }

        camera.handleResize();
        camera.mouseMove(inputHandle);
        camera.scrollZoom(inputHandle);
        map.update(inputHandle, camera, this::onMapFieldClick);
    }

    // Handle field click - apply selected tool
    public void onMapFieldClick(Integer x, Integer y) {
        var sel = this.editSelect.selector.getSelected();
        switch (this.editSelect.state) {
            case FieldType -> map.getField(x, y).type = (FieldType) sel;
            case Building -> map.setBuilding(x, y, (BuildingType) sel);
            case FillField -> map.batchUpdate(x, y, (FieldType) sel, null);
        }
    }

    @Override
    public void draw() {
        Raylib.BeginMode2D(camera);
        map.draw();
        Raylib.EndMode2D();
    }

    @Override
    public SceneInterface cloneScene() {
        return new EditScene(this.map, this.mapName);
    }
}
