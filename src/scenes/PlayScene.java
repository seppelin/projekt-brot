package src.scenes;

import com.raylib.Raylib;
import src.game.Camera;
import src.game.Map;
import src.game.Player;
import src.game.Enemy;
import src.ui.InputHandle;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class PlayScene implements SceneInterface {
    Camera camera;
    Player player;
    Enemy enemy;
    Map map;

    public PlayScene() {
        try (var in = new ObjectInputStream(new FileInputStream("map.savefile"))) {
            this.map = (Map) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.player = new Player(map.getWidth() / 2, map.getHeight() / 2);
        this.camera = new Camera(160, 160, 4);
        this.enemy = new Enemy(2, 1);
    }

    @Override
    public void setup(SceneManager sceneManager) {
    }

    @Override
    public void update(SceneManager sm, InputHandle inputHandle) {
        player.update(inputHandle, this.map);
        enemy.update(this.map, this.player.getPosition());
        camera.target(player.getPosition());
        camera.handleResize();
        camera.scrollZoom(inputHandle);
        // Todo: go back to menu scene when ESC pressed
    }

    @Override
    public void draw() {
        Raylib.BeginMode2D(camera);
        map.draw();
        player.draw();
        enemy.draw();
        Raylib.EndMode2D();
    }
}
