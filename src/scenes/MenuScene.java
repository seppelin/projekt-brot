package src.scenes;

import com.raylib.Colors;
import src.math.Vector2I;
import src.ui.Button;
import src.ui.InputHandle;

public class MenuScene implements SceneInterface {
    Button playButton;
    Button editButton;
    Button exitButton;

    public MenuScene() {
        playButton = new Button(new Vector2I(20, 20), "Play", 32, Colors.BLACK, Colors.BLANK);
        editButton = new Button(new Vector2I(20, 60), "Edit", 32, Colors.BLACK, Colors.BLANK);
        exitButton = new Button(new Vector2I(20, 100), "Exit", 32, Colors.BLACK, Colors.BLANK);
    }

    @Override
    public void setup(SceneManager sceneManager) {
        sceneManager.addUiElement(playButton);
        sceneManager.addUiElement(editButton);
        sceneManager.addUiElement(exitButton);
    }

    @Override
    public void update(SceneManager sm, InputHandle inputHandle) {
        if (playButton.isClicked()) {
            sm.changeScene(new PlayScene());
        }

        if (editButton.isClicked()) {
            sm.changeScene(new EditScene());
        }

        if (exitButton.isClicked()) {
            sm.quitGame();
        }
    }

    @Override
    public void draw() {
    }
}
