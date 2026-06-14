package src.ui;

public class InputHandle {
    private boolean scrollTaken = false;
    private boolean mouseTaken = false;
    private boolean keysTaken = false;
    private boolean escTaken = false;

    public boolean tryTakeEsc() {
        if (!escTaken) {
            escTaken = true;
            return true;
        }
        return false;
    }

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
