package com.util;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Mike Huang on 2016/5/26.
 */
public class NPuzzle {
    public int n;
    public ArrayList<Integer> arrayList;

    public NPuzzle() {
        this.n = 3;
        this.arrayList = new ArrayList<Integer>();
        generateData();
    }
    public NPuzzle(int n) {
        if (n < 3) {
            System.err.println("the value of n is too small, try to input a larger n");
        }
        this.n = n;
        this.arrayList = new ArrayList<Integer>();
        generateData();
    }

    private void generateData() {
        for (int i = 0; i < n * n; i++) {
            arrayList.add(i);
        }
        do {
            unsort();
        } while (inverseNumber() % 2 != 0);
    }

    private void unsort() {
        Random random = new Random();
        for (int i = n * n; i > 0; i--) {
            int tmp = arrayList.get(i-1);
            int index = random.nextInt(i);
            arrayList.set(i-1, arrayList.get(index));
            arrayList.set(index, tmp);
        }
    }

    private int inverseNumber() {
        int num = 0;
        for (int i = 0; i < arrayList.size() - 1; i++) {
            if (arrayList.get(i) != 0 && arrayList.get(i+1) != 0 && arrayList.get(i) > arrayList.get(i+1))
                num++;
        }
        return num;
    }

    @Override
    public String toString() {
        return arrayList.toString();
    }
}