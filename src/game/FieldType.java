package game;


import com.raylib.Raylib;

public record FieldType(String name, boolean walkable, Raylib.Texture texture) {
    public static FieldType GRASS = new FieldType("grass", true, Raylib.LoadTexture("resources/grass.png"));
    public static FieldType WATER = new FieldType("grass", false, Raylib.LoadTexture("resources/water.png"));
}
