package src.menu;

import com.raylib.Colors;
import com.raylib.Helpers;
import com.raylib.Raylib;
import src.edit.MapLoader;
import src.game.Camera;
import src.game.Player;
import src.math.RectangleI;
import src.scenes.*;
import src.ui.InputHandle;

public class Menu {
    static Raylib.Texture shopImage = Raylib.LoadTexture("resources/Lobby.png");
    static Raylib.Texture lobbyGrass = Raylib.LoadTexture("resources/LobbyGrass.png");

    // Menu interaction icons
    MenuIcon[] options = {
            new MenuIcon(
                    new RectangleI(210, 70, 32, 32),
                    Raylib.LoadTexture("resources/ShopIcon.png")
            ),
            new MenuIcon(
                    new RectangleI(110, 210, 32, 32),
                    Raylib.LoadTexture("resources/FightIcon.png")
            ),
            new MenuIcon(
                    new RectangleI(290, 147, 32, 32),
                    Raylib.LoadTexture("resources/Loadout.png")
            ),
            new MenuIcon(
                    new RectangleI(110, 130, 32, 32),
                    Raylib.LoadTexture("resources/EditIcon.png")
            )
    };

    Player player = new Player(14, 12);
    Camera camera = new Camera(160, 160, 4);

    // Update menu state
    public void update(SceneManager sm, InputHandle ih) {
        player.updateNoMap(ih);
        camera.target(player.getPosition());
        camera.handleResize();
        camera.scrollZoom(ih);

        for (MenuIcon option : options) {
            option.update(ih, player.getPosition());
        }
        if (options[0].isInteracted()) {
            sm.pushScene(new ShopScene());
        }
        if (options[1].isInteracted()) {
            sm.pushScene(new MapSelectScene((mapName ->
                    sm.changeScene(PreGameScene.gameStart(MapLoader.getMap(mapName), mapName, false))), false)
            );
        }
        if (options[2].isInteracted()) {

        }
        if (options[3].isInteracted()) {
            sm.pushScene(new MapSelectScene((mapName ->
                    sm.changeScene(new EditScene(MapLoader.getMap(mapName), mapName))), true)
            );
        }
    }

    // Draw menu scene
    public void draw() {
        Raylib.BeginMode2D(camera);
        drawInfiniteFields();
        Raylib.DrawTexture(shopImage, 0, 0, Colors.WHITE);
        for (MenuIcon option : options) {
            option.draw();
        }
        player.draw();
        Raylib.EndMode2D();
    }

    // Draw player coordinates for debugging
    private void drawCoordinates() {
        var pos = player.getPosition();
        Raylib.DrawText("x:" + Math.floor(pos.x()) + " y:" + Math.floor(pos.y()), 200, 10, 20, Colors.BLACK);
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
}
