package model;

/**
 * Created by Truls on 05/05/16.
 */
public class CameraView {
    public int currViewMinX;
    public int currViewMaxX;
    public int currViewMinY;
    public int currViewMaxY;

    // must be double, side scroll moves board to the left if int
    public double boardOffsetX = 50;
    public double boardOffsetY = 50;

    /**
     * Updates the fields that contains the grid minimum and maximum x and y values displayed on the canvas
     */
    public void updateView(GameOfLife gol, double cellSize, int ... dimensions) {

        // finds minimum x coordinate visible on canvas
        currViewMinX = (int) (getCommonOffsetX(gol, cellSize) / cellSize);
        if (currViewMinX < 0)
            currViewMinX = 0;

        // finds maximum x coordinate visible on canvas
        currViewMaxX = (int) ((getCommonOffsetX(gol, cellSize) + dimensions[0]) / cellSize);
        if (currViewMaxX > gol.getGridWidth())
            currViewMaxX = gol.getGridWidth();

        // finds minimum y coordinate visible on canvas
        currViewMinY = (int)(getCommonOffsetY(gol, cellSize) / cellSize);
        if (currViewMinY < 0)
            currViewMinY = 0;

        // finds maximum y coordinate visible on canvas
        currViewMaxY = (int) ((getCommonOffsetY(gol, cellSize) + dimensions[1]) / cellSize);
        if (currViewMaxY > gol.getGridHeight())
            currViewMaxY = gol.getGridHeight();

    }



    /**
     * Calculates the x offset for the board with the offset inside the GameOfLife
     * @return the common offset for the x coordinate
     */
    public double getCommonOffsetX(GameOfLife gol, double cellSize){
        return  (boardOffsetX + gol.getOffsetX() * cellSize);
    }

    /**
     * Calculates the y offset for the board with the offset inside the GameOfLife
     * @return the common offset for the y coordinate
     */
    public double getCommonOffsetY(GameOfLife gol, double cellSize){
        return  (boardOffsetY + gol.getOffsetY() * cellSize);
    }

}
