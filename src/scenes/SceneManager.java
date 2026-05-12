package src.scenes;

import com.raylib.Colors;
import com.raylib.Raylib;
import src.ui.InputHandle;
import src.ui.LayoutElement;
import src.ui.NoLayout;
import src.ui.UiElement;

import java.util.Vector;

public class SceneManager {
    private final Vector<UiElement> uiElements;
    private LayoutElement rootLayout;
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
            // compute layout
            layout();

            // update the scene
            update();

            draw();
        }
    }

    private void layout() {
        var max = this.rootLayout.maximum();
        var rect = new Raylib.Rectangle();
        rect.width(Math.min((float) Raylib.GetScreenWidth(), max.x()));
        rect.height(Math.min((float) Raylib.GetScreenHeight(), max.y()));

        this.rootLayout.setSpace(rect);
    }

    private void update() {
        if (Raylib.IsKeyPressed(Raylib.KEY_ESCAPE)) {
            if (scene instanceof MenuScene) {
                quitGame();
            } else {
                changeScene(new MenuScene());
            }
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

    public void setRootLayout(LayoutElement rootLayout) {
        this.rootLayout = rootLayout;
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
        this.rootLayout = new NoLayout();
        scene.setup(this);
        var min = this.rootLayout.minimum();
        Raylib.SetWindowMinSize((int) min.x(), (int) min.y());
        layout();
    }
}
