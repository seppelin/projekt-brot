package src.game;

import com.raylib.Raylib;
import src.ui.SelItemInterface;

// Player skin types
public enum SkinType implements SelItemInterface {
    Default("default", 0),
    Salami("salami", 150),
    Veganer("veganer", 200),
    Rainer("rainer", 250),
    KarateRainer("karateRainer", 300),
    ;

    final Raylib.Texture animTexture;
    final Raylib.Texture texture;
    final int price;

    SkinType(String texture, int price) {
        this.texture = Raylib.LoadTexture("resources/skins/" + texture + ".png");
        this.animTexture = Raylib.LoadTexture("resources/skins/" + texture + "_anim.png");
        this.price = price;
    }

    public Raylib.Texture getAnimTexture() {
        return animTexture;
    }

    public Raylib.Texture getTexture() {
        return texture;
    }

    public int getPrice() {
        return price;
    }
}
