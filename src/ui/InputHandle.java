package src.ui;

public class InputHandle {
    private boolean scrollTaken = false;
    private boolean mouseTaken = false;
    private boolean keysTaken = false;

    public boolean tryTakeScroll() {
        if (!scrollTaken) {
            scrollTaken = true;
            return true;
        }
        return false;
    }

    public boolean tryTakeMouse() {
        if (!mouseTaken) {
            mouseTaken = true;
            return true;
        }
        return false;
    }

    public boolean tryTakeKeyBoard() {
        if (!keysTaken) {
            keysTaken = true;
            return true;
        }
        return false;
    }
}
