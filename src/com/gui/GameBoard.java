package com.gui;

import com.util.GridOperator;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by Mike Huang on 2016/5/27.
 */
public class GameBoard extends Group {
    public static final int CELL_SIZE = 128;
    private static final int BORDER_WIDTH = (14 + 2) >> 1;
    private static final int TOP_HEIGHT = 92;
    public static final int GAP_HEIGHT = 50;
    public static final int TOOLBAR_HEIGHT = 80;

    private final IntegerProperty gameStepProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty gameSizeProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty gameBestProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty gameMovePoints = new SimpleIntegerProperty(0);
    private final BooleanProperty gameWonProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty gameOverProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty gameAboutProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty gamePauseProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty gameTryAgainProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty gameSaveProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty gameLoadProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty gameQuitProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty overlayOnProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty resetGame = new SimpleBooleanProperty(false);
    private final BooleanProperty clearGame = new SimpleBooleanProperty(false);
    private final BooleanProperty loadGame = new SimpleBooleanProperty(false);
    private final BooleanProperty saveGame = new SimpleBooleanProperty(false);

    private LocalTime time;
    private Timeline timer;
    private final StringProperty clock = new SimpleStringProperty("00:00:00");
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault());

    private final VBox vBox = new VBox(0);
    private final Group gridGroup = new Group();

    private final HBox hTop = new HBox(0);
    private final VBox vStep = new VBox(-5);
    private final Label lblStep = new Label("0");
    private final Label lblBest = new Label("0");
    private final Label lblPoints = new Label();
    private final HBox toolBar = new HBox();

    private final HBox overlay = new HBox();
    private final VBox overlayTxt = new VBox(10);
    private final Label overlayTitle = new Label();
    private final Label overlaySubTitle = new Label();
    private final HBox overlayBtns = new HBox();
    private final Button btContinue = new Button("Continue");
    private final Button btTry = new Button("Try again");
    private final Button btContinueNo = new Button("No, Continue");
    private final Button btSave = new Button("Save");
    private final Button btLoad = new Button("Load");
    private final Button btQuit = new Button("Quit");

    private final Label lblTime=new Label();
    private Timeline timerPause;

    private final int gridWidth;
    private final GridOperator gridOperator;

    public GameBoard(GridOperator gridOperator) {
        this.gridOperator = gridOperator;
        gridWidth = CELL_SIZE * gridOperator.getGridSize() + (BORDER_WIDTH << 1);

        createStep();
        createGrid();

        initialGameProperties();
    }

    private void createStep() {
        Label title = new Label("Recover");
        title.getStyleClass().addAll("game-label", "game-title");
        Label subTitle = new Label("FX");
        subTitle.getStyleClass().addAll("game-label", "game-subtitle");

        // Set fill the HBox
        HBox hFill = new HBox();
        HBox.setHgrow(hFill, Priority.ALWAYS);
        hFill.setAlignment(Pos.CENTER);

        VBox vSteps = new VBox();
        HBox hSteps = new HBox(5);

        vStep.setAlignment(Pos.CENTER);
        vStep.getStyleClass().add("game-vbox");
        Label stepTitle = new Label("STEPS");
        stepTitle.getStyleClass().addAll("game-label", "game-stepTitle");
        lblStep.getStyleClass().addAll("game-label", "game-step");
        lblStep.textProperty().bind(gameStepProperty.asString());
        vStep.getChildren().addAll(stepTitle, lblStep);

        VBox vRecord = new VBox(-5);
        vRecord.setAlignment(Pos.CENTER);
        vRecord.getStyleClass().add("game-vbox");
        Label bestTitle = new Label("BEST");
        bestTitle.getStyleClass().addAll("game-label", "game-stepTitle");
        lblBest.getStyleClass().addAll("game-label", "game-step");
        lblBest.textProperty().bind(gameBestProperty.asString());
        vRecord.getChildren().addAll(bestTitle, lblBest);

        hSteps.getChildren().addAll(vStep, vRecord);
        VBox vFill = new VBox();
        VBox.setVgrow(vFill, Priority.ALWAYS);
        vSteps.getChildren().addAll(hSteps, vFill);

        hTop.getChildren().addAll(title, subTitle, hFill, vSteps);
        hTop.setMinSize(gridWidth, TOP_HEIGHT);
        hTop.setPrefSize(gridWidth, TOP_HEIGHT);
        hTop.setMaxSize(gridWidth, TOP_HEIGHT);

        HBox hTime = new HBox();
        hTime.setMinSize(gridWidth, GAP_HEIGHT);
        hTime.setAlignment(Pos.BOTTOM_RIGHT);
        lblTime.getStyleClass().addAll("game-label", "game-time");
        lblTime.textProperty().bind(clock);

        timer = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            clock.set(LocalTime.now().minusNanos(time.toNanoOfDay()).format(fmt));
        }), new KeyFrame(Duration.seconds(1)));
        timer.setCycleCount(Animation.INDEFINITE);
        hTime.getChildren().add(lblTime);


        lblPoints.getStyleClass().addAll("game-label", "game-points");
        lblPoints.setAlignment(Pos.CENTER);
        lblPoints.setMinWidth(100);

        vBox.getChildren().addAll(hTop, hTime);
        this.getChildren().addAll(lblPoints, vBox);
    }

    private void createGrid() {
        // Draw Grid Group
        gridOperator.traverseGrid((i, j) -> {
            gridGroup.getChildren().add(createCell(i, j));
            return 0;
        });

        gridGroup.getStyleClass().add("game-grid");
        gridGroup.setManaged(false);
        gridGroup.setLayoutX(BORDER_WIDTH);
        gridGroup.setLayoutY(BORDER_WIDTH);


        HBox hBottom = new HBox();
        hBottom.getStyleClass().add("game-backGrid");
        hBottom.setMinSize(gridWidth, gridWidth);
        hBottom.setPrefSize(gridWidth, gridWidth);
        hBottom.setMaxSize(gridWidth, gridWidth);

        Rectangle rectangle = new Rectangle(gridWidth, gridWidth);
        hBottom.setClip(rectangle);
        hBottom.getChildren().add(gridGroup);

        vBox.getChildren().add(hBottom);

        // toolBar
        HBox hPadding = new HBox();
        hPadding.setMinSize(gridWidth, TOOLBAR_HEIGHT);
        hPadding.setPrefSize(gridWidth, TOOLBAR_HEIGHT);
        hPadding.setMaxSize(gridWidth, TOOLBAR_HEIGHT);

        toolBar.setAlignment(Pos.CENTER);
        toolBar.getStyleClass().add("game-backGrid");
        toolBar.setMinSize(gridWidth, TOOLBAR_HEIGHT);
        toolBar.setPrefSize(gridWidth, TOOLBAR_HEIGHT);
        toolBar.setMaxSize(gridWidth, TOOLBAR_HEIGHT);

        vBox.getChildren().add(hPadding);
        vBox.getChildren().add(toolBar);
    }

    private Rectangle createCell(int i, int j) {
        final double arcSize = CELL_SIZE / 6.;
        Rectangle cell = new Rectangle(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        // Default Style Without CSS
        cell.setFill(Color.WHITE);
        cell.setStroke(Color.GRAY);
        cell.setArcHeight(arcSize);
        cell.setArcWidth(arcSize);
        cell.getStyleClass().add("game-grid-cell");
        return cell;
    }

    public void setToolBar(HBox toolBar) {
        toolBar.disableProperty().bind(overlayOnProperty);
        toolBar.spacingProperty().bind(Bindings.divide(vBox.widthProperty(), 10));
        this.toolBar.getChildren().add(toolBar);
    }

    public void addPiece(Piece piece) {
        double layoutX = piece.getLocation().getLayoutX(CELL_SIZE)
                         -(piece.getMinWidth() / 2);
        double layoutY = piece.getLocation().getLayoutY(CELL_SIZE)
                         -(piece.getMinHeight() / 2);
        piece.setLayoutX(layoutX);
        piece.setLayoutY(layoutY);
        gridGroup.getChildren().add(piece);
    }

    public void startGame() {

        time = LocalTime.now();
        timer.playFromStart();
    }

    public BooleanProperty resetGameProperty() {
        return resetGame;
    }

    public BooleanProperty clearGameProperty() {
        return clearGame;
    }

    public boolean saveSession() {
        if (!gameSaveProperty.get()) {
            gameSaveProperty.set(true);
        }
        return true;
    }

    public void addStep(int num) {
        gameStepProperty.set(gameStepProperty.get() + num);
    }

    public void changeSize() {

    }

    private void continueGame() {
        timerPause.stop();
        overlayOnProperty.set(false);
        gamePauseProperty.set(false);
        gameTryAgainProperty.set(false);
        gameSaveProperty.set(false);
        gameLoadProperty.set(false);
        gameAboutProperty.set(false);
        gameQuitProperty.set(false);
        timer.play();
    }

    private void setOverlayBtn(Button btn, Runnable clickAction) {
        btn.getStyleClass().addAll("game-button");
        btn.setOnMouseClicked(e -> clickAction.run());
        btn.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER) || e.getCode().equals(KeyCode.SPACE))
                clickAction.run();
        });
    }

    private void setOverlayBtns() {
        setOverlayBtn(btTry, new Runnable() {
            @Override
            public void run() {
                btnTryAgain();
            }
        });
        setOverlayBtn(btContinue, new Runnable() {
            @Override
            public void run() {
                continueGame();
            }
        });
        setOverlayBtn(btContinueNo, new Runnable() {
            @Override
            public void run() {
                continueGame();
            }
        });
        setOverlayBtn(btSave, new Runnable() {
            @Override
            public void run() {
                saveGame.set(true);
            }
        });
        setOverlayBtn(btLoad, new Runnable() {
            @Override
            public void run() {
                loadGame.set(true);
            }
        });
        setOverlayBtn(btQuit, new Runnable() {
            @Override
            public void run() {
                quit();
            }
        });
    }

    private void initialListener() {
        gameWonProperty.addListener(wonListener);
        gamePauseProperty.addListener(new Overlay("Game Paused", "",
                btContinue, null, "game-overlay-pause", "game-label-pause", false));
        gameTryAgainProperty.addListener(new Overlay("Try Again?", "Current game will be abandoned",
                btTry, btContinue, "game-overlay-pause", "game-label-pause", true));
        gameSaveProperty.addListener(new Overlay("Save Game?", "Previous save data will be overwritten",
                btSave, btContinue, "game-overlay-pause", "game-label-pause", true));
        gameLoadProperty.addListener(new Overlay("Load Game?", "Current game will be abandoned",
                btLoad, btContinue, "game-overlay-pause", "game-label-pause", true));
        gameQuitProperty.addListener(new Overlay("Quit Game", "Current Game Will be Abandoned",
                btQuit, btContinueNo, "game-overlay-pause", "game-label-quit", true));

        gameAboutProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                timer.stop();
                timerPause.play();
                overlay.getStyleClass().setAll("game-overlay", "game-overlay-quit");
                TextFlow textFlow = new TextFlow();
                textFlow.setTextAlignment(TextAlignment.CENTER);
                textFlow.setPadding(new Insets(10, 0, 0, 0));
                textFlow.setMinSize(gridWidth, gridWidth);
                textFlow.setPrefSize(gridWidth, gridWidth);
                textFlow.setMaxSize(gridWidth, gridWidth);
                textFlow.setPrefSize(BASELINE_OFFSET_SAME_AS_HEIGHT, BASELINE_OFFSET_SAME_AS_HEIGHT);
                Text t0 = new Text("Recover FX Game \n" +
                        "JavaFX game - Desktop version\n" +
                        "\n" +
                        "Use ArrowKey to Control the Piece back to the Correct State\n" +
                        "\n" +
                        " Version "+MainWindow.VERSION+" - 2016\n\n");
                t0.getStyleClass().setAll("game-label", "game-label-about");

                textFlow.getChildren().setAll(t0);
                overlayTxt.getChildren().setAll(textFlow);
                overlayBtns.getChildren().setAll(btContinue);

                this.getChildren().removeAll(overlay,overlayBtns);
                this.getChildren().addAll(overlay,overlayBtns);
                overlayOnProperty.set(true);
            }
        });

        gameStepProperty.addListener(((observable, oldValue, newValue) -> {
            if (newValue.intValue() < gameBestProperty.get()) {
                gameBestProperty.set(newValue.intValue());
            }
        }));

        overlayOnProperty.addListener(((observable, oldValue, newValue) -> {
            if (!newValue) {
                getChildren().removeAll(overlay, overlayBtns);
                getParent().requestFocus();
            } else {
                overlayBtns.getChildren().get(0).requestFocus();
            }
        }));
    }

    private void initialGameProperties() {
        overlay.setMinSize(gridWidth, gridWidth);
        overlay.setAlignment(Pos.CENTER);
        overlay.setTranslateY(TOP_HEIGHT + GAP_HEIGHT);

        overlay.getChildren().setAll(overlayTxt);
        overlayTxt.setAlignment(Pos.CENTER);

        overlayBtns.setAlignment(Pos.CENTER);
        overlayBtns.setTranslateY(TOP_HEIGHT + GAP_HEIGHT + (gridWidth >> 1));
        overlayBtns.setMinSize(gridWidth, gridWidth >> 1);
        overlayBtns.setSpacing(10);

        btTry.getStyleClass().add("game-button");
        btTry.setOnAction(e -> btnTryAgain());
        btTry.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER) || e.getCode().equals(KeyCode.SPACE))
                btnTryAgain();
        });

        setOverlayBtns();

        timerPause = new Timeline(new KeyFrame(Duration.seconds(1),
                e -> time = time.plusNanos(1000000000)));
        timerPause.setCycleCount(Animation.INDEFINITE);

        initialListener();

