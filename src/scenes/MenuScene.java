package src.scenes;

import com.raylib.Colors;
import src.math.Vector2I;
import src.ui.Button;
import src.ui.InputHandle;

public class MenuScene implements SceneInterface {
    Button playButton;
    Button editButton;
    Button exitButton;
    Button shopButton;

    public MenuScene() {
        playButton = new Button(Helpers.newVector2(20, 20), "Play", 32,Colors.BLACK, Colors.BLANK);
        editButton = new Button(Helpers.newVector2(20, 60), "Edit", 32,Colors.BLACK, Colors.BLANK);
        exitButton = new Button(Helpers.newVector2(20, 100), "Exit", 32,Colors.BLACK, Colors.BLANK);

        playButton = new Button(new Vector2I(20, 20), "Play", 32, Colors.BLACK, Colors.BLANK);
        editButton = new Button(new Vector2I(20, 60), "Edit", 32, Colors.BLACK, Colors.BLANK);
        exitButton = new Button(new Vector2I(20, 100), "Exit", 32, Colors.BLACK, Colors.BLANK);
        shopButton = new Button(new Vector2I(20, 140), "Shop", 32,Colors.RED, Colors.BLANK);

    }

    @Override
    public void setup(SceneManager sceneManager) {
        sceneManager.addUiElement(playButton);
        sceneManager.addUiElement(editButton);
        sceneManager.addUiElement(exitButton);
        sceneManager.addUiElement(shopButton);
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
        
        if (shopButton.isClicked()){
            sm.changeScene(new ShopScene());
        }
    }

    @Override
    public void draw() {
    }
}
