package com.polytech.app.algorithm;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LaplacianAlgorithmTest
{
    private Algorithm algo;

    @Before
    public void initialize() {
        algo = new LaplacianAlgorithm();
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongImagePathThrowsIllegalArgumentException()
    {
        algo.run(new Input("wrongimagepath"));
    }

    @Test
    public void correctImagePathReturnOutputObject()
    {
        Output output = algo.run(new Input("src/test/resources/mouches.png", 30));

        assertTrue(output != null);
    }

    @Test
    public void durationTimeIsBiggerThanZero() {
        Output output = algo.run(new Input("src/test/resources/mouches.png", 30));

        assertTrue(output.getDuration() >= 0);
    }

    // Tester que le comptage fontionne
}