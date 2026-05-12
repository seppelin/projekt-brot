package src.scenes;

import src.ui.InputHandle;

public interface SceneInterface {
    void setup(SceneManager sceneManager);

    void update(SceneManager sceneManager, InputHandle inputHandle);

    void draw();
}
