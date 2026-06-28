package src.scenes;

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;
import src.game.Camera;
import src.game.Player;
import src.math.RectangleI;
import src.ui.InputHandle;
import src.ui.UiHelper;

import java.util.function.Consumer;

// Scene showing the lobby/menu area
public class MenuScene implements SceneInterface {
    static Raylib.Texture shopImage = Raylib.LoadTexture("resources/Lobby.png");
    static Raylib.Texture lobbyGrass = Raylib.LoadTexture("resources/LobbyGrass.png");

    // Menu interaction icons
    MenuIcon[] options;

    Player player;
    Camera camera;

    @Override
    public void setup(SceneManager sceneManager) {
        player = new Player(14, 12);
        camera = new Camera(160, 160, 4);

        options = new MenuIcon[]{
                new MenuIcon(
                        new RectangleI(210, 70, 32, 32),
                        Raylib.LoadTexture("resources/ShopIcon.png"),
                        m -> sceneManager.pushScene(new ShopScene())
                ),
                new MenuIcon(
                        new RectangleI(110, 210, 32, 32),
                        Raylib.LoadTexture("resources/FightIcon.png"),
                        m -> sceneManager.pushScene(new ChoosePlayMap())
                ),
                new MenuIcon(
                        new RectangleI(290, 147, 32, 32),
                        Raylib.LoadTexture("resources/Loadout.png"),
                        m -> sceneManager.pushScene(new LoadoutScene())
                ),
                new MenuIcon(
                        new RectangleI(110, 130, 32, 32),
                        Raylib.LoadTexture("resources/EditIcon.png"),
                        m -> sceneManager.pushScene(new ChooseEditMap())
                ),
                new MenuIcon(
                        new RectangleI(252, 242, 32, 32),
                        Raylib.LoadTexture("resources/Leaderboard.png"),
                        m -> sceneManager.pushScene(new LeaderboardScene())
                )
        };
    }

    @Override
    public void update(SceneManager sceneManager, InputHandle ih) {
        player.updateNoMap(ih);
        camera.target(player.getPosition());
        camera.handleResize();
        camera.scrollZoom(ih);

        for (var o : options) {
            o.update(ih, player.getPosition());
        }
    }

    @Override
    public void draw() {
        Raylib.BeginMode2D(camera);
        drawInfiniteFields();
        Raylib.DrawTexture(shopImage, 0, 0, Colors.WHITE);
        for (MenuIcon option : options) {
            option.draw();
        }
        player.draw(camera);
        Raylib.EndMode2D();
    }

    // Draw infinitely tiled background
    private void drawInfiniteFields() {
        var start = Raylib.GetScreenToWorld2D(Helpers.newVector2(0, 0), camera);
        var end = Raylib.GetScreenToWorld2D(Helpers.newVector2(Raylib.GetScreenWidth(), Raylib.GetScreenHeight()), camera);
        for (int x = (int) (start.x() / 16) - 1; x < (end.x() / 16); x++) {
            for (int y = (int) (start.y() / 16) - 1; y < (end.y() / 16); y++) {
                Raylib.DrawTexture(lobbyGrass, x * 16, y * 16, Colors.WHITE);
            }
        }
    }

    @Override
    public SceneInterface cloneScene() {
        return new MenuScene();
    }

    // Interactive icon in the menu
    private static class MenuIcon {
        private final Raylib.Texture texture;
        private final RectangleI rect;
        private final Consumer<MenuIcon> onClick;
        private boolean active;


        public MenuIcon(RectangleI rect, Raylib.Texture texture, Consumer<MenuIcon> onCLick) {
            this.texture = texture;
            this.rect = rect;
            this.active = false;
            this.onClick = onCLick;
        }

        // Update icon state based on player proximity
        public void update(InputHandle ih, Raylib.Vector2 playerPos) {
            var dist = Raylib.Vector2Distance(rect.middle().rl(), playerPos);
            active = dist < 50;
            if (active && ih.tryTakeKeyBoard()) {
                if (Raylib.IsKeyPressed(Raylib.KEY_E)) {
                    onClick.accept(this);
                }
            }
        }

        // Draw icon with highlight if active
        public void draw() {
            var color = active ? Colors.YELLOW : Colors.WHITE;
            UiHelper.drawTextureRect(texture, rect.rl(), color);
            if (active) {
                Raylib.DrawText("Press E to enter", rect.pos.x, rect.pos.y - 10, 6, Colors.BLACK);
            }
        }
    }
}
