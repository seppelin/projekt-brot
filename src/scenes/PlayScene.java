package src.scenes;

import src.game.Map;

public class PlayScene extends Scene {
    Map map;
    public PlayScene() {
        this.map = new Map(20, 20);
    }

    @Override
    public Scene update() {
        map.update();
        // Todo: go back to menu scene when ESC pressed
        return null;
    }

    @Override
    public void draw() {
        map.draw();
    }
}
