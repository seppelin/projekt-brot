package src.scenes;

import com.raylib.Colors;
import com.raylib.Raylib;
import src.ui.InputHandle;

public class Shop implements SceneInterface {
    public Shop() {

    }

    @Override
    public void setup(SceneManager sceneManager) {

    }

    @Override
    public void update(SceneManager sceneManager, InputHandle inputHandle) {

    }

    @Override
    public void draw() {
        Raylib.DrawRectangle(400, 60, 10, 10, Colors.BLACK);
    }
}