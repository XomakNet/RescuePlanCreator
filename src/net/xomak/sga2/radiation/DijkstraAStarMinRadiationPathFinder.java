package net.xomak.sga2.radiation;

import net.xomak.sga2.algorithms.AStarGraph;
import net.xomak.sga2.algorithms.AStarGraphStep;
import net.xomak.sga2.algorithms.Dijkstra;
import net.xomak.sga2.field.*;
import net.xomak.sga2.graph.Edge;
import net.xomak.sga2.graph.Path;
import net.xomak.sga2.graph.SimpleEdge;

import java.util.LinkedList;
import java.util.List;


public class DijkstraAStarMinRadiationPathFinder implements MinRadiationPathFinder {

    MaxLengthPathVertexAnalyzer pathLengthAnalyzer;
    private Dijkstra geometricDijkstra;
    private Dijkstra radiationDijkstra;
    private Field field;
    private Node from;
    private double[][] radiation;
    private VertexesWithNodeContainer<MinPathVertex> minPathContainer;
    private VertexWithNode finishVertexForDistance;
    private AStarGraph ma;
    private Double maxPathLength;

    public DijkstraAStarMinRadiationPathFinder(final Field field, final double[][] radiation, final Node from,
                                               final Node to, final int maxPathLength) {
        this.field = field;
        this.from = from;
        this.radiation = radiation;
        this.maxPathLength = (double) maxPathLength;

        VertexesWithNodeContainer<MinRadiationVertex> radiationVertexesContainer = new VertexesWithNodeContainer<>();
        MinRadiationVertex finishVertexForRadiation = new MinRadiationVertex(field, to.getX(), to.getY(), radiation, radiationVertexesContainer);
        radiationDijkstra = new Dijkstra(finishVertexForRadiation, true);
        pathLengthAnalyzer = new MaxLengthPathVertexAnalyzer(finishVertexForRadiation, radiationDijkstra, Double.POSITIVE_INFINITY);
        radiationDijkstra.setAnalyzer(pathLengthAnalyzer);

        minPathContainer = new VertexesWithNodeContainer<>();
        finishVertexForDistance = new MinPathVertex(field, to.getX(), to.getY(), minPathContainer);
        geometricDijkstra = new Dijkstra(finishVertexForDistance, true);
    }

    @Override
    public void run() {
        radiationDijkstra.run();
        geometricDijkstra.run();

        VertexWithNode startVertexForDistance = new MinPathVertex(field, from.getX(), from.getY(), minPathContainer);

        ma = new AStarGraph(radiationDijkstra.getDistances(),
                geometricDijkstra.getDistances(), pathLengthAnalyzer.getRealDistances(), radiation, startVertexForDistance,
                finishVertexForDistance, true, maxPathLength);
        ma.run();
    }

    @Override
    public Path getPath() {
        List<Edge> edges = new LinkedList<>();
        AStarGraphStep currentStep = ma.getMinPathLastStep();
        if (currentStep != null) {
            VertexWithNode prevVertex = currentStep.getStepVertex();
            currentStep = currentStep.getPreviousStep();
            while (currentStep != null) {
                VertexWithNode currentVertex = currentStep.getStepVertex();
                Node node = currentVertex.getNode();
                edges.add(new SimpleEdge(prevVertex, currentVertex, radiation[node.getX()][node.getY()]));
                prevVertex = currentVertex;
                currentStep = currentStep.getPreviousStep();
            }
            return new Path(edges);
        }
        return null;
    }

    @Override
    public double getPathLength() {
        return ma.getMinPathLastStep().getCurrentPathLength();
    }
}
