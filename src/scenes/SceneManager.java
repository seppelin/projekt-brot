package src.scenes;

import com.raylib.Colors;
import com.raylib.Raylib;
import src.math.RectangleI;
import src.ui.InputHandle;
import src.ui.LayoutInterface;
import src.ui.NoLayout;
import src.ui.UiInterface;

import java.util.Vector;

public class SceneManager {
    private final Vector<UiInterface> uiInterfaces;
    private LayoutInterface rootLayout;
    private SceneInterface scene;
    private boolean quit;

    private SceneManager(SceneInterface scene) {
        this.scene = null;
        this.quit = false;
        this.uiInterfaces = new Vector<>();
        this.changeScene(scene);
    }

    public static void startGame(SceneInterface scene) {
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
        var rect = new RectangleI(0, 0, Raylib.GetScreenWidth(), Raylib.GetScreenHeight());
        this.rootLayout.setSpaceSafe(rect);
    }

    private void update() {
        InputHandle ih = new InputHandle();
        // Ui before the rest since on top
        for (UiInterface uiInterface : uiInterfaces) {
            uiInterface.update(ih);
        }
        scene.update(this, ih);
        if (ih.tryTakeKeyBoard() && Raylib.IsKeyPressed(Raylib.KEY_ESCAPE)) {
            if (scene instanceof MenuScene) {
                quitGame();
            } else {
                changeScene(new MenuScene());
            }
        }
    }

    private void draw() {
        Raylib.BeginDrawing();
        Raylib.ClearBackground(Colors.RAYWHITE);
        // Ui after the rest since on top
        scene.draw();
        for (UiInterface uiInterface : uiInterfaces) {
            uiInterface.draw();
        }
        Raylib.EndDrawing();
    }

    public void setRootLayout(LayoutInterface rootLayout) {
        this.rootLayout = rootLayout;
    }

    public void addUiElement(UiInterface uiInterface) {
        uiInterfaces.add(uiInterface);
    }

    public void removeUiElement(UiInterface uiInterface) {
        uiInterfaces.remove(uiInterface);
    }

    public void quitGame() {
        quit = true;
    }

    public void changeScene(SceneInterface scene) {
        this.scene = scene;
        this.uiInterfaces.clear();
        this.rootLayout = new NoLayout();
        scene.setup(this);
        var min = this.rootLayout.minimum();
        Raylib.SetWindowMinSize(min.x, min.y);

        // Simple hack to simulate the start of the loop cycle
        Raylib.BeginDrawing();
        Raylib.EndDrawing();
        layout();
        update();
    }
}
