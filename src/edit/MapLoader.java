package src.edit;

import src.game.Map;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class MapLoader {
    // Get list of all available map names
    public static ArrayList<String> getMapList() {
        var names = new ArrayList<String>();
        var mapDir = Paths.get("resources/maps");

        try (Stream<Path> stream = Files.list(mapDir)) {
            stream.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".mapdata"))
                    .forEach(p -> names.add(p.getFileName().toString().replace(".mapdata", "")));
        } catch (IOException e) {
            System.err.println("Error loading files!");
        }

        return names;
    }

    // Load a map from file
    public static Map getMap(String mapName) {
        Map map;
        try (var in = new ObjectInputStream(new FileInputStream("resources/maps/" + mapName + ".mapdata"))) {
            map = (Map) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    // Save a map to file
    public static void saveMap(String mapName, Map map) {
        try (var out = new ObjectOutputStream(new FileOutputStream("resources/maps/" + mapName + ".mapdata"))) {
            out.writeObject(map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
