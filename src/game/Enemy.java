package src.game;

import com.raylib.Raylib;
import src.math.Vector2I;
import src.scenes.PlayScene;

import static com.raylib.Raylib.*;

public class Enemy implements Target {
    static final Raylib.Texture a = Raylib.LoadTexture("resources/zombie_anim.png");
    final Vector2I size = new Vector2I(10, 15);
    private final Animation anim = new Animation(a, 16, 0.2f);
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
    public boolean dealDamage(int damage) {
        anim.setDamage();
        var was = existing();
        this.health -= damage;
        return this.health < 0 && was;
    }

    // Update enemy position, moving towards target
    public boolean update() {
        if (!existing()) {
            return true;
        }
        anim.progress();
        var brotPos = this.play.getNearestBrotPos(position);
        // Calculate direction to target
        velocity = this.play.getMap().getPathDirection(position, brotPos);
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
        anim.draw(size.centeredRect(position));
    }
}
