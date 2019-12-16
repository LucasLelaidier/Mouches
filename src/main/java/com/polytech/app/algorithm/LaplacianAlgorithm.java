package com.polytech.app.algorithm;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;
import org.opencv.core.Core;
import org.opencv.core.CvType;

import java.nio.file.Path;
import java.nio.file.Paths;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.*;

public class LaplacianAlgorithm implements Algorithm {

	@Override
	public Output run(Input input) {
		int scale = 1;
		int delta = 0;
		int ddepth = CvType.CV_16S;

		long startTime = System.nanoTime();

		Mat image = imread(input.getImagePath());

		if(image.data() == null) {
			throw new IllegalArgumentException("The provided image path is not correct");
		}

		Mat greyscale = new Mat();
		Mat edges = new Mat();

		Path path = Paths.get(input.getImagePath());
		String newPath = path.getParent().toString() + "\\EDGED_" + path.getFileName();

		// On applique un flou gaussien à l'image afin de réduire
		// le bruit et ainsi avoir une image plus nette
		GaussianBlur(image, image, new Size(3, 3), 0, 0, Core.BORDER_DEFAULT); // Floutage de l'image pour réduire le bruit

		cvtColor(image, greyscale, COLOR_BGR2GRAY); // Conversion de l'image en noir et blanc

		// Le parametre kernel doit etre impair et inferieur à 31
		// donc on multiplie la valeur du slider (0 à 100) par 0.31
		// et si le nombre est pair on lui ajoute 1 pour le rendre impair
		Laplacian(greyscale, edges, ddepth, peerToOdd((int)(input.getThreshold() * 0.31)), scale, delta, Core.BORDER_DEFAULT);

		imwrite(newPath, edges);

		long endTime = System.nanoTime();

		return new Output(0, endTime - startTime, newPath);
	}

	/**
	 * Transforme un nombre entier pair en nombre impair
	 * @param value le nombre pair (ou impair mais la fonction ne fera a rien)
	 * @return le nombre impair supérieur au nombre pair passé
	 */
	private int peerToOdd(int value) {
		return value % 2 == 0 ? value + 1 : value;
	}
}
