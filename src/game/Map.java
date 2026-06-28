package src.game;

import com.raylib.Helpers;
import src.math.Vector2I;
import src.scenes.PlayScene;
import src.ui.InputHandle;

import java.io.Serial;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.function.BiConsumer;

import static com.raylib.Raylib.*;

public class Map implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final int[][] DIRECTIONS = {
            {0, -1},
            {0, 1},
            {-1, 0},
            {1, 0},
            {1, -1},
            {-1, 1},
            {-1, -1},
            {1, 1},
    };

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
                fields[x][y] = new Field(FieldType.GRASS, null);
            }
        }
    }

    public void setRounds(int rounds) {
        this.roundSeconds = Arrays.copyOf(roundSeconds, rounds);
        this.roundMoney = Arrays.copyOf(roundMoney, rounds);
        this.roundSpawnRate = Arrays.copyOf(roundSpawnRate, rounds);
    }

    public void setBuilding(int x, int y, BuildingType building) {
        var field = getField(x, y);
        fields[x][y] = field.withBuilding(building);
    }

    public boolean setPlayerBuilding(int x, int y, BuildingType building) {
        var field = getField(x, y);
        if (field.isWalkable() && field.building() == null) {
            fields[x][y] = field.withBuilding(building);
            return true;
        }
        return false;
    }

    public int[] getRoundSeconds() {
        return roundSeconds;
    }

    public void setRoundSeconds(int roundSecond, int round) {
        this.roundSeconds[round] = roundSecond;
    }

    public int[] getRoundMoney() {
        return roundMoney;
    }

    public void setRoundMoney(int roundMoney, int round) {
        this.roundMoney[round] = roundMoney;
    }

    public float[] getRoundSpawnRate() {
        return roundSpawnRate;
    }

    public int[] getRoundSpawnRateInt() {
        var floats = getRoundSpawnRate();
        var ints = new int[floats.length];
        for (int i = 0; i < floats.length; i++) {
            ints[i] = Math.round(floats[i]);
        }
        return ints;
    }

    public void setRoundSpawnRate(float roundSpawnRate, int round) {
        this.roundSpawnRate[round] = roundSpawnRate;
    }

    public MapSpecialty[] getSpecialties() {
        return specialties;
    }

    public void setupPlayScene(PlayScene playScene) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                var field = getField(x, y);
                if (field.building() != null) {
                    switch (field.building()) {
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
                    newFields[x][y] = new Field(FieldType.GRASS, null);
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
        if (!IsMouseButtonDown(MOUSE_BUTTON_RIGHT)) {
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
            old = fields[x][y].type();
        }
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return;
        }
        var field = fields[x][y];
        if (field.type() == type || field.type() != old) {
            return;
        }

        fields[x][y] = field.withType(type);

        batchUpdate(x, y - 1, type, old);
        batchUpdate(x, y + 1, type, old);
        batchUpdate(x - 1, y, type, old);
        batchUpdate(x + 1, y, type, old);
    }

    public void setField(int x, int y, FieldType type) {
        fields[x][y] = fields[x][y].withType(type);
    }

    private Vector2I getTileOfVec(Vector2 vec) {
        return new Vector2I((int) vec.x() / 16, (int) vec.y() / 16);
    }

    private Vector2 getMiddleOfTile(Vector2I tile) {
        return Helpers.newVector2(tile.x * 16 + 8, tile.y * 16 + 8);
    }

    private float goalDistance(Vector2I tile, Vector2 goal) {
        return Vector2Distance(goal, getMiddleOfTile(tile));
    }

    public Vector2 getPathDirection(Vector2 start, Vector2 goal) {
        var startTile = getTileOfVec(start);
        var goalTile = getTileOfVec(goal);

        if (startTile.equals(goalTile)) {
            return Vector2Subtract(goal, start);
        }

        var openSet = new PriorityQueue<Node>();
        var closedSet = new boolean[width][height];

        // Maps a position to its best known gScore to prevent evaluating worse paths
        float[][] bestGScore = new float[width][height];
        for (float[] row : bestGScore) Arrays.fill(row, Float.MAX_VALUE);

        openSet.add(new Node(startTile, 0, goalDistance(startTile, goal), null));

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.pos.equals(goalTile)) {
                // reconstruct dir
                while (current.parent != null && !current.parent.pos.equals(startTile)) {
                    current = current.parent;
                }
                return Vector2Subtract(getMiddleOfTile(current.pos), start);
            }

            closedSet[current.pos.x][current.pos.y] = true;


            for (var i = 0; i < DIRECTIONS.length; i++) {
                var dir = DIRECTIONS[i];
                int nextX = current.pos.x + dir[0];
                int nextY = current.pos.y + dir[1];

                // Bounds and walkability check
                if (nextX < 0 || nextX >= width || nextY < 0 || nextY >= height) continue;
                if (!fields[nextX][nextY].isWalkable() || closedSet[nextX][nextY]) continue;

                float tentativeGScore = current.gScore + 16; // Constant edge weight of 16
                if (i >= 4) tentativeGScore += 6.627416998f; // Diagonal

                if (tentativeGScore < bestGScore[nextX][nextY]) {
                    bestGScore[nextX][nextY] = tentativeGScore;
                    var nextPos = new Vector2I(nextX, nextY);
                    float fScore = tentativeGScore + goalDistance(nextPos, goal);
                    openSet.add(new Node(nextPos, tentativeGScore, fScore, current));
                }
            }
        }

        return null;
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
                        var nearestPoint = Vector2Clamp(point, Helpers.newVector2(xPos + 0.1f, yPos + 0.1f), Helpers.newVector2(xPos + 15.9f, yPos + 15.9f));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return hashCode() == o.hashCode();
    }

    public int computeIntHash() {
        try {
            // Allocate a safe capacity buffer for your data payload
            int estimatedSize = width * height * 8 + 512;
            ByteBuffer buffer = ByteBuffer.allocate(estimatedSize);

            // 1. Primitive fields
            buffer.putInt(gemReward);
            buffer.putInt(width);
            buffer.putInt(height);

            // 2. 1D Int Arrays
            if (roundSeconds != null) {
                for (int s : roundSeconds) buffer.putInt(s);
            }
            if (roundMoney != null) {
                for (int m : roundMoney) buffer.putInt(m);
            }

            // 3. Float Array
            if (roundSpawnRate != null) {
                for (float f : roundSpawnRate) buffer.putFloat(f);
            }

            // 4. Enum Array
            if (specialties != null) {
                for (MapSpecialty specialty : specialties) {
                    if (specialty != null) {
                        buffer.put(specialty.name().getBytes(StandardCharsets.UTF_8));
                    }
                }
            }

            // 5. 2D Record Array
            if (fields != null) {
                for (Field[] row : fields) {
                    if (row != null) {
                        for (Field field : row) {
                            if (field != null) {
                                if (field.building() != null) {
                                    buffer.put(field.building().name().getBytes(StandardCharsets.UTF_8));
                                }
                                if (field.type() != null) {
                                    buffer.put(field.type().name().getBytes(StandardCharsets.UTF_8));
                                }
                            }
                        }
                    }
                }
            }

            // Flip the buffer and run the SHA-256 algorithm
            buffer.flip();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(buffer);
            byte[] hashBytes = digest.digest();

            // Take the first 4 bytes of the SHA-256 hash and pack them into a 32-bit int
            return ((hashBytes[0] & 0xFF) << 24) |
                    ((hashBytes[1] & 0xFF) << 16) |
                    ((hashBytes[2] & 0xFF) << 8) |
                    (hashBytes[3] & 0xFF);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int hashCode() {
        return computeIntHash();
    }

    private static class Node implements Comparable<Node> {
        Vector2I pos;
        float gScore;
        float fScore;
        Node parent;

        public Node(Vector2I pos, float gScore, float fScore, Node parent) {
            this.pos = pos;
            this.gScore = gScore;
            this.fScore = fScore;
            this.parent = parent;
        }

        @Override
        public int compareTo(Node node) {
            return Float.compare(this.fScore, node.fScore);
        }
    }
}
