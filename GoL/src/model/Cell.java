package model;

import javafx.scene.paint.Color;

import static sun.jvm.hotspot.oops.MethodData.cellSize;


/**
 * Created by Truls on 18/04/16.
 */
public class Cell {
    private Color color, deadColor, ghostColor;
    private double size;
    private double spacing = 0.1;
    public final static byte MAX_SIZE = 100;

    public Cell(double size, double spacing, Color color, Color deadColor, Color ghostColor) {
        setSize(size);
        setSpacing(spacing);
        setColor(color);
        setDeadColor(deadColor);
        setGhostColor(ghostColor);
    };

    public Cell(Configuration configuration){
        try {
            color = (Color) Color.class.getField(configuration.getCellColor()).get(null);
            deadColor = (Color) Color.class.getField(configuration.getBackgroundColor()).get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        calculateGhostColor();

        size = configuration.getCellSize();
    }

    @Override
    public Cell clone() {
        Cell clone = new Cell(size, spacing, color, deadColor, ghostColor);
        return clone;
    }

    /**
     * Calculates the color just in between the background color and the cell color
     */
    public void calculateGhostColor() {

        double blue = (color.getBlue() + deadColor.getBlue()) / 2;
        double red = (color.getRed() + deadColor.getRed()) / 2;
        double green = (color.getGreen() + deadColor.getGreen()) / 2;

        ghostColor = new Color(red, green, blue, 1);
    }



    public Color getColor() {
        return color;
    }

    public Color getDeadColor() {
        return deadColor;
    }

    public Color getGhostColor() {
        return ghostColor;
    }

    public double getSize() {
        return size;
    }

    public double getSpacing() {
        return spacing;
    }

    public void setColor(Color color) {
        this.color = color;
        calculateGhostColor();
    }

    public void setDeadColor(Color deadColor) {
        this.deadColor = deadColor;
        calculateGhostColor();
    }

    public void setGhostColor(Color ghostColor) {
        this.ghostColor = ghostColor;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public void setSpacing(double spacing) {
        this.spacing = spacing;
    }
}
