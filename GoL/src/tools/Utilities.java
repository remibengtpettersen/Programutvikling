package tools;

/**
 * Created by Andreas on 29.02.2016.
 */
public class Utilities {

    public static void print2DArray(boolean[][] grid){
        for(int y = 0; y < grid[0].length; y++){
            for(int x = 0; x < grid.length; x++){
                if(grid[x][y])
                    System.out.print(1);
                else
                    System.out.print(0);
            }
            System.out.println();
        }
    }

    public static void print2DArray(byte[][] grid){
        for(int y = 0; y < grid[0].length; y++){
            for(int x = 0; x < grid.length; x++){
                System.out.print(grid[x][y]);

            }
            System.out.println();
        }
    }
}
