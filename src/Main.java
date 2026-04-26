import scenes.MenuScene;
import scenes.Scene;

import static com.raylib.Raylib.*;
import static com.raylib.Colors.*;
import static com.raylib.Helpers.*;

void main() {
    SetTraceLogLevel(LOG_WARNING);
    InitWindow(800, 450, "Demo");
    SetTargetFPS(60);

    Scene scene = new MenuScene();

    while (!WindowShouldClose()) {

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
