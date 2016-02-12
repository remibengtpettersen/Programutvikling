package model;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public class ClassicRule implements Rule {

    /**
     *
     * Checs if dead cells should live
     * if it has 3 neighbours it should live
     * @param neighbours
     * @return true if it should live
     */
    @Override
    public boolean shouldLive(int neighbours) {
        if(neighbours == 3)
            return true;
        else
            return false;
    }

    /**
     * Checks if live cells should die
     * if it has 2 ore 3 neighbours it should not die
     * @param neighbours
     * @return true if it should die
     */
    @Override
    public boolean shouldDie(int neighbours) {
        if(neighbours == 2 || neighbours == 3)
            return false;
        else
            return true;
    }
}
