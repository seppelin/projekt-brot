package src.game;

import com.raylib.Raylib;

// Types of weapons
public enum WeaponType {
    Fist,
    Knife,
    Shotgun,
    Minigun,
    RainersKatana,
    ;
    final static Raylib.Texture texture = Raylib.LoadTexture("resources/sword.png");

    public Raylib.Texture getTexture() {
        return texture;
    }
}
