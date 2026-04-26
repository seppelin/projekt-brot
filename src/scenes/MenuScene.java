package scenes;

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;
import ui.Button;

public class MenuScene extends Scene {
    private Button play;
    private Button exit;

    public MenuScene() {
        play = new Button(Helpers.newVector2(20, 20), "Play", 32,Colors.BLACK, Colors.BLANK);
        exit = new Button(Helpers.newVector2(20, 50), "Exit", 32,Colors.BLACK, Colors.BLANK);
    }

    @Override
    public Scene update() {
        if (play.update(Raylib.GetMousePosition())) {
            return new PlayScene();
        }

        // If exit button is pressed, app quits
        setQuit(exit.update(Raylib.GetMousePosition()));
        return null;
    }

    @Override
    public void draw() {
        play.draw();
        exit.draw();
    }
}
