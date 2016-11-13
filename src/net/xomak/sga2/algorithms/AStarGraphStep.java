package net.xomak.sga2.algorithms;

import net.xomak.sga2.field.VertexWithNode;

import java.util.HashSet;

public class AStarGraphStep extends AStarStep {


    private VertexWithNode stepVertex;
    private Double distanceByMinRadiationPath;
    private HashSet<Long> visitedVertexesIds = new HashSet<>();
    private AStarGraphStep previousStep;
    private Double currentRadiationLevel;
    private Double currentPathLength;
    private Double minPossibleRadiation;
    private Double minPossiblePathLength;
    private Double heuristic;

    public AStarGraphStep(final VertexWithNode stepVertex, final AStarGraphStep previousStep, final Double currentRadiationLevel, final Double currentPathLength, final Double minPossibleRadiation, final Double minPossiblePathLength, final Double distanceByMinRadiationPath) {
        this.stepVertex = stepVertex;
        this.currentRadiationLevel = currentRadiationLevel;
        this.currentPathLength = currentPathLength;
        this.minPossibleRadiation = minPossibleRadiation;
        this.minPossiblePathLength = minPossiblePathLength;
        this.distanceByMinRadiationPath = distanceByMinRadiationPath;
        this.previousStep = previousStep;
        this.heuristic = this.currentRadiationLevel + minPossibleRadiation;
//        if(previousStep != null) {
//            visitedVertexesIds.addAll(previousStep.visitedVertexesIds);
//            visitedVertexesIds.add(previousStep.getStepVertex().getId());
//        }
    }

    public HashSet<Long> getVisitedVertexesIds() {
        return visitedVertexesIds;
    }

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

    public Double getCurrentRadiationLevel() {
        return currentRadiationLevel;
    }

    public Double getCurrentPathLength() {
        return currentPathLength;
    }

    public Double getPotentialPathLength() {
        return currentPathLength + minPossiblePathLength;
    }

    public Double getHeuristic() {

        return this.heuristic;
    }

    public VertexWithNode getStepVertex() {
        return stepVertex;
    }

    @Override
    public int compareTo(final AStarStep o) {
        if (o instanceof AStarGraphStep) {
            AStarGraphStep that = (AStarGraphStep) o;
//            if(that.distanceByMinRadiationPath + that.currentPathLength > 1280 && this.distanceByMinRadiationPath + that.currentPathLength < 1280) {
//                return -1;
//            }
//            else if(that.distanceByMinRadiationPath + that.currentPathLength < 1280 && this.distanceByMinRadiationPath + that.currentPathLength > 1280) {
//                return 1;
//            } else
            {
                int byHeuristic = this.getHeuristic().compareTo(that.getHeuristic());
                if (byHeuristic == 0) {
                    return this.getPotentialPathLength().compareTo(that.getPotentialPathLength());
                }
                return byHeuristic;
            }
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
