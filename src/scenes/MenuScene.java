package src.scenes;

import com.raylib.Raylib;
import com.raylib.Colors;
import src.math.Vector2I;
import src.ui.Button;
import src.ui.InputHandle;
import src.game.Player;
import src.game.Camera;
import src.game.Map;
import src.game.FieldType;


public class MenuScene implements SceneInterface {
    static Raylib.Texture shopimage = Raylib.LoadTexture("resources/lobby.jpeg");
    
    Button playButton;
    Button editButton;
    Button exitButton;
    Button shopButton;
    Player player;
    Camera camera;
    Map map;
    

    public MenuScene() {

        playButton = new Button(new Vector2I(20, 20), "Play", 32, Colors.BLACK, Colors.BLANK);
        editButton = new Button(new Vector2I(20, 60), "Edit", 32, Colors.BLACK, Colors.BLANK);
        exitButton = new Button(new Vector2I(20, 100), "Exit", 32, Colors.BLACK, Colors.BLANK);
        shopButton = new Button(new Vector2I(20, 140), "Shop", 32,Colors.RED, Colors.BLANK);
        this.player = new Player(20, 20);
        this.camera = new Camera(160, 160, 4);
        this.map = new Map(140, 140, FieldType.GRASSDARK);
    }

    @Override
    public void setup(SceneManager sceneManager) {
        sceneManager.addUiElement(playButton);
        sceneManager.addUiElement(editButton);
        sceneManager.addUiElement(exitButton);
        sceneManager.addUiElement(shopButton);
    }

    @Override
    public void update(SceneManager sm, InputHandle inputHandle) {
        
        
        if (playButton.isClicked()) {
            sm.changeScene(new PlayScene());
        }

        if (editButton.isClicked()) {
            sm.changeScene(new EditScene());
        }

        if (exitButton.isClicked()) {
            sm.quitGame();
        }
        
        if (shopButton.isClicked()){
            sm.changeScene(new ShopScene());
        }
        player.updateNoMap(inputHandle);
        camera.target(player.getPosition());
        camera.handleResize();
    }

    @Override
    public void draw() {
        Raylib.BeginMode2D(camera);
        map.draw();
        Raylib.DrawTexture(shopimage, 100, 100, Colors.WHITE);

        player.draw();
        Raylib.EndMode2D();
        var pos = player.getPosition();
        Raylib.DrawText("x:" + Math.round(pos.x()) + " y:" + Math.round(pos.y()), 200, 10, 20, Colors.BLACK);
    }
}
