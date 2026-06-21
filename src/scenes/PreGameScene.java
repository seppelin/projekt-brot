package src.scenes;

import com.raylib.Raylib;
import src.game.BuildingType;
import src.game.Camera;
import src.game.GameInfo;
import src.game.Map;
import src.math.Vector2I;
import src.ui.*;

public class PreGameScene implements SceneInterface {
    Map map;
    GameInfo gameInfo;
    Camera camera;
    Button start;
    BuySelector selector;

    public PreGameScene(Map map, GameInfo info) {
        this.map = map;
        this.gameInfo = info;
        selector = new BuySelector(new Vector2I(0, 0),
                new Vector2I(32, 32), new Vector2I(10, 10),
                6, "$", BuildingType.PLAYER_BUILDINGS);
        camera = new Camera(map.getWidth() * 8, map.getHeight() * 8, 4);
        start = new Button("start", 26);
    }

    public static PreGameScene gameStart(Map map, String name, boolean comp) {
        var info = new GameInfo(map, name, comp);
        return new PreGameScene(map, info);
    }

    @Override
    public void setup(SceneManager sceneManager) {
        var layout = new AlignLayout(0, Align.Start, new Vector2I(10, 10));
        layout.add(start, Align.Start);
        layout.add(gameInfo, Align.Start);
        layout.add(selector, Align.End);
        sceneManager.addUiElement(start);
        sceneManager.addUiElement(gameInfo);
        sceneManager.setRootLayout(layout);
    }

    @Override
    public void update(SceneManager sceneManager, InputHandle inputHandle) {
        this.map.update(inputHandle, camera, (x, y) -> {
            var sel = this.selector.getSelected();
            if (sel != null) {
                if (this.map.setPlayerBuilding(x, y, (BuildingType) sel)) {
                    this.gameInfo.setMoney(gameInfo.getMoney() - sel.getPrice());
                }
            }
        });
        selector.update(inputHandle, gameInfo.getMoney());

        camera.scrollZoom(inputHandle);
        camera.handleResize();
        camera.mouseMove(inputHandle);

        if (start.isClicked()) {
            sceneManager.changeScene(new PlayScene(this.map, this.gameInfo));
        }
    }

    @Override
    public void draw() {
        Raylib.BeginMode2D(camera);
        map.draw();
        Raylib.EndMode2D();

        selector.draw(gameInfo.getMoney());
    }

    @Override
    public SceneInterface cloneScene() {
        return new PreGameScene(map, gameInfo);
    }
}
