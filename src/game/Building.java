package src.game;

import com.raylib.Helpers;
import com.raylib.Raylib;
import src.math.Vector2I;
import src.scenes.PlayScene;

public abstract class Building implements Target {
    PlayScene play;
    boolean exists = true;
    Vector2I mapPos;
    int health;

    Building(PlayScene play, Vector2I mapPos, int health) {
        this.play = play;
        this.mapPos = mapPos;
        this.health = health;
    }

    abstract void aliveUpdate();

    public boolean update() {
        if (exists) {
            aliveUpdate();
            return false;
        }
        return true;
    }

    @Override
    public boolean existing() {
        return exists;
    }

    @Override
    public Raylib.Vector2 getPosition() {
        return Helpers.newVector2(mapPos.x * 16 + 8, mapPos.y * 16 + 8);
    }

    @Override
    public void dealDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            exists = false;
        }
    }
}
