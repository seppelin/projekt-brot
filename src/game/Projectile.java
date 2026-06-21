package src.game;

import com.raylib.Colors;
import com.raylib.Raylib;

import static com.raylib.Raylib.*;

public class Projectile {
    private final float speed;
    private final Target target;
    private final int damage;
    private Vector2 position;

    public Projectile(Vector2 start, Target target, float speed, int damage) {
        this.speed = speed;
        this.position = start;
        this.target = target;
        this.damage = damage;
    }

    public boolean update() {
        if (!target.existing()) {
            return true;
        }
        var velocity = Vector2Subtract(target.getPosition(), position);
        velocity = Vector2Normalize(velocity);
        velocity = Vector2Scale(velocity, speed);

        this.position = Vector2Add(position, velocity);

        // Check if the Projectile has overcome the target
        var secondVelocity = Vector2Subtract(target.getPosition(), position);
        var signs = Vector2Multiply(secondVelocity, velocity);
        if (signs.x() < 0 || signs.y() < 0 || Vector2Distance(this.position, target.getPosition()) < 1) {
            target.dealDamage(damage);
            return true;
        }
        return false;
    }

    public void draw() {
        Raylib.DrawCircleV(position, 1, Colors.BLACK);
    }
}
