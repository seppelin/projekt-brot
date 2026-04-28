package src.game;

import com.raylib.Colors;
import com.raylib.Helpers;
import static com.raylib.Raylib.*;

public class Player {
    private Camera2D camera;
    private Vector2 position;
    private Vector2 velocity;

    public Player(int startX, int startY) {
        velocity = Helpers.newVector2(0, 0);
        // Position of the player in pixel, 16px one field
        position = Helpers.newVector2(startX * 16, startY * 16);

        // Offset the camer by half the screen size to center the player
        var offset = Helpers.newVector2(400, 300);
        // The target is the player, the camera follows him
        var target = position;

        // Setting the rotation to 0 and the zoom to 1
        camera = Helpers.newCamera2D(offset, target, 0, 4);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Camera2D getCamera() {
        // update the camera to focus the player
        camera.target(position);
        return camera;
    }

    public void handleInput() {
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
        position = Vector2Add(position, velocity);
    }

    public void draw() {
        // Todo: draw the player
        DrawCircleV(position, 5, Colors.BLACK);
    }
}
