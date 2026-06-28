package src.scenes;

import src.game.GameInfo;
import src.game.Map;
import src.leaderboard.DB;
import src.math.Vector2I;
import src.ui.*;

public class AddToLeaderBoardScene implements SceneInterface {
    Map map;
    GameInfo gameInfo;
    TextInput name = new TextInput(20, 20, "name");
    Button submit = new Button("submit", 20);

    public AddToLeaderBoardScene(Map map, GameInfo info) {
        this.map = map;
        this.gameInfo = info;
    }

    @Override
    public void setup(SceneManager sceneManager) {
        var layout = new AlignLayout(1, Align.Middle, new Vector2I(10, 10));
        layout.add(name, Align.Middle);
        layout.add(submit, Align.Middle);
        sceneManager.setRootLayout(layout);
    }

    @Override
    public void update(SceneManager sceneManager, InputHandle inputHandle) {
        name.update(inputHandle);
        submit.update(inputHandle);
        var text = name.getText();
        if (submit.isClicked() && !text.isEmpty()) {
            DB.addLeaderBoard(gameInfo.getMapName(), text, gameInfo.getMoney(), gameInfo.getOriginalHash());
            sceneManager.goToMenu();
        }
    }

    @Override
    public void draw() {
        name.draw();
        if (!name.getText().isEmpty()) {
            submit.draw();
        } else {
            submit.drawInactive();
        }
    }

    @Override
    public SceneInterface cloneScene() {
        return null;
    }
}
