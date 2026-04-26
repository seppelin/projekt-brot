package scenes;

import com.raylib.Colors;
import com.raylib.Helpers;
import ui.Button;

public class MenuScene implements Scene {
    private Button play;
    private Button exit;

    public MenuScene() {
        play = new Button(Helpers.newRectangle(20, 20, 50, 50), "Hello", Colors.BLACK, Colors.BLANK);
    }


    @Override
    public Scene update() {
        return null;
    }

    @Override
    public void draw() {
        play.draw();
    }
}
