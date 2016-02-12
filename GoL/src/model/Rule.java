package model;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public abstract class Rule {

    abstract boolean evolve(int neighbours, boolean isAlive);

}
