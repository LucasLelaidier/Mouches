package com.polytech.app.algorithm;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestAlgorithmTest
{

    @Test(expected = IllegalArgumentException.class)
    public void wrongImagePathThrowsIllegalArgumentException()
    {
        Algorithm algo = new TestAlgorithm();
        algo.run(new Input("wrongimagepath"));
    }

    @Test
    public void correctImagePathReturnOutputObject()
    {
        Algorithm algo = new TestAlgorithm();

        Output output = algo.run(new Input("src/test/resources/mouches.png"));

        assertTrue(output != null);
    }
}