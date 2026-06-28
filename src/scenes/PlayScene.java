package src.scenes;

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;
import src.game.*;
import src.math.Vector2I;
import src.ui.*;

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
    ImageUi gemInfo;


    ImageUi lostBanner;
    ImageUi wonBanner;
    Button continueButton;
    Button addToLeaderboardButton;
    AlignLayout layout;

    double roundTimeLeft;


    public PlayScene(Map map, GameInfo gameInfo) {
        this.map = map;
        this.player = new Player(map.getWidth() / 2, map.getHeight() / 2);
        this.camera = new Camera(160, 160, 4);
        this.gameInfo = gameInfo;
        this.state = GameState.Running;
        roundTimeLeft = map.getRoundSeconds()[gameInfo.getRound()];
        map.setupPlayScene(this);

        wonBanner = new ImageUi(UiHelper.textTextureEx("You won!", 44, Colors.BLACK, Colors.GREEN, 2));
        lostBanner = new ImageUi(UiHelper.textTextureEx("You lost!", 44, Colors.BLACK, Colors.RED, 2));
        continueButton = new Button("continue", 28);
        addToLeaderboardButton = new Button("<add to leaderboard>", 28);

        layout = new AlignLayout(1, Align.Middle, new Vector2I(10, 10));
    }

    public Player getPlayer() {
        return player;
    }

    public Map getMap() {
        return map;
    }

    @Override
    public void setup(SceneManager sceneManager) {
        sceneManager.setRootLayout(layout);
    }

    public void breadFound() {
        this.layout.add(lostBanner, Align.Middle);
        this.layout.add(continueButton, Align.Middle);
        this.state = GameState.Lost;
    }

    public Vector2 getNearestBrotPos(Vector2 pos) {
        var dist = 1000000000000000000000000f;
        Vector2 nearest = null;
        for (var x = 0; x < map.getWidth(); x++) {
            for (var y = 0; y < map.getHeight(); y++) {
                var field = map.getField(x, y);
                if (field.building() == BuildingType.Brot) {
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
            continueButton.update(inputHandle);
            if (continueButton.isClicked()) {
                sm.goToMenu();
            }
            if (gameInfo.isCompetitive()) {
                addToLeaderboardButton.update(inputHandle);
                if (addToLeaderboardButton.isClicked()) {
                    sm.changeScene(new AddToLeaderBoardScene(map, gameInfo));
                }
            }
            return;
        }
        if (this.state == GameState.Lost) {
            continueButton.update(inputHandle);
            if (continueButton.isClicked()) {
                sm.goToMenu();
            }
            return;
        }
        if (this.roundTimeLeft <= 0 && this.enemies.isEmpty()) {
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
            layout.clear();
            layout.add(wonBanner, Align.Middle);
            layout.add(continueButton, Align.Middle);
            if (gameInfo.isCompetitive()) {
                gemInfo = new ImageUi(UiHelper.textTexture("You earned " + map.getGemReward() + " gems", 20, Colors.DARKGREEN));
                layout.add(gemInfo, Align.Middle);
            }
            if (gameInfo.isCompetitive()) {
                layout.add(addToLeaderboardButton, Align.Middle);
            }
            return;
        }
        if (this.roundTimeLeft > 0) {
            this.roundTimeLeft -= Raylib.GetFrameTime();
            enemyBuildings.removeIf(Building::update);
        }

        // Update player
        player.update(inputHandle, this.map, camera, this);

        // Update all entity groups
        projectiles.removeIf(Projectile::update);
        enemies.removeIf(Enemy::update);
        playerBuildings.removeIf(Building::update);

        camera.target(player.getPosition());
        camera.handleResize();
        camera.scrollZoom(inputHandle);
    }

    public void addKillMoney(int money) {
        this.gameInfo.setMoney(gameInfo.getMoney() + money);
    }

    @Override
    public void draw() {
        Raylib.BeginMode2D(camera);
        map.draw();
        player.draw(camera);
        for (var e : enemies) {
            e.draw();
        }
        for (var e : projectiles) {
            e.draw();
        }
        Raylib.EndMode2D();
        Raylib.DrawText("Time: " + Math.round(roundTimeLeft) + "s", 10, 10, 28, Colors.BLACK);
        if (!state.equals(GameState.Running)) {
            Raylib.DrawRectangle(0, 0, Raylib.GetScreenWidth(), Raylib.GetScreenHeight(), Raylib.Fade(Colors.GRAY, 0.7f));
        }
        if (state.equals(GameState.Lost)) {
            lostBanner.draw();
            continueButton.draw();
        }
        if (state.equals(GameState.Won)) {
            wonBanner.draw();
            continueButton.draw();
            if (gameInfo.isCompetitive()) {
                addToLeaderboardButton.draw();
                gemInfo.draw();
            }
        }
    }

    @Override
    public SceneInterface cloneScene() {
        return new PlayScene(this.map, this.gameInfo);
    }
}
