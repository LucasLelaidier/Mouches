package com.polytech.app.controllers;

import com.polytech.app.algorithm.*;
import javafx.scene.control.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MainController {
    @FXML
    private Slider sliderZoom;

    @FXML
    private TextField treshold;

    @FXML
    private Menu algorithmMenu;

    @FXML
    private VBox vbox;

    @FXML
    private CheckBox showEdgedImageCheckBox;

    private ImageView imageAnalisee;

    private String image;

    private String edgedImage;

    private Algorithm choosedAlgorithm;

    @FXML
    private void initialize() {
        // We set vbox's minSize to 0, 0 in order to
        // let their childrens shrink when the vbox shrinks.
        vbox.setMinSize(0, 0);

        // We initialise the algorithm to TestAlgorithm
        // so when the software is launched by default
        // TestAlgorithm is selected
        choosedAlgorithm = new TestAlgorithm();

        // Listener on threshold text area.
        // When we change the value of the
        // text area, the slider's value change.
        treshold.textProperty().addListener((observable, oldValue, newValue) -> sliderZoom.setValue(Double.parseDouble(newValue)));

        // Listener on slider.
        // When we move the slider, the value of threshold
        // text area is updated with the value of the slider.
        sliderZoom.valueProperty().addListener((observable, oldValue, newValue) -> treshold.setText(Double.toString(newValue.intValue())));

        // Listener on slider release.
        // Each time we release the slider, we update
        // edged image with the new value of the slider.
        sliderZoom.setOnMouseReleased(event -> {
            if(image != null) {
                Output out = choosedAlgorithm.run(new Input(image, (int) sliderZoom.getValue()));
                edgedImage = out.getImagePath();

                Image fxImage = new Image("file:" + edgedImage);
                imageAnalisee.setImage(fxImage);

                showEdgedImageCheckBox.setSelected(false);
            }
        });

        // A remettre dans un controlleur dédié

        // Getting factory used to create XML parser
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            // Creating parser
            final DocumentBuilder builder = factory.newDocumentBuilder();
            // Opening the file containing the algorithm list
            final Document document = builder.parse(getClass().getResource("/algorithms.xml").openStream());
            // Getting root element
            final Element racine = document.getDocumentElement();
            // Getting childs of root
            final NodeList racineNoeuds = racine.getChildNodes();

            instanciateAlgoMenu(racineNoeuds);
        }
        catch (ParserConfigurationException | SAXException | IOException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ouvrirImage() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("Images", "*.JPG", "*.PNG", "*.GIF");
        fileChooser.getExtensionFilters().addAll(extFilterJPG);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(null);

        try {
            if(file != null) {
                // Read the image and transform it into javaFX image
                BufferedImage bufferedImage = ImageIO.read(file);
                Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);

                // We remove actual image from interface
                vbox.getChildren().remove(imageAnalisee);

                imageAnalisee = new ImageView(fxImage);
                imageAnalisee.setPreserveRatio(true);

                // Let the image resize following the window size
                imageAnalisee.fitHeightProperty().bind(vbox.heightProperty());
                imageAnalisee.fitWidthProperty().bind(vbox.widthProperty());

                vbox.getChildren().addAll(imageAnalisee);

                this.image = file.getAbsolutePath();
            }
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void changeImage() {
        if(image != null && edgedImage != null) {
            if(showEdgedImageCheckBox.isSelected()) {
                Image fxImage = new Image("file:" + image);
                imageAnalisee.setImage(fxImage);
            } else {
                Image fxImage = new Image("file:" + edgedImage);
                imageAnalisee.setImage(fxImage);
            }
        }
    }

    public void instanciateAlgoMenu(NodeList racineNoeuds) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final int nbRacineNoeuds = racineNoeuds.getLength();
        for (int i = 0; i < nbRacineNoeuds; i++)
        {
            // For each childs if its a node (an algorithm) we add its name to algorithm menu
            if(racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE)
            {
                // We get the node containing the algorithm data.
                final Element algo = (Element) racineNoeuds.item(i);

                // Adding the algorithm to the menu
                MenuItem menuItem = new MenuItem(algo.getAttribute("name"));
                algorithmMenu.getItems().add(menuItem);

                // Then we have to instantiate and object
                // of the type described in the node data.
                // We use java forName in order to do that.
                String path = algo.getAttribute("path");
                Class algorithmClass = Class.forName(path);

                // We get algorithm's constructor
                Constructor constructor = algorithmClass.getConstructor();

                // Then we can instantiate the object
                Algorithm algorithmInstance = (Algorithm) constructor.newInstance();

                // We change the parameters of the interface
                // depending of chosen algorithm.
                updateParameters(algo.getChildNodes(), menuItem, algorithmInstance);
            }
        }
    }

    public void updateParameters(NodeList parameters, MenuItem menuItem, Algorithm algorithmInstance) {
        for (int i = 0; i < parameters.getLength(); i++) {
            // We search for the parameters node
            if(parameters.item(i).getNodeType() == Node.ELEMENT_NODE && parameters.item(i).getNodeName().equals("parameters")) {
                final NodeList parametersList = parameters.item(i).getChildNodes();
                // Once we found it we parse all parameter node
                // and add the corresponding fx element to the gridpane

                int row = 1; // The row where we start to add parameters
                for (int j = 0; j < parametersList.getLength(); j++) {
                    // Only cast elements else an error is raised
                    if(parametersList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        Element param = (Element) parametersList.item(j);
                        if(param.getAttribute("type").equals("slider")) {
                            // We fetch max and min range of slider
                            // from the Slider node.
                            String range = param.getAttribute("range");
                            String[] ranges = range.split("-");
                            int min = Integer.parseInt(ranges[0]);
                            int max = Integer.parseInt(ranges[1]);

                            // We set an onClick event on the menu :
                            // onClick we change slider's max and min
                            // and also change actual algorithm used
                            // to the new one.
                            menuItem.setOnAction(event -> {
                                sliderZoom.setMin(min);
                                sliderZoom.setMax(max);
                                sliderZoom.setMajorTickUnit(Math.round(max / 4f));
                                choosedAlgorithm = algorithmInstance;
                            });
                        }
                    }
                }
            }
        }
    }
}
