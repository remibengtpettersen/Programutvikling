package model;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public class CustomRule implements Rule {
    @Override
    public boolean shouldLive(int neighbours) {
        return false;
    }

    @Override
    public boolean shouldDie(int neighbours) {
        return false;
    }
}
