package src.scenes;

import com.raylib.Helpers;
import com.raylib.Raylib;
import src.ui.InputHandle;

public class SaveScene implements SceneInterface {
    @Override
    public void setup(SceneManager sceneManager) {

    }

    @Override
    public void update(SceneManager sceneManager, InputHandle inputHandle) {

    }

    @Override
    public void draw() {
        Raylib.GuiButton(Helpers.newRectangle(10, 10, 200, 50), "Hello");
    }

    @Override
    public SceneInterface cloneScene() {
        return new SaveScene();
    }
}
