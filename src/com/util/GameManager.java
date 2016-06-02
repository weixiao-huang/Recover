package com.util;

import com.gui.GameBoard;
import com.gui.Piece;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike Huang on 2016/5/26.
 */
public class GameManager extends Group {
    public ArrayList<Integer> arrayList;

    private static final Duration ANIMATION_MOVE_TO_NEAR = Duration.millis(65);

    private volatile boolean movingPiece = false;
    private final Map<Location, Piece> gameGrid;

    private final GameBoard gameBoard;
    private final GridOperator gridOperator;

    public GameManager() {
        this(GridOperator.DEFAULT_GRID_SIZE);
    }

    public GameManager(int gridSize) {
        this.gameGrid = new HashMap<>();


    }

    public void setToolBar(HBox toolbar) {
        gameBoard.setToolBar(toolbar);
    }
}
