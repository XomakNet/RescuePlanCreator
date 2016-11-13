package net.xomak.sga2.radiation;

import net.xomak.sga2.algorithms.AStarGraph;
import net.xomak.sga2.algorithms.Dijkstra;
import net.xomak.sga2.field.*;
import net.xomak.sga2.graph.Path;
import net.xomak.sga2.graph.Vertex;

import java.util.Map;

/**
 * Created by regis on 13.11.2016.
 */
public class DijkstraAStarMinRadiationPathFinder implements MinRadiationPathFinder {

    private Dijkstra geometricDijkstra;
    private Dijkstra radiationDijkstra;
    private Field field;
    private Node from;
    private double[][] radiation;
    private VertexesWithNodeContainer<MinPathVertex> minPathContainer;
    private VertexWithNode finishVertexForDistance;
    MaxLengthPathVertexAnalyzer pathLengthAnalyzer;
    private AStarGraph ma;

    public DijkstraAStarMinRadiationPathFinder(final Field field, final double[][] radiation, final Node from,
                                                final Node to, final int maxPathLength) {
        this.field = field;
        this.from = from;
        this.radiation = radiation;

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
        geometricDijkstra.getDistances(), pathLengthAnalyzer.getRealDistances(), radiation,startVertexForDistance,
        finishVertexForDistance, true, 1250.0);
        System.out.println("Mega algorithm started");
        ma.run();
    }

    @Override
    public Path getPath() {
        System.out.println(ma.getMinPathLastStep());
        return null;
    }

    @Override
    public double getPathLength() {
        return 0;
    }
}
