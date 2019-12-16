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

        if(image.data() == null) {
        	throw new IllegalArgumentException("The provided image path is not correct");
		}

        Mat greyscale = new Mat();
        Mat edges = new Mat();

        Path path = Paths.get(input.getImagePath());
        String newPath = path.getParent().toString() + "\\EDGED_" + path.getFileName();

		// On converti l'image en noir et blanc
		cvtColor(image, greyscale, COLOR_BGR2GRAY);

		// On récupère la valeur de seuil en pourcentage passée en input
		// et on la choisis comme seuil bas et on la multiplie par
		// trois pour obtenir le seuil haut
		double lowThreshold = input.getThreshold();
		double highThreshold = lowThreshold * 3;

		// On applicque l'alogirthme de detection de contours canny sur l'image
		Canny(greyscale, edges, lowThreshold, highThreshold);
		imwrite(newPath, edges);

        long endTime = System.nanoTime();

        return new Output(50, endTime - startTime, newPath);
    }
}

// Responsive
// XML => Liste des algos + options
