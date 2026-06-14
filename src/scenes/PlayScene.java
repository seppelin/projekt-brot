package src.scenes;

import com.raylib.Raylib;
import src.game.Camera;
import src.game.Enemy;
import src.game.Map;
import src.game.Player;
import src.ui.InputHandle;

import java.util.Vector;

public class PlayScene implements SceneInterface {
    public Vector<Enemy> enemies = new Vector<>();

    Camera camera;
    Player player;
    Map map;

    public PlayScene(Map map) {
        this.map = map;
        this.player = new Player(map.getWidth() / 2, map.getHeight() / 2);
        this.camera = new Camera(160, 160, 4);
    }

    @Override
    public void setup(SceneManager sceneManager) {
    }

    @Override
    public void update(SceneManager sm, InputHandle inputHandle) {
        player.update(inputHandle, this.map);
        for (var e : enemies) {
            e.update(this.map, this.player.getPosition());
        }
        camera.target(player.getPosition());
        camera.handleResize();
        camera.scrollZoom(inputHandle);
        map.updateFields(this.enemies);
    }

    @Override
    public void draw() {
        Raylib.BeginMode2D(camera);
        map.draw();
        player.draw();
        for (var e : enemies) {
            e.draw();
        }
        Raylib.EndMode2D();
    }

    @Override
    public SceneInterface cloneScene() {
        return new PlayScene(this.map);
    }
}
