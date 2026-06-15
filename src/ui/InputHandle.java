package src.ui;

// Manages input event consumption to prevent duplicate handling
public class InputHandle {
    private boolean scrollTaken = false;
    private boolean mouseTaken = false;
    private boolean keysTaken = false;
    private boolean escTaken = false;

    // Try to consume escape key
    public boolean tryTakeEsc() {
        if (!escTaken) {
            escTaken = true;
            return true;
        }
        return false;
    }

    // Try to consume scroll input
    public boolean tryTakeScroll() {
        if (!scrollTaken) {
            scrollTaken = true;
            return true;
        }
        return false;
    }

    // Try to consume mouse input
    public boolean tryTakeMouse() {
        if (!mouseTaken) {
            mouseTaken = true;
            return true;
        }
        return false;
    }

    // Try to consume keyboard input
    public boolean tryTakeKeyBoard() {
        if (!keysTaken) {
            keysTaken = true;
            return true;
        }
        return false;
    }
}
