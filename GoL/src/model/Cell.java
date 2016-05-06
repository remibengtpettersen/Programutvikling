package model;

import javafx.scene.paint.Color;

/**
 *  Pair programmed.
 *  A simple cell object. The cell object can contain size, max size, min size, color, dead color, ghost color and spacing.
 */
public class Cell {
    public final static byte MAX_SIZE = 100;
    public final static double MIN_SIZE = 0.1;

    // sets colors to default color when instantiate
    private Color color = Color.BLACK, deadColor = Color.WHITE, ghostColor;
    private double size;
    private double spacing = 0.1;

    /**
     * Creates a default cell with the default Conway "Game Of Life" style.
     */
    public Cell(){
        calculateGhostColor();
    }

    /**
     * Creates a default cell with the default Conway "Game Of Life" style with a specific configuration.
     * @param configuration Configuration file
     */
    public Cell(Configuration configuration){

        try {
            // set color from configuration file
            setColor((Color) Color.class.getField(configuration.getCellColor()).get(null));

            // set dead color from configuration file
            setDeadColor((Color)Color.class.getField(configuration.getBackgroundColor()).get(null));

        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        // calculates ghost color according to RGB-model
        calculateGhostColor();

        // set cell size from configuration file
        setSize(configuration.getCellSize());
    }

    /**
     * Calculates a color in the range: alive cell to dead cell color.
     */
    public void calculateGhostColor() {

        // blue color
        double blue = (color.getBlue() + deadColor.getBlue()) / 2;
        // red color
        double red = (color.getRed() + deadColor.getRed()) / 2;
        // green color
        double green = (color.getGreen() + deadColor.getGreen()) / 2;

        // set ghost color
        setGhostColor(new Color(red, green, blue, 1));
    }

    /**
     * Sets the value of the property color
     * @return A color for its cell.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Gets the value of the property deadColor.
     * @return A dead color for its cell.
     */
    public Color getDeadColor() {
        return deadColor;
    }

    /**
     * Gets the value of the property ghostColor.
     * @return A ghost color for its cell.
     */
    public Color getGhostColor() {
        return ghostColor;
    }

    /**
     * Gets the value of the property size.
     * @return A size for its cell.
     */
    public double getSize() {
        return size;
    }

    /**
     * Gets the value for the property spacing.
     * @return A spacing for its cell.
     */
    public double getSpacingInPixels() {
        return spacing * getSize();
    }

    /**
     * Gets the value for the property spacing
     * @return A spacing for its cell.
     */
    public double getSpacingFactor() {
        return spacing;
    }

    /**
     * Sets the color of the property color.
     * @param color A color for its cell.
     */
    public void setColor(Color color) {
        this.color = color;
        calculateGhostColor();
    }

    /**
     * Sets the color of the property deadColor.
     * @param deadColor A color value for its cell.
     */
    public void setDeadColor(Color deadColor) {
        this.deadColor = deadColor;
        calculateGhostColor();
    }

    /**
     * Sets the color of the property deadColor.
     * @param ghostColor A color value for its cell color.
     */
    public void setGhostColor(Color ghostColor) {
        this.ghostColor = ghostColor;
    }

    /**
     * Set the size of the property size.
     * @param size A double value for its cell.
     */
    public void setSize(double size) {
        // checks if size is less then MAX_SIZE
        if (size > MAX_SIZE) {
            this.size = MAX_SIZE;
        }
        // less then MIN_SIZE
        else if (size < MIN_SIZE){
            this.size = MIN_SIZE;
        }
        else {
            this.size = size;
        }
    }

    /**
     * Sets the spacing of the property spacing.
     * @param spacing A double value for its cell spacing.
     */
    public void setSpacing(double spacing) {
        this.spacing = spacing;
    }

    public Cell clone(){
        Cell cell = new Cell();
        cell.setSize(size);
        cell.setColor(color);
        cell.setDeadColor(deadColor);
        cell.setSpacing(spacing);

        return cell;
    }


}
