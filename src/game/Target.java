package src.game;

import com.raylib.Raylib;

public interface Target {
    public boolean existing();

    public Raylib.Vector2 getPosition();

    public boolean dealDamage(int damage);
}
