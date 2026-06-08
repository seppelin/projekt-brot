package src.game;


import com.raylib.Colors;
import com.raylib.Helpers;
import src.ui.InputHandle;

import static com.raylib.Raylib.*;

public class Enemy {
    private Vector2 position;
    private Vector2 velocity;

    public Enemy(int startX, int startY) {
        velocity = new Vector2();
        // Position of the player in pixel, 16px one field
        position = Helpers.newVector2(startX * 16, startY * 16);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void update(Map map, Vector2 target) {
        velocity = Vector2Subtract(target, position);
        velocity = Vector2Normalize(velocity);
        velocity = Vector2Scale(velocity, 1);
        position = Vector2Add(position, velocity);
        position = map.nearestValidPosition(position);
    }

    public void draw() {
        // Todo: draw the player
        DrawCircleV(position, 5, Colors.RED);
    }
}
