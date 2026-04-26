package game;

import com.raylib.Helpers;
import com.raylib.Raylib.*;

public class Player {
    private Camera2D camera;
    private Vector2 position;

    public Player(int startX, int startY) {
        // Position of the player in pixel, 16px one field
        position = Helpers.newVector2(startX * 16, startY * 16);

        // Offset the camer by half the screen size to center the player
        var offset = Helpers.newVector2(400, 300);
        // The target is the player, the camera follows him
        var target = position;

        // Setting the rotation to 0 and the zoom to 1
        camera = Helpers.newCamera2D(offset, target, 0, 1);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Camera2D getCamera() {
        // update the camera to focus the player
        camera.target(position);
        return camera;
    }

    public void update() {
        // Todo: handle input of player
    }

    public void draw() {
        // Todo: draw the player
    }
}
