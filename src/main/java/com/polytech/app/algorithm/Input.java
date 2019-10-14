package main.java.com.polytech.app.algorithm;

/**
 * The object that an algorithm must use as entry parameter.
 * It contains the path of the image to process.
 *
 * @author Lucas Lelaidier
 *
 */
public class Input {
    /**
     * Path of the image to process.
     */
    private String imagePath;

    /**
     * Path of the image to process.
     * @param imagePath
     */
    public Input(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }
}