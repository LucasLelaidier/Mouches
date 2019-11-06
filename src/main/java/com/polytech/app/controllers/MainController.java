package com.polytech.app.controllers;

import com.polytech.app.algorithm.Input;
import com.polytech.app.algorithm.Output;
import com.polytech.app.algorithm.TestAlgorithm;
import javafx.scene.control.TextField;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class MainController {
	@FXML
	private ImageView imageAnalisee;

	@FXML
	private Slider sliderZoom;

	@FXML
	private TextField Treshold;

	private String image;

	@FXML
	private void initialize() {
        Treshold.textProperty().addListener((observable, oldValue, newValue) -> {
            sliderZoom.setValue(Double.parseDouble(newValue));
        });

		sliderZoom.valueProperty().addListener((observable, oldValue, newValue) -> {
		    Treshold.setText(Double.toString(newValue.intValue()));
        });

        sliderZoom.setOnMouseReleased(event -> {
            if(image != null) {
                TestAlgorithm algo = new TestAlgorithm();

                Output out = algo.run(new Input(image, (int) sliderZoom.getValue()));

                Image fxImage = new Image("file:" + out.getImagePath());
                imageAnalisee.setImage(fxImage);
            }
        });
	}

	@FXML
	private void ouvrirImage() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
		FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
		fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

		// Show open file dialog
		File file = fileChooser.showOpenDialog(null);

		try {
			BufferedImage bufferedImage = ImageIO.read(file);
			Image image = SwingFXUtils.toFXImage(bufferedImage, null);
			imageAnalisee.setImage(image);
			this.image = file.getAbsolutePath();
		} catch (IOException ex) {
			Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
