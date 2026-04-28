package src;

import src.scenes.MenuScene;
import src.scenes.Scene;

import static com.raylib.Colors.RAYWHITE;
import static com.raylib.Raylib.*;


class Main {
    public static int SCREEN_WIDTH = 800;
    public static int SCREEN_HEIGHT = 600;

    public static void main() {
        SetTraceLogLevel(LOG_WARNING);
        InitWindow(SCREEN_WIDTH, SCREEN_HEIGHT, "Demo");
        SetTargetFPS(60);
        SetExitKey(0);

        Scene scene = new MenuScene();

        while (!WindowShouldClose() && !scene.shouldQuit()) {

            // Update the scene and possible change it
            Scene sceneChange = scene.update();
            if (sceneChange != null) {
                scene = sceneChange;
            }


            // Draw the scene
            BeginDrawing();
            ClearBackground(RAYWHITE);
            scene.draw();
            EndDrawing();
        }
        CloseWindow();
    }
}
