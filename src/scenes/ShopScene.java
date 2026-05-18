package src.scenes;

import com.raylib.Colors;
import com.raylib.Raylib;
import src.game.Camera;
import src.game.Map;
import src.game.Player;
import src.math.Vector2I;
import src.ui.Button;
import src.ui.InputHandle;


public class ShopScene implements SceneInterface {
    Button MenuButton;
    Map Shopmap;
    Player player;
    Camera camera;
    Raylib.Texture tex;

    public ShopScene() {
        MenuButton = new Button(new Vector2I(20, 20), "Menu", 32, Colors.BLACK, Colors.BLANK);
        Shopmap = new Map(20, 20);
        player = new Player(Shopmap.getWidth() / 2, Shopmap.getHeight() / 4);
        camera = new Camera(100, 100, 3);
        tex = Raylib.LoadTexture("resources/Schanze.jpg");

    }

    public void setup(SceneManager sceneManager) {
        sceneManager.addUiElement(MenuButton);
    }

    @Override
    public void update(SceneManager sceneManager, InputHandle inputHandle) {
        if (MenuButton.isClicked()) {
            sceneManager.changeScene(new MenuScene());
        }
        if (Raylib.IsKeyPressed(69)) {
            sceneManager.changeScene(new Shop());
        }
        //Shop Fenster öffnen
        Shopmap.update(inputHandle, camera, (a, b) -> {
        });
        player.update(inputHandle, Shopmap);
        camera.target(player.getPosition());
        camera.handleResize();
        camera.scrollZoom(inputHandle);
    }

    public void draw() {

        Raylib.BeginMode2D(camera);
        Raylib.DrawTexture(tex, 0, 0, Colors.WHITE);
        //Shopmap.draw();
        player.draw();
        Raylib.EndMode2D();

    }
}
