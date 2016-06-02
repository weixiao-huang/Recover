package com.util;

/**
 * Created by Mike Huang on 2016/5/28.
 */
public class Location {
    private final int x;
    private final int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Location offset(Direction direction) {
        return new Location(x + direction.getX(), y + direction.getY());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.x;
        hash = 97 * hash + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Location location = (Location) obj;
        return this.x == location.x && this.y == location.y;
    }

    public double getLayoutX(int CELL_SIZE) {
        return (y * CELL_SIZE) + CELL_SIZE / 2;
    }

    public double getLayoutY(int CELL_SIZE) {
        return (x * CELL_SIZE) + CELL_SIZE / 2;
    }

    public boolean isValidFor(int gridSize) {
        return x >= 0 && x < gridSize && y >= 0 && y < gridSize;
    }
}
