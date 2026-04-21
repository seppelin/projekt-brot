package de.developergang;

public class MapCell {
    private MapGround ground;

    public MapCell(MapGround ground) {
        this.ground = ground;
    }

    public MapGround getGround() {
        return ground;
    }

    public void setGround(MapGround ground) {
        this.ground = ground;
    }
}
