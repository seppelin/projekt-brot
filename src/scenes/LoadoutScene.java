package src.scenes;

import com.raylib.Colors;
import src.game.Loadout;
import src.game.SkinType;
import src.math.Vector2I;
import src.ui.*;

import java.util.Arrays;

public class LoadoutScene implements SceneInterface {
    Selector skins;

    @Override
    public void setup(SceneManager sceneManager) {
        var layout = new AlignLayout(0, Align.Start, new Vector2I(10, 10));
        sceneManager.setRootLayout(layout);

        var label = new ImageUi(UiHelper.textTexture("Skins: ", 28, Colors.DARKBLUE));
        sceneManager.addUiElement(label);
        layout.add(label, Align.Start);

        skins = new Selector(new Vector2I(0, 0), 5, Loadout.getSkins(), 4);
        skins.setSelected(Arrays.asList(Loadout.getSkins()).indexOf(Loadout.getCurrentSkin()));
        layout.add(skins, Align.Start);
        sceneManager.addUiElement(skins);
    }

    @Override
    public void update(SceneManager sceneManager, InputHandle inputHandle) {
        if (!skins.getSelected().equals(Loadout.getCurrentSkin())) {
            Loadout.setSkin((SkinType) skins.getSelected());
        }
    }

    @Override
    public void draw() {

    }

    @Override
    public SceneInterface cloneScene() {
        return new LoadoutScene();
    }
}
