package com.polytech.app.algorithm;

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
     * What threshold we cant (in percentage).
     */
    private int threshold;

    /**
     * Path of the image to process.
     * @param imagePath
     */
    public Input(String imagePath) {
        this.imagePath = imagePath;
    }

    public Input(String imagePath, int threshold) {
        this.imagePath = imagePath;
        this.threshold = threshold;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getThreshold() {
        return threshold;
    }
}