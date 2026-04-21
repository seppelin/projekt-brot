package de.developergang;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.StartupScene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends GameApplication {

    static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Brot-Defense");
        settings.setSceneFactory(new SceneFactory() {
            @NotNull
            @Override
            public StartupScene newStartup(int width, int height) {
                return new MyStartupScene(width, height);
            }
        });
    }

    public static class MyStartupScene extends StartupScene {
        public MyStartupScene(int appWidth, int appHeight) {
            super(appWidth, appHeight);

            Rectangle bg = new Rectangle(appWidth, appHeight);

            Text textCompanyName = new Text("Projekt Brot");
            textCompanyName.setFill(Color.WHITE);
            textCompanyName.setFont(Font.font(64));

            getContentRoot().getChildren().addAll(new StackPane(bg, textCompanyName));
        }
    }

    @Override
    protected void initUI() {
        Text text = new Text("Hello World");
        FXGL.addUINode(text, 100, 100);
    }

    @Override
    protected void initGame() {
        GameMap map = new GameMap(20, 20);

        for (int width = 0; width < map.getMapWidth(); width++) {
            for (int height = 0; height < map.getMapHeight(); height++) {
                Rectangle rect = new Rectangle(32, 32, Color.GREEN);
                Entity cell = FXGL.entityBuilder()
                        .at(width * 32, height * 32)
                        .view(rect)
                        .buildAndAttach();
                int finalWidth = width;
                int finalHeight = height;
                cell.getViewComponent().addOnClickHandler(_ -> {
                    rect.setFill(Color.GRAY);
                    map.setGround(finalWidth, finalHeight, MapGround.Stone);
                });
            }
        }
    }

    @Override
    protected void initInput() {

        Input input = FXGL.getInput();

        input.addAction(new UserAction("Print Line") {
            @Override
            protected void onActionBegin() {
                System.out.println("begin");
            }

            @Override
            protected void onActionEnd() {
                System.out.println("end");
            }
        }, KeyCode.F);
    }
}