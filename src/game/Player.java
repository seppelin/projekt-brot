package src.game;


import com.raylib.Colors;
import com.raylib.Helpers;
import src.ui.InputHandle;

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

    private void handleInput(InputHandle ih) {
        velocity = Helpers.newVector2(0, 0);
        // Todo: handle input of player
        if (IsKeyDown(KEY_W)) {
            velocity.y(-1);
        }
        if (IsKeyDown(KEY_S)) {
            velocity.y(1);
        }
        if (IsKeyDown(KEY_A)) {
            velocity.x(-1);
        }
        if (IsKeyDown(KEY_D)) {
            velocity.x(1);
        }
        velocity = Vector2Normalize(velocity);
        velocity = Vector2Scale(velocity, 1);
    }

    public void updateNoMap(InputHandle ih) {
        var start1 = Helpers.newVector2(174, 114);
        var end1 = Helpers.newVector2(274, 193);
        var start2 = Helpers.newVector2(148, 193);
        var end2 = Helpers.newVector2(243, 276);

        handleInput(ih);
        var oldPos = position;
        position = Vector2Add(position, velocity);

        // Prevent clipping in between
        var oldBigger = oldPos.y() - 193 > 0;
        var newBigger = position.y() - 193 > 0;
        if (oldBigger != newBigger && (oldPos.x() < start1.x() || oldPos.x() > end2.x())) {
            position.y(oldPos.y());
        }

        // Normal boarders
        if (position.y() <= 193) {
            position = Vector2Clamp(position, start1, end1);
        } else {
            position = Vector2Clamp(position, start2, end2);
        }
    }

    public void update(InputHandle inputHandle, Map map) {
        handleInput(inputHandle);

        position = Vector2Add(position, velocity);
        position = map.nearestValidPosition(position);
    }

    public void draw() {
        // Todo: draw the player
        DrawCircleV(position, 5, Colors.BLACK);
    }
}
