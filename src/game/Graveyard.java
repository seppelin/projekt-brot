package src.game;

import com.raylib.Raylib;
import src.math.Vector2I;
import src.scenes.PlayScene;

public class Graveyard extends Building {
    static final int zombieHealth = 90;
    static final int startHealth = 70;
    static final double startCooldown = 0.5;
    static final double randomAdd = 6;

    double wait;

    Graveyard(PlayScene play, Vector2I mapPos) {
        super(play, mapPos, startHealth);
        setWait();
    }

    private void setWait() {
        wait = startCooldown + Math.random() * randomAdd;
        wait /= play.getSpawnRate();
    }

    @Override
    void aliveUpdate() {
        wait -= Raylib.GetFrameTime();
        if (wait <= 0) {
            play.enemies.add(new Enemy(play, getPosition(), zombieHealth));
            setWait();
        }
    }
}
