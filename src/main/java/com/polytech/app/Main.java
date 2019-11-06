package com.polytech.app;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

import org.bytedeco.opencv.opencv_core.*;

import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = FXMLLoader.load(getClass().getResource("/vues.fxml"));
			Scene scene = new Scene(root,600,400);
			scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
<<<<<<< HEAD
		// launch(args);

		Mat image = imread("D:\\Lucas\\Pictures\\eggs.png");
		Mat greyscale = new Mat();
		Mat edges = new Mat();

		if (image != null) {
			cvtColor(image, greyscale, COLOR_BGR2GRAY); // Conversion de l'image en noir et blanc

			double highThreshold = threshold(greyscale, new Mat(), 0, 255, THRESH_BINARY + THRESH_OTSU);
			double lowThreshold = 0.5 * highThreshold;

			Canny(greyscale, edges, lowThreshold, highThreshold);
			imwrite("D:\\Lucas\\Pictures\\edges.jpg", edges);
		}
=======
		launch(args);
>>>>>>> fd2dec21d31acfcd398a7c6a0be97df4a86d7ba9
	}
}
