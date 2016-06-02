package com.gui;

import com.util.GameManager;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * Created by Mike Huang on 2016/5/26.
 */
public class MainPane extends StackPane {

    private GameManager gameManager;
    private Bounds gameBounds;
    private static final int MARGIN = 36;
    private static final int BUTTON_SIZE = 40;

    public MainPane() {
        gameManager = new GameManager();
    }

    private HBox createToolBar() {
        HBox toolBar = new HBox();
        toolBar.setAlignment(Pos.CENTER);
        toolBar.setPadding(new Insets(10.0));

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
