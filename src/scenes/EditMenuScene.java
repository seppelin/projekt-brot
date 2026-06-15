package src.scenes;

import src.menu.Menu;
import src.ui.InputHandle;

// Scene showing the lobby/menu area
public class EditMenuScene implements SceneInterface {
    Menu menu = new Menu();

    @Override
    public void setup(SceneManager sceneManager) {
    }

    @Override
    public void update(SceneManager sceneManager, InputHandle inputHandle) {
        menu.update(inputHandle);
    }

    @Override
    public void draw() {
        menu.draw();
    }

    @Override
    public SceneInterface cloneScene() {
        return new EditMenuScene();
    }
}
