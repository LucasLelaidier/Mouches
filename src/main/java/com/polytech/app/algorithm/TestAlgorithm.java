package com.polytech.app.algorithm;

public class TestAlgorithm implements Algorithm {

    @Override
    public Output run(Input input) {
        return new Output(50, 23, "random/path.png");
    }
}
