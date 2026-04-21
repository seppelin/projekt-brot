package de.developergang;

public record Vec2i(int x, int y) {
    public Vec2i add(int dx, int dy) {
        return new Vec2i(x + dx, y + dy);
    }

    public Vec2i add(Vec2i other) {
        return new Vec2i(x + other.x, y + other.y);
    }

    // Scale for converting tile coords to pixel coords
    public Vec2i scale(int factor) {
        return new Vec2i(x * factor, y * factor);
    }
}