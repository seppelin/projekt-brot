package src;

import src.game.Loadout;
import src.scenes.EditMenuScene;
import src.scenes.SceneManager;

import static com.raylib.Raylib.*;

public class Main {

    public static void main(String[] args) {
        // Configure raylib settings
        SetTraceLogLevel(LOG_WARNING);
        SetTargetFPS(60);
        SetConfigFlags(FLAG_WINDOW_RESIZABLE);

        // Initialize window
        InitWindow(1600, 900, "Demo");

        // Disable default exit key (ESC)
        SetExitKey(0);

        // Load game data and start main menu
        Loadout.init();
        SceneManager.startGame(new EditMenuScene());

        // Clean up
        CloseWindow();
    }
}
