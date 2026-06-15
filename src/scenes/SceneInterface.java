package src.scenes;

import src.ui.InputHandle;

// Interface for all game scenes
public interface SceneInterface {
    // Initialize scene
    void setup(SceneManager sceneManager);

    // Update scene logic
    void update(SceneManager sceneManager, InputHandle inputHandle);

    // Draw scene
    void draw();

    // Create a copy of this scene
    SceneInterface cloneScene();
}
