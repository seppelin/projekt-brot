package src.game;

import com.raylib.Raylib;
import src.ui.BuyItemInterface;
import src.ui.SelItemInterface;

// Types of items that can be placed on fields
public enum BuildingType implements SelItemInterface, BuyItemInterface {
    Brot("brot", "resources/buildings/brot.png", -1),
    Spawner("spawner", "resources/buildings/spawner.png", -1),
    Cannon("cannon", "resources/buildings/cannon.png", 50);

    public static final BuildingType[] MAP_BUILDINGS = {Brot, Spawner};
    public static final BuildingType[] PLAYER_BUILDINGS = {Cannon};
    public final int price;
    public final String name;
    public final Raylib.Texture texture;

    BuildingType(String name, String path, int price) {
        this.price = price;
        this.name = name;
        this.texture = Raylib.LoadTexture(path);
    }

    @Override
    public int getPrice() {
        if (price == -1) {
            throw new RuntimeException("Building not buyable!");
        }
        return price;
    }

    @Override
    public Raylib.Texture getTexture() {
        return this.texture;
    }
}
