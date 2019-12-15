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
        vbox.setMinSize(0, 0);

        choosedAlgorithm = new TestAlgorithm();

        treshold.textProperty().addListener((observable, oldValue, newValue) -> {
            sliderZoom.setValue(Double.parseDouble(newValue));
        });

        sliderZoom.valueProperty().addListener((observable, oldValue, newValue) -> {
            treshold.setText(Double.toString(newValue.intValue()));
        });

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
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);

                vbox.getChildren().remove(imageAnalisee);

                imageAnalisee = new ImageView(image);
                imageAnalisee.setPreserveRatio(true);
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
                final Element algo = (Element) racineNoeuds.item(i);
                MenuItem menuItem = new MenuItem(algo.getAttribute("name"));
                algorithmMenu.getItems().add(menuItem);

                String path = algo.getAttribute("path");
                Class algorithmClass = Class.forName(path);
                Constructor constructor = algorithmClass.getConstructor();

                Algorithm algorithmInstance = (Algorithm) constructor.newInstance();

                updateParameters(algo.getChildNodes(), menuItem, algorithmInstance);
            }
        }
    }

    public void updateParameters(NodeList parameters, MenuItem menuItem, Algorithm algorithmInstance) {
        for (int i = 0; i < parameters.getLength(); i++) {
            // We search for the parameters node
            if(parameters.item(i).getNodeType() == Node.ELEMENT_NODE) {
                if(parameters.item(i).getNodeName().equals("parameters")) {
                    final NodeList parametersList = parameters.item(i).getChildNodes();
                    // Once we found it we parse all parameter node
                    // and add the corresponding fx element to the gridpane

                    int row = 1; // The row where we start to add parameters
                    for (int j = 0; j < parametersList.getLength(); j++) {
                        // Only cast elements else an error is raised
                        if(parametersList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            Element param = (Element) parametersList.item(j);
                            if(param.getAttribute("type").equals("slider")) {
                                String range = param.getAttribute("range");
                                String[] ranges = range.split("-");
                                int min = Integer.parseInt(ranges[0]);
                                int max = Integer.parseInt(ranges[1]);

                                menuItem.setOnAction(event -> {
                                    sliderZoom.setMin(min);
                                    sliderZoom.setMax(max);
                                    sliderZoom.setMajorTickUnit(Math.round(max / 4));
                                    choosedAlgorithm = algorithmInstance;
                                });
                            }
                        }
                    }
                }
            }
        }
    }
}
