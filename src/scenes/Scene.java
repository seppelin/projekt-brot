package src.scenes;

public abstract class Scene {
    private boolean quit = false;

    public void setQuit(boolean quit) {
        this.quit = quit;
    }

    public boolean shouldQuit() {
        return quit;
    }

    public abstract Scene update();

    public abstract void draw();
}
