package src.game;

import com.raylib.Colors;
import src.scenes.PlayScene;

import static com.raylib.Raylib.*;

public class Enemy implements Target {
    PlayScene play;
    private int health;
    private Vector2 position;
    private Vector2 velocity;

    public Enemy(PlayScene play, Vector2 position, int health) {
        this.play = play;
        velocity = new Vector2();
        // Convert grid position to pixel coordinates (16px per field)
        this.position = position;
        this.health = health;
    }

    @Override
    public boolean existing() {
        return health > 0;
    }

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public void dealDamage(int damage) {
        this.health -= damage;
    }

    // Update enemy position, moving towards target
    public boolean update() {
        if (!existing()) {
            return true;
        }
        var brotPos = play.getNearestBrotPos(position);
        // Calculate direction to target
        velocity = Vector2Subtract(brotPos, position);
        // Normalize direction and set speed
        velocity = Vector2Normalize(velocity);
        velocity = Vector2Scale(velocity, 0.5f);
        // Move and snap to valid position
        position = Vector2Add(position, velocity);
        position = play.getMap().nearestValidPosition(position);

        if (Vector2Distance(brotPos, position) < 4) {
            play.breadFound();
        }
        return false;
    }

    public void draw() {
        DrawCircleV(position, 5, Colors.RED);
    }
}
