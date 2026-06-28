package src.scenes;

import com.raylib.Colors;
import src.edit.MapLoader;
import src.game.Map;
import src.math.RectangleI;
import src.math.Vector2I;
import src.ui.*;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class MapSettings implements SceneInterface {
    static final int fontSize = 32;
    Map map;
    String name;

    Button save;
    Button comp;

    public MapSettings(Map map, String name) {
        this.map = map;
        this.name = name;
    }

    public static boolean isInt(String str) {
        if (str == null) {
            return false;
        }
        try {
            int value = Integer.parseInt(str);
            return value >= 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isFloat(String str) {
        if (str == null) {
            return false;
        }
        try {
            float value = Float.parseFloat(str);
            return value >= 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void setup(SceneManager sceneManager) {
        var layout = new AlignLayout(1, Align.Start, new Vector2I(10, 10));
        sceneManager.setRootLayout(layout);

        var len = map.getRoundSeconds().length;

        var roundSeconds = new ArrayInput(map.getRoundSeconds(), (i, str)
                -> map.setRoundSeconds(Integer.parseInt(str), i), MapSettings::isInt, 3,
                "Set round length in seconds");
        sceneManager.addUiElement(roundSeconds);
        layout.add(roundSeconds, Align.Start);
        var roundMoney = new ArrayInput(map.getRoundMoney(), (i, str)
                -> map.setRoundMoney(Integer.parseInt(str), i), MapSettings::isInt, 3,
                "set round money reward");
        sceneManager.addUiElement(roundMoney);
        layout.add(roundMoney, Align.Start);
        var roundSpawnRate = new ArrayInput(map.getRoundSpawnRateInt(), (i, str)
                -> map.setRoundSpawnRate(Float.parseFloat(str), i), MapSettings::isFloat, 3,
                "set round spawn rate multiplier");
        sceneManager.addUiElement(roundSpawnRate);
        layout.add(roundSpawnRate, Align.Start);

        var round = new TextInput(2, fontSize, "set number of rounds: " + len);
        round.setValidator(MapSettings::isInt);
        round.setOnEnter(str -> {
            var rounds = Integer.parseInt(str);
            map.setRounds(rounds);
            roundSeconds.changeSize(map.getRoundSeconds());
            roundMoney.changeSize(map.getRoundMoney());
            roundSpawnRate.changeSize(map.getRoundSpawnRateInt());
        });
        sceneManager.addUiElement(round);
        layout.add(round, Align.Start);


        var width = new TextInput(2, fontSize, "set width: " + map.getWidth());
        width.setValidator(MapSettings::isInt);
        width.setOnEnter(str -> map.changeSize(Integer.parseInt(str), map.getHeight()));
        sceneManager.addUiElement(width);
        layout.add(width, Align.Start);

        var height = new TextInput(2, fontSize, "set height: " + map.getHeight());
        height.setValidator(MapSettings::isInt);
        height.setOnEnter(str -> map.changeSize(map.getWidth(), Integer.parseInt(str)));
        sceneManager.addUiElement(height);
        layout.add(height, Align.Start);

        var gems = new TextInput(3, fontSize, "gem reward: " + map.getGemReward());
        gems.setValidator(MapSettings::isInt);
        gems.setOnEnter(str -> map.setGemReward(Integer.parseInt(str)));
        sceneManager.addUiElement(gems);
        layout.add(gems, Align.Start);

        save = new Button(UiHelper.textTexture("Save settings", fontSize, Colors.DARKGREEN));
        sceneManager.addUiElement(save);
        layout.add(save, Align.Start);

        comp = new Button(UiHelper.textTexture("Make map competitive", fontSize, Colors.DARKPURPLE));
        sceneManager.addUiElement(comp);
        layout.add(comp, Align.Start);
    }

    @Override
    public void update(SceneManager sceneManager, InputHandle inputHandle) {
        if (save.isClicked()) {
            MapLoader.saveMap(this.name, this.map);
        }
        if (comp.isClicked()) {
            sceneManager.changeScene(new MakeMapComp(this.map, this.name));
        }
    }

    @Override
    public void draw() {

    }

    @Override
    public SceneInterface cloneScene() {
        return new MapSettings(map, name);
    }

    private class ArrayInput implements LayoutInterface, UiInterface {
        ImageUi label;
        AlignLayout layout;
        ArrayList<TextInput> inputs;
        BiConsumer<Integer, String> onEnter;
        Predicate<String> validator;
        int maxLen;

        public ArrayInput(int[] shadowValues, BiConsumer<Integer, String> onEnter, Predicate<String> validator, int maxLen, String str) {
            this.label = new ImageUi(UiHelper.textTexture(str, fontSize, Colors.DARKBLUE));
            this.layout = new AlignLayout(0, Align.Middle, new Vector2I(10, 10));
            layout.add(label, Align.Start);
            this.inputs = new ArrayList<>();
            this.onEnter = onEnter;
            this.validator = validator;
            this.maxLen = maxLen;
            changeSize(shadowValues);
        }

        public void changeSize(int[] shadowValues) {
            var size = shadowValues.length;
            while (size > inputs.size()) {
                var i = inputs.size();
                var input = new TextInput(maxLen, fontSize, "" + shadowValues[i]);
                input.setValidator(validator);
                input.setOnEnter(name -> onEnter.accept(i, name));

                inputs.add(input);
                layout.add(input, Align.Start);
            }
            while (size < inputs.size()) {
                var input = inputs.removeLast();
                layout.remove(input);
            }
        }

        @Override
        public void setSpace(RectangleI rect) {
            this.layout.setSpace(rect);
        }

        @Override
        public Vector2I minimum() {
            return this.layout.minimum();
        }

        @Override
        public void update(InputHandle inputHandle) {
            for (var input : inputs) {
                input.update(inputHandle);
            }
        }

        @Override
        public void draw() {
            for (var input : inputs) {
                input.draw();
            }
            label.draw();
        }

        @Override
        public float extraSpaceGreed() {
            return 0;
        }
    }
}