//        loadRecord();

    }

    private void quit() {
        timerPause.stop();
        Platform.exit();
    }

    public void pauseGame() {
        if(!gamePauseProperty.get()){
            gamePauseProperty.set(true);
        }
    }

    public void aboutGame() {
        if (!gameAboutProperty.get()) {
            gameAboutProperty.set(true);
        }
    }

    public void quitGame() {
        if (!gameQuitProperty.get()) {
            gameQuitProperty.set(true);
        }
    }

    public void setGameWin(boolean won) {
        if (!gameWonProperty.get()) {
            gameWonProperty.set(won);
        }
    }

    private void btnTryAgain() {
        timerPause.stop();
        overlayOnProperty.set(false);

        doResetGame();
    }

    private void doResetGame() {
        doClearGame();
        resetGame.set(true);
    }

    private void doClearGame() {
        saveRecord();
        gridGroup.getChildren().removeIf(c -> c instanceof  Piece);
        this.getChildren().removeAll(overlay, overlayBtns);

        clearGame.set(false);
        resetGame.set(false);
        loadGame.set(false);
        saveGame.set(false);
        gameWonProperty.set(false);
        overlayOnProperty.set(false);
        gameStepProperty.set(0);
        gameAboutProperty.set(false);
        gameQuitProperty.set(false);
        gameSaveProperty.set(false);
        gameTryAgainProperty.set(false);
        gamePauseProperty.set(false);
        gameLoadProperty.set(false);

        clearGame.set(true);
    }

    private void saveRecord() {

    }

    private final Overlay wonListener = new Overlay("Fantastic! You Win!", "",
            btContinue, btTry, "game-overlay-won", "game-label-won", true);

    public void tryAgain() {
        if (!gameTryAgainProperty.get()) {
            gameTryAgainProperty.set(true);
        }
    }


    private class Overlay implements ChangeListener<Boolean> {

        private final String msg, warning;
        private final String sty1, sty2;
        private final Button btn1, btn2;
        private final boolean pause;

        public Overlay(String msg, String warning, Button b1, Button b2, String sty1, String sty2, boolean pause) {
            this.msg = msg; this.warning = warning;
            this.sty1 = sty1; this.sty2 = sty2;
            this.btn1 = b1; this.btn2 = b2;
            this.pause = pause;
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (newValue) {
                timer.stop();
                if (pause) {
                    timerPause.play();
                }
                overlay.getStyleClass().setAll("game-overlay", sty1);
                overlayTitle.setText(msg);
                overlayTitle.getStyleClass().setAll("game-label", sty2);
                overlaySubTitle.setText(warning);
                overlaySubTitle.getStyleClass().setAll("game-label", "game-label-warning");
                overlayTxt.getChildren().setAll(overlayTitle, overlaySubTitle);
                overlayBtns.getChildren().setAll(btn1);
                if (btn2 != null) {
                    overlayBtns.getChildren().add(btn2);
                }
                if (!overlayOnProperty.get()) {
                    GameBoard.this.getChildren().addAll(overlay, overlayBtns);
                    overlayOnProperty.set(true);
                }
            }
        }
    }
}

