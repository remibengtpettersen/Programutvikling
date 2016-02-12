package model;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public class GameBoard {

    public boolean[][] grid;
    public byte[][] neighbours;

    public void createGameBoard(int gameSize) {
        grid = new boolean[gameSize][gameSize];
        neighbours = new byte[gameSize][gameSize];
    }


}
