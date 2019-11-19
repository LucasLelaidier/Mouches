package com.polytech.app.algorithm;

import org.bytedeco.opencv.opencv_core.Mat;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_imgproc.Canny;

public class TestAlgorithm implements Algorithm {

    @Override
    public Output run(Input input) {
        long startTime = System.nanoTime();

        Mat image = imread(input.getImagePath());
        Mat greyscale = new Mat();
        Mat edges = new Mat();

        Path path = Paths.get(input.getImagePath());
        String newPath = path.getParent().toString() + "\\EDGED_" + path.getFileName();

        if (image != null) {
            cvtColor(image, greyscale, COLOR_BGR2GRAY); // Conversion de l'image en noir et blanc

            double lowThreshold = input.getThreshold();
            double highThreshold = lowThreshold * 3;

            Canny(greyscale, edges, lowThreshold, highThreshold);
            imwrite(newPath, edges);
        }

        long endTime = System.nanoTime();

        return new Output(50, endTime - startTime, newPath);
    }
}

// Responsive
// XML => Liste des algos + options
