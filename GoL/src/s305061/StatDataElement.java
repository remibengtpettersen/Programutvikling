package s305061;

/**
 * Created by And on 10.04.2016.
 */
public class StatDataElement {

    private int iteration;

    private int living;
    private int growth;
    //private int similiarity bldakjshdajksdakjsdgasjdh

    public int getIteration() { return iteration; }
    public int getGrowth() { return growth; }
    public int getLiving() { return living; }

    public StatDataElement(int iteration, int living, int growth){

        this.iteration = iteration;
        this.living = living;
        this.growth = growth;
    }
}
