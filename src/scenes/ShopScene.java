package src.scenes;

import com.raylib.Helpers;
import com.raylib.Raylib;
import src.math.Vector2I;
import src.shop.ShopUi;
import src.ui.Align;
import src.ui.AlignLayout;
import src.ui.InputHandle;

// Shop UI scene
public class ShopScene implements SceneInterface {
    Raylib.Color shopBackground = Helpers.newColor(160, 110, 74, 255);
    ShopUi shopUi = new ShopUi();

    public ShopScene() {
    }

    @Override
    public void setup(SceneManager sceneManager) {
        var layout = new AlignLayout(0, Align.Middle, new Vector2I(0, 0));
        layout.add(shopUi, Align.Middle);
        sceneManager.setRootLayout(layout);
        sceneManager.addUiElement(shopUi);
    }

    @Override
    public void update(SceneManager sceneManager, InputHandle inputHandle) {
    }

    @Override
    public void draw() {
        Raylib.DrawRectangle(0, 0, Raylib.GetScreenWidth(), Raylib.GetScreenHeight(), shopBackground);
    }

    @Override
    public SceneInterface cloneScene() {
        return new ShopScene();
    }
}
