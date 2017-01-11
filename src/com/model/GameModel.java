package com.model;

import com.gui.Piece;
import com.gui.MainPane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
/**
 * Created by Mike Huang on 2016/6/4.
 */
public class GameModel {
    public static final int DEFAULT_GRID_SIZE = 3;
    public static MainPane.ResetSize resetSize;

    public static List<Piece> REFRESH_MODEL(int gridSize) {
        List<Integer> numList = createModel(gridSize);
        List<Piece> pieces = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Piece piece = new Piece(numList.get(index++));
                piece.setLocation(new Location(i, j));
                pieces.add(piece);
            }
        }
        System.out.println(numList);
        return pieces;
    }

    public static List<Integer> SOL_NUM_LIST(int gridSize) {
        List<Integer> list = IntStream.range(1, gridSize * gridSize).boxed().collect(Collectors.toList());
        list.add(0);
        return list;
    }

    private static List<Integer> createModel(int gridSize) {
        if (gridSize < 3) {
            System.err.println("grid size is too small, try to input a larger size");
        }
        List<Integer> numList = SOL_NUM_LIST(gridSize);
        do {
            Collections.shuffle(numList);
        } while (!canBeSolved(numList));
        System.out.println(inverseNumber(numList));
        return numList;
    }

    private static boolean canBeSolved(List<Integer> numList) {
        int gridSize = (int)Math.sqrt(numList.size());
        List<Integer> list = new ArrayList<>();

        if (gridSize % 2 == 1) {
            list = numList;
        } else {
            for (int i = 0; i < gridSize * gridSize; i++) {
                if ((i / gridSize) % 2 == 0) {
                    list.add(numList.get(i));
                } else {
                    list.add(numList.get((i / gridSize + 1) * gridSize - i % gridSize - 1));
                }
            }
            gridSize /= gridSize;
        }
        System.out.println(list);
        return (inverseNumber(list) - gridSize) % 2 == 1;
    }

    private static int inverseNumber(List<Integer> list) {
        int num = 0;

        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i) > list.get(j) && list.get(j) != 0) num++;
            }
        }

        return num;
    }
}
