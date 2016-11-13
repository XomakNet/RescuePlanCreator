package net.xomak.sga2.algorithms;

import net.xomak.sga2.graph.Vertex;

/**
 * Created by regis on 13.11.2016.
 */
public abstract class AStarStep implements Comparable<AStarStep> {

    public abstract AStarStep getPreviousStep();

    public AStarStep() {

    }

}
