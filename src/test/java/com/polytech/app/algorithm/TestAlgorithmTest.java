package test.java.com.polytech.app.algorithm;

import org.junit.Test;

import main.java.com.polytech.app.algorithm.Algorithm;
import main.java.com.polytech.app.algorithm.Input;
import main.java.com.polytech.app.algorithm.Output;
import main.java.com.polytech.app.algorithm.TestAlgorithm;

import static org.junit.Assert.*;

public class TestAlgorithmTest {

    @Test
    public void run() {
        Algorithm algo = new TestAlgorithm();

        Output output = algo.run(new Input("random/image.png"));

        assertTrue(output.getEggNumber() > 0);
    }
}