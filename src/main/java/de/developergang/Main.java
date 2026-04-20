package de.developergang;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends GameApplication {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Brot-Defense");
    }
}