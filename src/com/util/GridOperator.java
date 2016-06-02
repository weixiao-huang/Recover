package com.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Mike Huang on 2016/5/28.
 */
public class GridOperator {

    public static final int DEFAULT_GRID_SIZE = 4;

    private final int gridSize;
    private final List<Integer> traversalX;
    private final List<Integer> traversalY;

    public GridOperator() {
        this(DEFAULT_GRID_SIZE);
    }

    public GridOperator(int gridSize) {
        this.gridSize = gridSize;
        this.traversalX = IntStream.range(0, gridSize).boxed().collect(Collectors.toList());
        this.traversalY = IntStream.range(0, gridSize).boxed().collect(Collectors.toList());
    }
}
