package com.gui;

import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.scene.Group;
import javafx.scene.layout.HBox;

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

    private final IntegerProperty scoreProperty = new SimpleIntegerProperty(0);
    private final IntegerProperty bestProperty = new SimpleIntegerProperty(0);

    private final BooleanProperty layerOnProperty = new SimpleBooleanProperty(false);

    private LocalTime time;
    private Timeline timer;
    private final StringProperty clock = new SimpleStringProperty("00:00:00");
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault());

    // User Interface controls


    public void setToolBar(HBox toolBar) {
        toolBar.disableProperty().bind(layerOnProperty);
        toolBar.spacingProperty().bind(Bindings.divide());
    }
}

