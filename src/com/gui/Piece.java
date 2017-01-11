package com.gui;

import com.model.Location;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

/**
 * Created by Mike Huang on 2016/5/26.
 */
public class Piece extends Label {
    private Integer value;
    private Location location;

    public Piece(Integer value) {
        final int cellSize = GameBoard.CELL_SIZE - 13;
        setMinSize(cellSize, cellSize);
        setMaxSize(cellSize, cellSize);
        setPrefSize(cellSize, cellSize);
        setAlignment(Pos.CENTER);

        this.value = value;

        setText(value.toString());
        getStyleClass().addAll("game-label", "game-piece-" + value, "game-piece-unit");
    }

    public Integer getValue() {
        return value;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
