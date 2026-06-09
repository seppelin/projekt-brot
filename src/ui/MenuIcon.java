package src.ui;


import com.raylib.Colors;
import com.raylib.Raylib;
import src.math.RectangleI;
import src.math.Vector2I;


/**
 * Beschreiben Sie hier die Klasse MenuIcon.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class MenuIcon{
    private final Raylib.Texture texture;
    private Vector2I position;
    private boolean active;
    
    public MenuIcon(Vector2I pos, Raylib.Texture texture){
        this.texture = texture;
        this.position = pos;
        this.active = false;
    }
    
    public void update (InputHandle Ih, Raylib.Vector2 playerPos){
        var dist = Raylib.Vector2Distance(position.rl(), playerPos);
        active = dist < 100;
    }
    
    public void draw(){
        var color = active ? Colors.YELLOW : Colors.WHITE;
        Raylib.DrawTextureV(texture, position.rl(), color);
    }
}