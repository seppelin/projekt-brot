package de.developergang;

public class GameMap {
    private final int mapWidth;
    private final int mapHeight;

    // A 3D array
    // Layer 0: Ground, Layer 1: Obstacles, Layer 2: Overhead
    private final MapCell[][] grid;

    public GameMap(int width, int height) {
        this.mapWidth = width;
        this.mapHeight = height;
        grid = new MapCell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grid[i][j] = new MapCell(MapGround.Grass);
            }
        }
    }

    public MapCell[][] getGrid() {
        return grid;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setGround(int width, int height, MapGround ground) {
        grid[width][height].setGround(ground);
    }
}

