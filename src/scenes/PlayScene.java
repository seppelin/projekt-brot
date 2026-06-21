package src.scenes;

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;
import src.game.*;
import src.ui.InputHandle;

import java.util.ArrayList;

import static com.raylib.Raylib.Vector2;
import static com.raylib.Raylib.Vector2Distance;

// Gameplay scene
public class PlayScene implements SceneInterface {
    // List of active enemies
    public ArrayList<Enemy> enemies = new ArrayList<>();
    public ArrayList<Projectile> projectiles = new ArrayList<>();
    public ArrayList<Building> playerBuildings = new ArrayList<>();
    public ArrayList<Building> enemyBuildings = new ArrayList<>();

    Camera camera;
    Player player;
    Map map;
    GameInfo gameInfo;
    GameState state;

    double roundTimeLeft;


    public PlayScene(Map map, GameInfo gameInfo) {
        this.map = map;
        this.player = new Player(map.getWidth() / 2, map.getHeight() / 2);
        this.camera = new Camera(160, 160, 4);
        this.gameInfo = gameInfo;
        this.state = GameState.Running;
        roundTimeLeft = map.getRoundSeconds()[gameInfo.getRound()];
        map.setupPlayScene(this);
    }

    public Player getPlayer() {
        return player;
    }

    public Map getMap() {
        return map;
    }

    @Override
    public void setup(SceneManager sceneManager) {
    }

    public void breadFound() {
        this.state = GameState.Lost;
    }

    public Vector2 getNearestBrotPos(Vector2 pos) {
        var dist = 1000000000000000000000000f;
        Vector2 nearest = null;
        for (var x = 0; x < map.getWidth(); x++) {
            for (var y = 0; y < map.getHeight(); y++) {
                var field = map.getField(x, y);
                if (field.item == BuildingType.Brot) {
                    var brotPos = Helpers.newVector2(x * 16 + 8, y * 16 + 8);
                    var newDist = Vector2Distance(pos, brotPos);
                    if (newDist < dist) {
                        nearest = brotPos;
                        dist = newDist;
                    }
                }
            }
        }
        return nearest;
    }

    public Enemy getNearestEnemy(Raylib.Vector2 pos) {
        float dist = 10000000000000000000000000000000000f;
        Enemy nearest = null;
        for (var e : enemies) {
            var newDist = Raylib.Vector2Distance(pos, e.getPosition());
            if (newDist < dist) {
                nearest = e;
                dist = newDist;
            }
        }
        return nearest;
    }

    public float getSpawnRate() {
        return map.getRoundSpawnRate()[gameInfo.getRound()];
    }

    @Override
    public void update(SceneManager sm, InputHandle inputHandle) {
        if (this.state == GameState.Won) {
            return;
        }
        if (this.state == GameState.Lost) {
            return;
        }
        if (this.roundTimeLeft <= 0) {
            var round = gameInfo.getRound() + 1;
            if (round < map.getRoundSeconds().length) {
                gameInfo.setMoney(gameInfo.getMoney() + map.getRoundMoney()[round]);
                gameInfo.setRound(round);
                sm.changeScene(new PreGameScene(map, gameInfo));
            } else {
                if (gameInfo.isCompetitive()) {
                    Loadout.addGems(map.getGemReward());
                }
            }
            this.state = GameState.Won;
            return;
        }
        this.roundTimeLeft -= Raylib.GetFrameTime();

        // Update player
        player.update(inputHandle, this.map);

        // Update all entity groups
        projectiles.removeIf(Projectile::update);
        enemies.removeIf(Enemy::update);
        playerBuildings.removeIf(Building::update);
        enemyBuildings.removeIf(Building::update);

        camera.target(player.getPosition());
        camera.handleResize();
        camera.scrollZoom(inputHandle);
    }

    @Override
    public void draw() {
        Raylib.BeginMode2D(camera);
        map.draw();
        player.draw();
        for (var e : enemies) {
            e.draw();
        }
        for (var e : projectiles) {
            e.draw();
        }
        Raylib.EndMode2D();
        Raylib.DrawText("Time: " + Math.round(roundTimeLeft) + "s", 10, 10, 28, Colors.BLACK);
    }

    @Override
    public SceneInterface cloneScene() {
        return new PlayScene(this.map, this.gameInfo);
    }
}
