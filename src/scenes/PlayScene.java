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

import java.util.Vector;

public class PlayScene implements SceneInterface {
    public Vector<Enemy> enemies = new Vector<>();
    
    Camera camera;
    Player player;
    Map map;

    public PlayScene() {
        try (var in = new ObjectInputStream(new FileInputStream("resources/maps/default.mapdata"))) {
            this.map = (Map) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
        // Todo: go back to menu scene when ESC pressed
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
}
