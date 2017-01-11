package com.gui;


import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Created by Mike Huang on 2016/5/26.
 */
public class MainWindow extends Application {

    public static final String VERSION = "1.0.0";

    private MainPane root;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new MainPane();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("com/style.css");

        Bounds gameBounds = root.getGameManager().getLayoutBounds();
        int MARGIN = MainPane.getMARGIN();
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double factor = Math.min(visualBounds.getWidth() / (gameBounds.getWidth() + MARGIN),
                visualBounds.getHeight() / (gameBounds.getHeight() + MARGIN));

        primaryStage.setTitle("Recover");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(gameBounds.getHeight() / 2.);
        primaryStage.setMinWidth(gameBounds.getWidth() / 2.);
        primaryStage.setWidth((gameBounds.getWidth() + MARGIN) * factor);
        primaryStage.setHeight((gameBounds.getHeight() + MARGIN) * factor);

        primaryStage.setOnCloseRequest(t->{
            t.consume();
            root.getGameManager().quitGame();
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
