package com.polytech.app;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

import org.bytedeco.opencv.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;


public class Main /*extends Application*/ {
	/*@Override
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
	}*/

	public static void main(String[] args) {
		// launch(args);

		Mat image = imread("D:\\Lucas\\Pictures\\11402933_845908588820714_5708211936915400943_n.jpg");
		if (image != null) {
			GaussianBlur(image, image, new Size(51, 51), 0);
			imwrite("D:\\Lucas\\Pictures\\blured-bg.jpg", image);
		}
	}
}
