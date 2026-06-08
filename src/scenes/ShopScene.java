package src.scenes;

import com.raylib.Helpers;
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
    Raylib.Texture tex;
    Button Buy;

    public ShopScene() {
        MenuButton = new Button(new Vector2I(20, 20), "Menu", 32, Colors.BLACK, Colors.BLANK);
        Buy = new Button (new Vector2I((int)(0.1 * Raylib.GetScreenWidth()),(int)(0.31 * Raylib.GetScreenHeight())), "Kaufen", 32, Colors.BLACK, Colors.BLANK);
        
        
        tex = Raylib.LoadTexture("resources/Shop.png");

    }

    public void setup(SceneManager sceneManager) {
        sceneManager.addUiElement(MenuButton);
        sceneManager.addUiElement(Buy);
    }

    @Override
    public void update(SceneManager sceneManager, InputHandle inputHandle) {
        if (MenuButton.isClicked()) {
            sceneManager.changeScene(new MenuScene());
        }
        Buy.getRect().pos = new Vector2I((int)(0.12 * Raylib.GetScreenWidth()),(int)(0.31 * Raylib.GetScreenHeight()));
        Buy.getRect().size = new Vector2I((int)(0.05 * Raylib.GetScreenWidth()),(int)(0.02 * Raylib.GetScreenHeight()));
        
        //Shop Fenster öffnen
    }

    public void draw() {
    
        Raylib.DrawTexture(tex, 0, 0, Colors.WHITE);
        //Shopmap.draw();   
        Raylib.DrawTexturePro(tex,
            Helpers.newRectangle(0, 0, tex.width(), tex.height()),
            Helpers.newRectangle(0, 0, Raylib.GetScreenWidth(), Raylib.GetScreenHeight()),
            Helpers.newVector2(0, 0),
            0,
            Colors.WHITE 
        );
    }
}
