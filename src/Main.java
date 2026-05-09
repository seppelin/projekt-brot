package src;

import src.scenes.MenuScene;
import src.scenes.SceneManager;

import static com.raylib.Raylib.*;

public class Main {

    public static void main(String[] args) {
        SetTraceLogLevel(LOG_WARNING);
        SetTargetFPS(60);
        SetConfigFlags(FLAG_WINDOW_RESIZABLE);

        InitWindow(1600, 900, "Demo");

        SetExitKey(0);

        SceneManager.startGame(new MenuScene());

        CloseWindow();
    }
}
