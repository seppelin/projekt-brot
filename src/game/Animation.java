package src.game;

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;
import src.math.Vector2I;

public class Animation {
    final float frameTime;
    Raylib.Texture texture;
    Vector2I frameSize;
    int framePos;
    double changeTime;
    boolean paused;
    double damageTime = 0;

    public Animation(Raylib.Texture texture, int frameWidth, float frameTime) {
        this.texture = texture;
        this.frameSize = new Vector2I(frameWidth, texture.height());
        this.frameTime = frameTime;
        framePos = 0;
        this.paused = true;
    }

    public boolean isRunning() {
        return !paused;
    }

    public void setDamage() {
        damageTime = Raylib.GetTime() + frameTime;
    }

    public void stop() {
        this.framePos = 0;
        this.paused = true;
    }

    public void start() {
        this.paused = false;
        this.changeTime = Raylib.GetTime() + frameTime;
    }

    public void progress() {
        if (paused) {
            start();
        }
        update();
    }

    public void update() {
        if (!paused && changeTime <= Raylib.GetTime()) {
            this.framePos += this.frameSize.x;
            this.framePos %= this.texture.width();
            changeTime += frameTime;
        }
    }

    public void draw(Raylib.Rectangle rect) {
        var color = damageTime > Raylib.GetTime() ? Colors.RED : Colors.WHITE;
        Raylib.DrawTexturePro(texture,
                Helpers.newRectangle(framePos, 0, frameSize.x, frameSize.y),
                rect, Helpers.newVector2(0, 0),
                0, color);
    }
}
