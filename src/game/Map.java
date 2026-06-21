package src.game;

import com.raylib.Helpers;
import src.math.Vector2I;
import src.scenes.PlayScene;
import src.ui.InputHandle;
import src.ui.SelItemInterface;

import java.io.Serial;
import java.io.Serializable;
import java.util.function.BiConsumer;

import static com.raylib.Raylib.*;

public class Map implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int[] roundSeconds = {10, 20, 30};
    private int[] roundMoney = {50, 100, 200};
    private float[] roundSpawnRate = {1, 1.5f, 2};
    private int gemReward = 50;
    private MapSpecialty[] specialties;
    private int width;
    private int height;
    private Field[][] fields;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;

        fields = new Field[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                fields[x][y] = new Field(FieldType.GRASS);
            }
        }
    }

    public void setBuilding(int x, int y, BuildingType building) {
        var field = getField(x, y);
        if (field.isWalkable()) {
            field.item = building;
        }
    }

    public boolean setPlayerBuilding(int x, int y, BuildingType building) {
        var field = getField(x, y);
        if (field.isWalkable() && field.item == null) {
            field.item = building;
            return true;
        }
        return false;
    }

    public int[] getRoundSeconds() {
        return roundSeconds;
    }

    public void setRoundSeconds(int[] roundSeconds) {
        this.roundSeconds = roundSeconds;
    }

    public int[] getRoundMoney() {
        return roundMoney;
    }

    public void setRoundMoney(int[] roundMoney) {
        this.roundMoney = roundMoney;
    }

    public float[] getRoundSpawnRate() {
        return roundSpawnRate;
    }

    public void setRoundSpawnRate(float[] roundSpawnRate) {
        this.roundSpawnRate = roundSpawnRate;
    }

    public MapSpecialty[] getSpecialties() {
        return specialties;
    }

    public void setupPlayScene(PlayScene playScene) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                var field = getField(x, y);
                if (field.item != null) {
                    switch (field.item) {
                        case Spawner -> playScene.enemyBuildings.add(new Graveyard(playScene, new Vector2I(x, y)));
                        case Cannon -> playScene.playerBuildings.add(new Cannon(playScene, new Vector2I(x, y)));
                    }
                }
            }
        }
    }

    public void addSpecialty(MapSpecialty specialty) {
        var newSpecials = new MapSpecialty[specialties.length + 1];
        System.arraycopy(specialties, 0, newSpecials, 0, specialties.length);
        newSpecials[specialties.length] = specialty;
        specialties = newSpecials;
    }

    public void changeSize(int width, int height) {
        var newFields = new Field[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x < this.width && y < this.height) {
                    newFields[x][y] = fields[x][y];
                } else {
                    newFields[x][y] = new Field(FieldType.GRASS);
                }
            }
        }
        this.fields = newFields;
        this.width = width;
        this.height = height;
    }

    public Field getField(int x, int y) {
        return fields[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void update(InputHandle ih, Camera camera, BiConsumer<Integer, Integer> onFieldClick) {
        var worldMousePos = GetScreenToWorld2D(GetMousePosition(), camera);
        if (!IsMouseButtonPressed(MOUSE_BUTTON_RIGHT)) {
            return;
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (CheckCollisionPointRec(worldMousePos, Helpers.newRectangle(x * 16, y * 16, 16, 16))
                        && ih.tryTakeMouse()) {
                    onFieldClick.accept(x, y);
                }
            }
        }
    }

    public void batchUpdate(int x, int y, FieldType type, FieldType old) {
        if (old == null) {
            old = fields[x][y].type;
        }
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return;
        }
        var field = fields[x][y];
        if (field.type == type || field.type != old) {
            return;
        }

        field.type = type;

        batchUpdate(x, y - 1, type, old);
        batchUpdate(x, y + 1, type, old);
        batchUpdate(x - 1, y, type, old);
        batchUpdate(x + 1, y, type, old);
    }

    public void updateSelection(InputHandle inputHandle, SelItemInterface selected, Camera camera) {
        var worldMousePos = GetScreenToWorld2D(GetMousePosition(), camera);
        if (IsMouseButtonPressed(MOUSE_BUTTON_RIGHT)) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (CheckCollisionPointRec(worldMousePos, Helpers.newRectangle(x * 16, y * 16, 16, 16))
                            && inputHandle.tryTakeMouse()) {
                        if (selected instanceof FieldType) {
                            getField(x, y).type = (FieldType) selected;
                        } else {
                            getField(x, y).item = (BuildingType) selected;
                        }
                    }
                }
            }
        }
    }

    public void draw() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                getField(x, y).draw(x * 16, y * 16);
            }
        }
    }

    public Vector2 nearestValidPosition(Vector2 point) {
        int fieldX = (int) (point.x() / 16);
        int fieldY = (int) (point.y() / 16);
        for (int ringSize = 0; ringSize < this.width; ringSize++) {
            float distance = 10000;
            Vector2 validPos = null;
            for (int offsetX = 0; offsetX <= 2 * ringSize; offsetX++) {
                for (int offsetY = 0; offsetY <= 2 * ringSize; offsetY++) {
                    if (offsetX != 0 && offsetX != 2 * ringSize && offsetY != 0 && offsetY != 2 * ringSize) {
                        continue;
                    }
                    var x = fieldX + offsetX - ringSize;
                    var y = fieldY + offsetY - ringSize;
                    if (x >= width || y >= width || x < 0 || y < 0) {
                        continue;
                    }
                    var xPos = x * 16;
                    var yPos = y * 16;
                    if (getField(x, y).isWalkable()) {
                        var nearestPoint = Vector2Clamp(point, Helpers.newVector2(xPos, yPos), Helpers.newVector2(xPos + 16, yPos + 16));
                        var newDistance = Vector2Distance(nearestPoint, point);
                        if (distance > newDistance) {
                            validPos = nearestPoint;
                            distance = newDistance;
                        }
                    }
                }
            }
            if (validPos != null) {
                return validPos;
            }
        }
        return point;
    }

    public int getGemReward() {
        return gemReward;
    }

    public void setGemReward(int gemReward) {
        this.gemReward = gemReward;
    }
}
