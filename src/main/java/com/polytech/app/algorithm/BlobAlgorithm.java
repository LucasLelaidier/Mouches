package com.polytech.app.algorithm;

import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.KeyPoint;
import org.bytedeco.opencv.opencv_core.KeyPointVector;
import org.bytedeco.opencv.opencv_core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.bytedeco.opencv.opencv_features2d.AgastFeatureDetector;
import org.opencv.features2d.Features2d;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;

public class BlobAlgorithm implements Algorithm
{
	@Override
	public Output run(Input input)
	{
		long startTime = System.nanoTime();

		Path path = Paths.get(input.getImagePath());
		String newPath = path.getParent().toString() + "\\EDGED_" + path.getFileName();

		AgastFeatureDetector detector = AgastFeatureDetector.create();
		Mat image = imread(input.getImagePath());
		Mat edged = new Mat();
		KeyPointVector keypoints = new KeyPointVector();

		detector.detect(image, keypoints);

		for(int i = 0; i < keypoints.size(); i++) {
			KeyPoint keypoint = keypoints.get(i);

			// ???????	
		}

		imwrite(newPath, image);

		long endTime = System.nanoTime();
		return new Output(0, endTime - startTime, newPath);
	}
}
