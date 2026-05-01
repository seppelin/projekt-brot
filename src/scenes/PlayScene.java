package src.scenes;

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;
import src.game.Camera;
import src.game.Map;
import src.game.Player;
import src.ui.Button;

public class PlayScene implements Scene {
    Camera camera;
    Player player;
    Map map;

    public PlayScene() {
        this.map = new Map(20, 20);
        this.player = new Player(map.getWidth()/2, map.getHeight()/2);
        this.camera = new Camera(160, 160, 4);
    }

    @Override
    public void setup(SceneManager sceneManager) {}

    @Override
    public void update(SceneManager sm) {
        map.update();
        player.update();
        camera.target(player.getPosition());
        camera.handleResize();
        camera.scrollZoom();
        // Todo: go back to menu scene when ESC pressed
    }

    @Override
    public void draw() {
        Raylib.BeginMode2D(camera);
        map.draw();
        player.draw();
        Raylib.EndMode2D();
    }
}
