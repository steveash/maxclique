package com.github.steveash.maxclique;

import static org.junit.Assert.*;

import org.junit.Test;

public class WeightMatrixTest {

    @Test
    public void shouldWorkWithSize1() throws Exception {
        WeightMatrix m = new WeightMatrix(1);
        assertEquals(0, m.weight(0, 0), 0.01);
        m.set(0, 0, 123);
    }

    @Test
    public void shouldWorkWithSizeX() throws Exception {
        checkSize(2);
        checkSize(3);
        checkSize(4);
        checkSize(5);
        checkSize(6);
    }

    private void checkSize(int n) {
        int v = 0;
        WeightMatrix m = new WeightMatrix(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                m.set(i, j, v++);
            }
        }
        int shouldBe = n * (n - 1) / 2;
        double[] weights = m.getWeights();
        assertEquals(shouldBe, weights.length);
        for (int i = 0; i < shouldBe; i++) {
            assertEquals((double) i, weights[i], 0.001);
        }

        v = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {

                double check = m.weight(i, j);
                assertEquals((double) v, check, 0.001);
                v += 1;
            }
        }
    }

    @Test
    public void shouldName() throws Exception {

    }
}