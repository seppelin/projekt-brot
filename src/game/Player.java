package src.game;

import com.raylib.Colors;
import com.raylib.Helpers;

import static com.raylib.Raylib.*;

public class Player {
    private Vector2 position;
    private Vector2 velocity;

    public Player(int startX, int startY) {
        velocity = new Vector2();
        // Position of the player in pixel, 16px one field
        position = Helpers.newVector2(startX * 16, startY * 16);
    }

    public Vector2 getPosition() {
        return position;
    }

    private void handleInput() {
        velocity = Helpers.newVector2(0, 0);
        // Todo: handle input of player
        if (IsKeyDown(KEY_W)) {
            velocity.y(-1);
        } if (IsKeyDown(KEY_S)) {
            velocity.y(1);
        } if  (IsKeyDown(KEY_A)) {
            velocity.x(-1);
        } if (IsKeyDown(KEY_D)) {
            velocity.x(1);
        }
        velocity = Vector2Normalize(velocity);
        velocity = Vector2Scale(velocity, 1);
    }

    public void update() {
        handleInput();
        position = Vector2Add(position, velocity);
        System.out.println(position.x() + ", " + position.y());
        position = Vector2Clamp(position, new Vector2().x(0).y(0), new Vector2().x(16*20).y(16*20));
    }

    public void draw() {
        // Todo: draw the player
        DrawCircleV(position, 5, Colors.BLACK);
    }
}
