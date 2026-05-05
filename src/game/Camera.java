package src.game;

import com.raylib.Helpers;
import com.raylib.Raylib;
import src.ui.InputHandle;

import static com.raylib.Raylib.GetMouseWheelMove;

public class Camera extends Raylib.Camera2D {
    private boolean dragged = false;

    public Camera(float targetX, float targetY, float zoom) {
        // Offset the camera by half the screen size to center the target
        offset(calcOffset());
        // The target is the player, the camera follows him
        target(Helpers.newVector2(targetX, targetY));
        // Setting the rotation to 0 and the zoom to 1
        rotation(0);
        zoom(zoom);
    }

    public Raylib.Vector2 calcOffset() {
        return Helpers.newVector2((float) Raylib.GetScreenWidth() / 2, (float) Raylib.GetScreenHeight() / 2);
    }

    public void handleResize() {
        if (Raylib.IsWindowResized()) {
            offset(calcOffset());
        }
    }

    public void scrollZoom(InputHandle inputHandle) {
        var wheelMove = GetMouseWheelMove();
        if (wheelMove != 0 && inputHandle.tryTakeScroll()) {
            var mouseWheelMovement = wheelMove / 5;
            var newZoom = Math.clamp(zoom() + mouseWheelMovement, 1, 8);
            zoom(newZoom);
        }
    }

    public void mouseMove(InputHandle  inputHandle) {
        if (Raylib.IsMouseButtonPressed(Raylib.MOUSE_BUTTON_LEFT) && inputHandle.tryTakeMouse()) {
            dragged = true;
        }
        if (dragged && Raylib.IsMouseButtonDown(Raylib.MOUSE_BUTTON_LEFT)) {
            var delta = Raylib.GetMouseDelta();
            delta = Raylib.Vector2Scale(delta, 1/zoom());
            var newTarget = Raylib.Vector2Subtract(target(), delta);
            target(newTarget);
        } else {
            dragged = false;
        }
    }
}
