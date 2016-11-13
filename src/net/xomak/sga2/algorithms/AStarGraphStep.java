package net.xomak.sga2.algorithms;

import net.xomak.sga2.field.VertexWithNode;

/**
 * Created by regis on 13.11.2016.
 */
public class AStarGraphStep extends AStarStep {


    private VertexWithNode stepVertex;
    private Double distanceByMinRadiationPath;

    @Override
    public String toString() {
        return "AStarGraphStep{" +
                "heuristic=" + heuristic +
                "stepVertex=" + stepVertex +
                ", currentRadiationLevel=" + currentRadiationLevel +
                ", currentPathLength=" + currentPathLength +
                ", minPossibleRadiation=" + minPossibleRadiation +
                ", minPossiblePathLength=" + minPossiblePathLength +
                '}';
    }
    private AStarGraphStep previousStep;

    public AStarGraphStep(final VertexWithNode stepVertex, final AStarGraphStep previousStep, final Double currentRadiationLevel, final Double currentPathLength, final Double minPossibleRadiation, final Double minPossiblePathLength, final Double distanceByMinRadiationPath) {
        this.stepVertex = stepVertex;
        this.previousStep = previousStep;
        this.currentRadiationLevel = currentRadiationLevel;
        this.currentPathLength = currentPathLength;
        this.minPossibleRadiation = minPossibleRadiation;
        this.minPossiblePathLength = minPossiblePathLength;
        this.distanceByMinRadiationPath = distanceByMinRadiationPath;
        this.heuristic = this.currentRadiationLevel + minPossibleRadiation;
    }

    private Double currentRadiationLevel;

    public Double getCurrentRadiationLevel() {
        return currentRadiationLevel;
    }

    public Double getCurrentPathLength() {
        return currentPathLength;
    }

    public Double getPotentialPathLength() {
        return currentPathLength + minPossiblePathLength;
    }

    private Double currentPathLength;

    private Double minPossibleRadiation;
    private Double minPossiblePathLength;

    private Double heuristic;


    public Double getHeuristic() {

        return this.heuristic;
    }

    public VertexWithNode getStepVertex() {
        return stepVertex;
    }

    @Override
    public int compareTo(final AStarStep o) {
        if(o instanceof AStarGraphStep) {
            AStarGraphStep that = (AStarGraphStep)o;
            int byHeuristic = this.getHeuristic().compareTo(that.getHeuristic());
            if(byHeuristic == 0) {
                return this.getPotentialPathLength().compareTo(that.getPotentialPathLength());
            }
            return byHeuristic;
        } else {
            throw new IllegalArgumentException("Comparison between AStarStep and AStarGraphStep is not supported");
        }
    }

    @Override
    public AStarGraphStep getPreviousStep() {
        return previousStep;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof AStarGraphStep)) return false;

        AStarGraphStep that = (AStarGraphStep) o;

        return stepVertex.equals(that.stepVertex);

    }

    @Override
    public int hashCode() {
        return stepVertex.hashCode();
    }
}
