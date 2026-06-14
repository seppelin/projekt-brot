package src.scenes;

import com.raylib.Colors;
import src.edit.MapLoader;
import src.math.Vector2I;
import src.ui.Button;
import src.ui.InputHandle;


public class MenuScene implements SceneInterface {
    Button playButton;
    Button editButton;
    Button exitButton;
    Button shopButton;
    Button newMenu;

    public MenuScene() {
        playButton = new Button(new Vector2I(20, 20), "Play", 32, Colors.BLACK, Colors.BLANK);
        editButton = new Button(new Vector2I(20, 60), "Edit", 32, Colors.BLACK, Colors.BLANK);
        exitButton = new Button(new Vector2I(20, 100), "Exit", 32, Colors.BLACK, Colors.BLANK);
        shopButton = new Button(new Vector2I(20, 140), "Shop", 32, Colors.RED, Colors.BLANK);
        newMenu = new Button(new Vector2I(20, 180), "Menu", 32, Colors.BLACK, Colors.BLANK);
    }

    @Override
    public void setup(SceneManager sceneManager) {
        sceneManager.addUiElement(playButton);
        sceneManager.addUiElement(editButton);
        sceneManager.addUiElement(exitButton);
        sceneManager.addUiElement(shopButton);
        sceneManager.addUiElement(newMenu);
    }

    @Override
    public void update(SceneManager sm, InputHandle inputHandle) {
        if (newMenu.isClicked()) {
            sm.pushScene(new EditMenuScene());
        }

        if (playButton.isClicked()) {
            sm.pushScene(new MapSelectScene((mapName ->
                    sm.changeScene(new PlayScene(MapLoader.getMap(mapName)))), false)
            );
        }

        if (editButton.isClicked()) {
            sm.pushScene(new MapSelectScene((mapName ->
                    sm.changeScene(new EditScene(MapLoader.getMap(mapName), mapName))), true));
        }

        if (exitButton.isClicked()) {
            sm.quitGame();
        }

        if (shopButton.isClicked()) {
            sm.pushScene(new ShopScene());
        }
    }

    @Override
    public void draw() {
    }

    @Override
    public SceneInterface cloneScene() {
        return new MenuScene();
    }
}
