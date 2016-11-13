package net.xomak.sga2.algorithms;

import net.xomak.sga2.field.Node;
import net.xomak.sga2.field.VertexWithNode;
import net.xomak.sga2.graph.Edge;
import net.xomak.sga2.graph.Vertex;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class AStarGraph extends AStar<AStarGraphStep> {

    private Map<Vertex, Double> radiationDistances;
    private Map<Vertex, Double> pathsByMinimalRadiation;
    private Map<Vertex, Double> geometricDistances;
    private VertexWithNode startVertex;
    private VertexWithNode finishVertex;
    private Double maxGeometricDistance;
    private boolean isInversed;
    private double[][] radiation;

    public AStarGraph(final Map<Vertex, Double> radiationDistances,
                      final Map<Vertex, Double> geometricDistances, final Map<Vertex, Double> pathsByMinimalRadiation, final double[][] radiation, final VertexWithNode startVertex,
                      final VertexWithNode finishVertex, final boolean isInversed, final Double maxGeometricDistance) {
        super(new AStarGraphStep(startVertex, null, 0.0, 0.0, radiationDistances.get(startVertex), geometricDistances.get(startVertex), pathsByMinimalRadiation.get(startVertex)));
        this.radiationDistances = radiationDistances;
        this.geometricDistances = geometricDistances;
        this.startVertex = startVertex;
        this.finishVertex = finishVertex;
        this.maxGeometricDistance = maxGeometricDistance;
        this.isInversed = isInversed;
        this.pathsByMinimalRadiation = pathsByMinimalRadiation;
        this.radiation = radiation;
    }

    protected Set<Edge> getEdgesFrom(Vertex vertex) {
        if (isInversed) {
            return vertex.getIncomingEdges();
        } else {
            return vertex.getOutgoingEdges();
        }
    }


    protected Double getRadiationIn(final VertexWithNode vertex) {
        Node node = vertex.getNode();
        return this.radiation[node.getX()][node.getY()];
    }

    @Override
    protected Set<AStarGraphStep> getStepsFrom(final AStarGraphStep currentStep) {
        Set<AStarGraphStep> steps = new HashSet<>();
        VertexWithNode vertex = currentStep.getStepVertex();
        for (Edge edge : getEdgesFrom(vertex)) {
            VertexWithNode nextVertex = (VertexWithNode) edge.getOppositeVertex(vertex);
            Double newRadiation = currentStep.getCurrentRadiationLevel() + getRadiationIn(nextVertex);
            Double newPathLength = currentStep.getCurrentPathLength() + edge.getWeight();
            AStarGraphStep newStep = new AStarGraphStep(nextVertex, currentStep, newRadiation, newPathLength, radiationDistances.get(nextVertex), geometricDistances.get(nextVertex), pathsByMinimalRadiation.get(nextVertex));
            steps.add(newStep);
        }
        return steps;
    }

    @Override
    protected boolean isLastStep(final AStarGraphStep step) {
        return step.getStepVertex().equals(finishVertex);
    }

    @Override
    protected boolean shouldConsider(final AStarGraphStep step) {
        //System.out.println(step);
//        if(step.getVisitedVertexesIds().contains(step.getStepVertex().getId())) {
//            return false;
//        }
        AStarGraphStep current = step.getPreviousStep();
        while (current != null) {
            if (current.equals(step)) {
                return false;
            }
            current = current.getPreviousStep();
        }
        if (step.getPotentialPathLength() < maxGeometricDistance) {
            return true;
        } else {
            return false;
        }
    }

}
