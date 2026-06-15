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
        // Convert grid position to pixel coordinates (16px per field)
        position = Helpers.newVector2(startX * 16, startY * 16);
    }

    public Vector2 getPosition() {
        return position;
    }

    // Update enemy position, moving towards target
    public void update(Map map, Vector2 target) {
        // Calculate direction to target
        velocity = Vector2Subtract(target, position);
        // Normalize direction and set speed
        velocity = Vector2Normalize(velocity);
        velocity = Vector2Scale(velocity, 0.5f);
        // Move and snap to valid position
        position = Vector2Add(position, velocity);
        position = map.nearestValidPosition(position);
    }

    public void draw() {
        DrawCircleV(position, 5, Colors.RED);
    }
}
