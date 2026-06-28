package src.game;

import com.raylib.Colors;
import com.raylib.Raylib;
import src.math.RectangleI;
import src.math.Vector2I;
import src.ui.InputHandle;
import src.ui.LayoutInterface;
import src.ui.UiInterface;

public class GameInfo implements UiInterface, LayoutInterface {
    Vector2I position;
    String mapName;
    boolean isCompetitive;
    int money;
    int round;
    int maxRounds;
    int originalHash;

    public GameInfo(Map map, String name, boolean comp) {
        this.position = new Vector2I(0, 0);
        this.mapName = name;
        this.isCompetitive = comp;
        this.money = map.getRoundMoney()[0];
        this.round = 0;
        this.maxRounds = map.getRoundMoney().length;
        originalHash = map.hashCode();
    }

    public int getOriginalHash() {
        return originalHash;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public boolean isCompetitive() {
        return isCompetitive;
    }

    public void setCompetitive(boolean competitive) {
        isCompetitive = competitive;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    @Override
    public void setSpace(RectangleI rect) {
        this.position = rect.pos;
    }

    @Override
    public Vector2I minimum() {
        var x = Raylib.MeasureText("Money: 888$, Round: 88/88", 28);
        return new Vector2I(x, 32);
    }

    @Override
    public void update(InputHandle inputHandle) {
    }

    @Override
    public void draw() {
        Raylib.DrawText("Money: " + money + "$, Round: " + (round + 1) + "/" + maxRounds,
                this.position.x, this.position.y + 2, 28, Colors.BLACK);
    }

    @Override
    public float extraSpaceGreed() {
        return 0;
    }
}
