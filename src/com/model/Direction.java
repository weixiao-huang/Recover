package com.model;

import javafx.scene.input.KeyCode;

/**
 * Created by Mike Huang on 2016/5/28.
 */
public enum Direction {
    UP(-1, 0), RIGHT(0, 1), DOWN(1, 0), LEFT(0, -1);

    private final int x;
    private final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Direction{x = " + x + ", y = " + y + '}' + name();
    }

    public static Direction valueFor(KeyCode keyCode) {
        return valueOf(keyCode.name());
    }
}
