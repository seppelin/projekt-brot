package src.game;

import com.raylib.Raylib;
import src.math.Vector2I;
import src.scenes.PlayScene;

public class Cannon extends Building {
    static final double cooldown = 0.8;
    static final int damage = 50;
    static final float range = 80;

    double wait = cooldown;

    Cannon(PlayScene play, Vector2I mapPos) {
        super(play, mapPos, 150);
    }

    @Override
    public void aliveUpdate() {
        wait -= Raylib.GetFrameTime();
        if (wait <= 0) {
            var enemy = play.getNearestEnemy(getPosition());
            if (enemy != null && Raylib.Vector2Distance(getPosition(), enemy.getPosition()) <= range) {
                play.projectiles.add(new Projectile(getPosition(),
                        enemy, 8, damage));
                wait = cooldown;
            }
        }
    }
}
