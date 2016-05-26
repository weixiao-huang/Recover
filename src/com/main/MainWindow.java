package com.main;

import com.util.NPuzzle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Created by Mike Huang on 2016/5/26.
 */
public class MainWindow extends Application {
    private int indexOfZero;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        int nOrder = 3;
        int sizeOfNum = 100;

        primaryStage.setTitle("NPuzzle");

        GridPane numPane = new GridPane();
        numPane.setAlignment(Pos.CENTER);

        ArrayList<Button> buttons = new ArrayList<Button>();
        NPuzzle nPuzzle = new NPuzzle(nOrder);

        for (int i = 0; i < nOrder * nOrder; i++) {
            Button button = new Button(String.valueOf(i));
            button.setPrefHeight(sizeOfNum);
            button.setPrefWidth(sizeOfNum);
            buttons.add(button);
        }

        int num = -1;
        for (int i = 0; i < nOrder; i++) {
            for (int j = 0; j < nOrder; j++) {
                num++;
                int index = nPuzzle.arrayList.indexOf(num);
                if (num == 0) {
                    indexOfZero = index;
                    continue;
                }
                int row = index/nOrder, col = index%nOrder;
                numPane.add(buttons.get(num), col, row);
            }
        }

        numPane.setId("number_pane");

        AnchorPane anchorPane = new AnchorPane();

        Scene scene = new Scene(numPane, 800, 600);
        scene.getStylesheets().add(MainWindow.class.getResource("style.css").toExternalForm());

        primaryStage.setScene(scene);

        primaryStage.show();
        System.out.println(nPuzzle);

        for (int i = 0; i < nOrder * nOrder; i++) {
            Button button = buttons.get(i);
            int index = nPuzzle.arrayList.indexOf(i);
            int finalI = i;
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int row0 = indexOfZero / nOrder, row = index / nOrder;
                    int col0 = indexOfZero % nOrder, col = index % nOrder;
                    if ((Math.abs(row - row0) == 1 && Math.abs(col - col0) == 0) ||
                            (Math.abs(row - row0) == 0 && Math.abs(col - col0) == 1)) {
                        GridPane.setColumnIndex(button, col0);
                        GridPane.setRowIndex(button, row0);
                        nPuzzle.arrayList.set(indexOfZero, finalI);
                        nPuzzle.arrayList.set(index, 0);
                        indexOfZero = index;
                        System.out.println(nPuzzle);
                    }
                }
            });
        }
    }
}
