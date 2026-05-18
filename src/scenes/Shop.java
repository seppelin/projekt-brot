package src.scenes;
import com.raylib.Raylib;
import com.raylib.Colors;

public class Shop implements Scene{
    public Shop(){
        
    }
    
    public void setup(SceneManager sceneManager) {}
    public void update (SceneManager sm){}
    public void draw(){
        Raylib.DrawRectangle(400, 60, 10, 10, Colors.BLACK);
    }
}