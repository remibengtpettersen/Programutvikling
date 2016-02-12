package model;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public class CustomRule extends Rule {

    @Override
    boolean evolve(int neighbours, boolean isAlive) {
        return false;
    }
}
