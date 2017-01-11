package com.util;

import com.model.GameModel;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntBinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Mike Huang on 2016/5/28.
 */
public class GridOperator {


    private final int gridSize;
    private final List<Integer> traversalX;
    private final List<Integer> traversalY;

    public GridOperator() {
        this(GameModel.DEFAULT_GRID_SIZE);
    }

    public GridOperator(int gridSize) {
        this.gridSize = gridSize;
        this.traversalX = IntStream.range(0, gridSize).boxed().collect(Collectors.toList());
        this.traversalY = IntStream.range(0, gridSize).boxed().collect(Collectors.toList());
    }

    public int getGridSize() {
        return gridSize;
    }

    public int traverseGrid(IntBinaryOperator fuc) {
        AtomicInteger atomicInteger = new AtomicInteger();
        traversalX.forEach(x -> {
            traversalY.forEach(y -> {
                atomicInteger.addAndGet(fuc.applyAsInt(x, y));
            });
        });

        return atomicInteger.get();
    }

}
