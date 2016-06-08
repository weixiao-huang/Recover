package com.util;

import com.gui.GameBoard;
import com.gui.Piece;
import com.model.Direction;
import com.model.GameModel;
import com.model.Location;
import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.util.*;

/**
 * Created by Mike Huang on 2016/5/26.
 */
public class GameManager extends Group {
    private int gridSize;
    private List<Piece> pieceList;

    private static final Duration ANIMATION_MOVE_TO_NEAR = Duration.millis(65);

    private volatile boolean movingPiece = false;
    private final Map<Location, Piece> gameGrid;
    private final List<Location> locations = new ArrayList<>();

    private GameBoard gameBoard;
    private GridOperator gridOperator;

    public GameManager() {
        this(GameModel.DEFAULT_GRID_SIZE);
    }

    public GameManager(int gridSize) {
        this.gridSize = gridSize;
        this.gameGrid = new HashMap<>();

        gridOperator = new GridOperator(gridSize);
        gameBoard = new GameBoard(gridOperator);
        this.getChildren().add(gameBoard);

        gameBoard.clearGameProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                initializeGameGrid();
            }
        }));

        gameBoard.resetGameProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                startGame();
            }
        }));

        gameBoard.changeSizeProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.intValue() != oldValue.intValue()) {
                this.gridSize = newValue.intValue();
                initializeGameGrid();
                startGame();
            }
        }));

        initializeGameGrid();
        startGame();


    }

    private void initializeGameGrid() {
        gameGrid.clear();
        locations.clear();
        gridOperator.traverseGrid((x, y) -> {
            Location loc = new Location(x, y);
            locations.add(loc);
            gameGrid.put(loc, null);
            return 0;
        });
    }

    private void startGame() {
        pieceList = GameModel.REFRESH_MODEL(gridSize);
        pieceList.forEach(t -> gameGrid.put(t.getLocation(), t));

        drawPiecesInGameGrid();
        gameBoard.startGame();
    }

    private void drawPiecesInGameGrid() {
        gameGrid.values().stream().filter(Objects::nonNull).forEach(
                t -> {
                    if (t.getValue() == 0) return;
                    gameBoard.addPiece(t);
                }
        );
    }

    private void movePiece(Direction direction) {
        ParallelTransition parallelTransition = new ParallelTransition();
        int index0 = -1;
        int index = -1;
        for (int i = 0; i < pieceList.size(); i++) {
            if (pieceList.get(i).getValue() == 0) {
                index0 = i;
                break;
            }
        }
        Piece piece0 = pieceList.get(index0);
        Location nextLoc = piece0.getLocation().offsetM(direction);
        if (isLocLegal(nextLoc, gridSize)) {
            for (int i = 0; i < pieceList.size(); i++) {
                if (pieceList.get(i).getLocation().equals(nextLoc)) {
                    index = i;
                }
            }
            Piece piece = pieceList.get(index);

            Timeline timeline = animatePiece(piece, piece0.getLocation());
            parallelTransition.getChildren().add(timeline);
            parallelTransition.play();

            gameBoard.addStep(1);

            Collections.swap(pieceList, index0, index);
            Location tmpLoc = piece.getLocation();
            piece.setLocation(piece0.getLocation());
            piece0.setLocation(tmpLoc);

            List<Integer> numList = new ArrayList<>();
            for(Piece piece1 : pieceList) {
                numList.add(piece1.getValue());
            }

            if (numList.equals(GameModel.SOL_NUM_LIST(gridSize))){
                gameBoard.setGameWin(true);
            }

            System.out.println(numList);
        }
    }

    private boolean isLocLegal(Location location, int gridSize) {
        return location.getX() >= 0 && location.getX() < gridSize &&
                location.getY() >= 0 && location.getY() < gridSize;
    }

    private Timeline animatePiece(Piece piece, Location newLoc) {
        Timeline timeline = new Timeline();
        KeyValue kvX = new KeyValue(piece.layoutXProperty(),
                newLoc.getLayoutX(GameBoard.CELL_SIZE) - (piece.getMinHeight() / 2),
                Interpolator.EASE_OUT);
        KeyFrame kfX = new KeyFrame(ANIMATION_MOVE_TO_NEAR, kvX);
        timeline.getKeyFrames().add(kfX);

        KeyValue kvY = new KeyValue(piece.layoutYProperty(),
                newLoc.getLayoutY(GameBoard.CELL_SIZE) - (piece.getMinHeight() / 2),
                Interpolator.EASE_OUT);
        KeyFrame kfY = new KeyFrame(ANIMATION_MOVE_TO_NEAR, kvY);
        timeline.getKeyFrames().add(kfY);

        return timeline;
    }

    public void setScale(double scale) {
        this.setScaleX(scale);
        this.setScaleY(scale);
    }

    public void setToolBar(HBox toolbar) {
        gameBoard.setToolBar(toolbar);
    }

    public void move(Direction direction) {
        movePiece(direction);
    }

    public void changeSize() {
        gameBoard.changeSize();
    }

//    public void saveSession() {
//        gameBoard.saveSession();
//    }

//    public void restoreSession() {
//
//    }

    public void pauseGame() {
        gameBoard.pauseGame();

    }

    public void tryAgain() {
        gameBoard.tryAgain();
    }

    public void aboutGame() {
        gameBoard.aboutGame();
    }

    public void quitGame() {
        gameBoard.quitGame();
    }

}
