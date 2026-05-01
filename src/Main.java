package src;

import src.scenes.MenuScene;
import src.scenes.SceneManager;

import static com.raylib.Raylib.*;


class Main {
    public static int SCREEN_WIDTH = 800;
    public static int SCREEN_HEIGHT = 600;

    public static void main(String[] args) {
        SetTraceLogLevel(LOG_WARNING);
        SetTargetFPS(60);
        SetConfigFlags(FLAG_WINDOW_RESIZABLE);

        InitWindow(SCREEN_WIDTH, SCREEN_HEIGHT, "Demo");

        SetExitKey(0);

        SceneManager.startGame(new MenuScene());

        CloseWindow();
    }
}
