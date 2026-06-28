package src.scenes;

import src.game.Map;
import src.leaderboard.DB;
import src.math.Vector2I;
import src.ui.*;

public class MakeMapComp implements SceneInterface {
    Map map;
    String name;
    TextInput password = new TextInput(20, 20);
    Button submit = new Button("submit", 28);

    public MakeMapComp(Map map, String name) {
        this.map = map;
        this.name = name;
    }

    @Override
    public void setup(SceneManager sceneManager) {
        var layout = new AlignLayout(0, Align.Middle, new Vector2I(10, 10));
        layout.add(password, Align.Middle);
        layout.add(submit, Align.Middle);
        sceneManager.addUiElement(password);
        sceneManager.addUiElement(submit);
        sceneManager.setRootLayout(layout);
    }

    @Override
    public void update(SceneManager sceneManager, InputHandle inputHandle) {
        if (submit.isClicked()) {
            DB.addCompMap(name, map.hashCode(), password.getText());
            sceneManager.goToMenu();
        }
    }

    @Override
    public void draw() {

    }

    @Override
    public SceneInterface cloneScene() {
        return null;
    }
}
