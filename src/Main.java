package src;

import src.game.Loadout;
import src.leaderboard.DB;
import src.scenes.MenuScene;
import src.scenes.SceneManager;

import static com.raylib.Raylib.*;

public class Main {

    public static void main(String[] args) {
        // Configure raylib settings
        SetTraceLogLevel(LOG_WARNING);
        SetTargetFPS(60);
        SetConfigFlags(FLAG_WINDOW_RESIZABLE);

        DB.asyncInit();


        // Initialize window
        InitWindow(1600, 900, "Demo");
        // Disable default exit key (ESC)
        SetExitKey(0);

        // Load game data and start main menu
        Loadout.init();

        SceneManager.startGame(new MenuScene());

        Loadout.save();

        // Clean up
        CloseWindow();
    }
}
