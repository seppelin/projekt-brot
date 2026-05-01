package src.scenes;

import com.raylib.Helpers;
import com.raylib.Raylib;

public class SaveScene implements Scene {
    @Override
    public void setup(SceneManager sceneManager) {

    }

    @Override
    public void update(SceneManager sceneManager) {

    }

    @Override
    public void draw() {
        Raylib.GuiButton(Helpers.newRectangle(10, 10, 200, 50), "Hello");
    }
}
