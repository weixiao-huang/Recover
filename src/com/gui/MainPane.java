package com.gui;

import com.model.Direction;
import com.model.GameModel;
import com.util.GameManager;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 * Created by Mike Huang on 2016/5/26.
 */
public class MainPane extends StackPane {

    private GameManager gameManager;
    private Bounds gameBounds;
    private static final int MARGIN = 36;
    private static final int BUTTON_SIZE = 40;

//    static {
//        // Downloaded from https://01.org/clear-sans/blogs
//        // The font may be used and redistributed under the terms of the Apache License, Version 2.0.
//        Font.loadFont(MainWindow.class.getResource("ClearSans-Bold.ttf").toExternalForm(), 10.0);
//    }

    public class ResetSize{
        public ResetSize(){}
        public void run(int newSize){
            init(newSize);
        }
    }

    public void init(){
        this.init(GameModel.DEFAULT_GRID_SIZE);
    }
    public void Resize(){
        double scale = Math.min((getWidth() - MARGIN) / gameBounds.getWidth(),
                (getHeight() - MARGIN) / gameBounds.getHeight());
        gameManager.setScale(scale);
        gameManager.setLayoutX((getWidth() - gameBounds.getWidth()) / 2d);
        gameManager.setLayoutY((getHeight() - gameBounds.getHeight()) / 2d);
    }
    public void init(int gridSize){
        this.getChildren().clear();

        GameModel.resetSize = new ResetSize();
        gameManager = new GameManager(gridSize);
        gameManager.setToolBar(createToolBar());
        gameBounds = gameManager.getLayoutBounds();

        this.getChildren().add(gameManager);

        this.getStyleClass().add("game-root");

        // Window Resize
        ChangeListener<Number> resize = (ov, v, v1) -> {
            Resize();
        };
        widthProperty().addListener(resize);
        heightProperty().addListener(resize);
        Resize();
    }

    public MainPane() {
        init();

        addKeyHandlers(this);

        setFocusTraversable(true);
        setOnMouseClicked(e -> requestFocus());
    }

    // key handles
    private void addKeyHandlers(Node node) {
        node.setOnKeyPressed(key -> {
            KeyCode keyCode = key.getCode();
            if (keyCode.isArrowKey()) {
                Direction direction = Direction.valueFor(keyCode);
                move(direction);
            }
        });
    }

    private void move(Direction direction) {
        gameManager.move(direction);
    }

    private HBox createToolBar() {
        HBox toolBar = new HBox();
        toolBar.setAlignment(Pos.CENTER);
        toolBar.setPadding(new Insets(10.0));

        Button btItem0 = createButtonItem("mSize", "Change Size", t -> gameManager.changeSize());
        Button btItem3 = createButtonItem("mPause", "Pause Game", t -> gameManager.pauseGame());
        Button btItem4 = createButtonItem("mReplay", "Try Again", t -> gameManager.tryAgain());
        Button btItem5 = createButtonItem("mInfo", "About the Game", t -> gameManager.aboutGame());
        toolBar.getChildren().setAll(btItem0, btItem3, btItem4, btItem5);
        Button btItem6 = createButtonItem("mQuit", "Quit Game", t -> gameManager.quitGame());
        toolBar.getChildren().add(btItem6);

        return toolBar;
    }

    private Button createButtonItem(String id, String text, EventHandler<ActionEvent> t) {
        Button button = new Button();
        button.setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
        button.setId(id);
        button.setOnAction(t);
        button.setTooltip(new Tooltip(text));
        return button;
    }

    public static int getMARGIN() {
        return MARGIN;
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
