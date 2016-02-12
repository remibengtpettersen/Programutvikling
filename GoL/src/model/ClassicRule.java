package model;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public class ClassicRule extends Rule {

    private int neighbour;

    @Override
    public boolean evolve(int neighbours, boolean isAlive) {

        if(neighbours == 3)
            return true;                //will live
        else if(neighbours != 2)
            return false;               //will die
        else
            return isAlive;             //wont change
    }
}
