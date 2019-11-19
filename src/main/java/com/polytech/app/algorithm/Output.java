package com.polytech.app.algorithm;

/**
 * The object that an algorithm must return after complete an image processing.
 * It will then be used by UI to show results.
 *
 * @author Lucas Lelaidier
 *
 */
public class Output {
    /**
     * The number of eggs find in the image
     */
    private int eggNumber;

    /**
     * The time taken by the algorithm to complete the image processing in ms
     */
    private long duration;

    /**
     * The path of the new image
     */
    private String imagePath;

    /**
     * Constructor
     *
     * @param eggNumber
     * @param duration
     * @param imagePath
     */
    public Output(int eggNumber, long duration, String imagePath) {
        super();
        this.eggNumber = eggNumber;
        this.duration = duration;
        this.imagePath = imagePath;
    }

    public int getEggNumber() {
        return eggNumber;
    }

    public long getDuration() {
        return duration;
    }

    public String getImagePath() {
        return imagePath;
    }
}