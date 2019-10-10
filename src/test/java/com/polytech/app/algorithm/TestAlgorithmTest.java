package com.polytech.app.algorithm;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestAlgorithmTest {

    @Test
    public void run() {
        Algorithm algo = new TestAlgorithm();

        Output output = algo.run(new Input("random/image.png"));

        assertTrue(output.getEggNumber() > 0);
    }
}