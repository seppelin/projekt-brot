package src.scenes;

import com.raylib.Colors;
import com.raylib.Raylib;
import src.ui.InputHandle;
import src.ui.UiElement;

import java.util.Vector;

public class SceneManager {
    private final Vector<UiElement> uiElements;
    private Scene scene;
    private boolean quit;

    private SceneManager(Scene scene) {
        this.scene = null;
        this.quit = false;
        this.uiElements = new Vector<>();
        this.changeScene(scene);
    }

    public static void startGame(Scene scene) {
        SceneManager sceneManager = new SceneManager(scene);
        sceneManager.run();
    }

    private void run() {
        while (!quit && !Raylib.WindowShouldClose()) {
            // update the scene
            update();

            draw();
        }
    }

    private void update() {
        if (Raylib.IsKeyPressed(Raylib.KEY_ESCAPE)) {
            changeScene(new MenuScene());
            return;
        }
        InputHandle ih = new InputHandle();
        // Ui before the rest since on top
        for (UiElement uiElement : uiElements) {
            uiElement.update(ih);
        }
        scene.update(this, ih);
    }

    private void draw() {
        Raylib.BeginDrawing();
        Raylib.ClearBackground(Colors.RAYWHITE);
        // Ui after the rest since on top
        scene.draw();
        for (UiElement uiElement : uiElements) {
            uiElement.draw();
        }
        Raylib.EndDrawing();
    }

    public void addUiElement(UiElement uiElement) {
        uiElements.add(uiElement);
    }

    public void removeUiElement(UiElement uiElement) {
        uiElements.remove(uiElement);
    }

    public void quitGame() {
        quit = true;
    }

    public void changeScene(Scene scene) {
        this.scene = scene;
        this.uiElements.clear();
        scene.setup(this);
    }
}
