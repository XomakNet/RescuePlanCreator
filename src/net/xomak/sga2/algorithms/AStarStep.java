package net.xomak.sga2.algorithms;

/**
 * Created by regis on 13.11.2016.
 */
public abstract class AStarStep implements Comparable<AStarStep> {

    public AStarStep() {

    }

    public abstract AStarStep getPreviousStep();

}
