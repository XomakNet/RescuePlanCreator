package net.xomak.sga2.radiation;

import net.xomak.sga2.algorithms.Dijkstra;
import net.xomak.sga2.field.*;
import net.xomak.sga2.graph.Path;
import net.xomak.sga2.graph.Vertex;

/**
 * Minimal radiation path with given pathLength restriction finder.
 * Uses two Dijkstra's algorithms. First, consider all paths with length < maxPathLength.
 * After that start Dijkstra's for minimal radiation path on this reduced graph.
 */
public class DoubleDijkstraMinRadiationPathFinder implements MinRadiationPathFinder {

    // Calculates minimal radiation paths from all to start
    private Dijkstra minRadiationDijkstra;
    // Calculates minimal pathes from all to finish
    private Dijkstra minPathDijkstra;
    private MaxLengthPathVertexAnalyzer minRadiationDijkstraAnalyzer;
    private Vertex finishVertexForRadiation;
    private Vertex startVertexForRadiation;

    public DoubleDijkstraMinRadiationPathFinder(final Field field, final double[][] radiation, final Node from,
                                                final Node to, final int maxPathLength) {

        VertexesWithNodeContainer<MinRadiationVertex> radiationVertexesContainer = new VertexesWithNodeContainer<>();
        finishVertexForRadiation = new MinRadiationVertex(field, to.getX(), to.getY(), radiation, radiationVertexesContainer);
        startVertexForRadiation = new MinRadiationVertex(field, from.getX(), from.getY(), radiation, radiationVertexesContainer);

        VertexesWithNodeContainer<MinPathVertex> minPathContainer = new VertexesWithNodeContainer<>();
        VertexWithNode startVertexForDistance = new MinPathVertex(field, from.getX(), from.getY(), minPathContainer);

        minPathDijkstra = new Dijkstra(startVertexForDistance, true);
        minPathDijkstra.setAnalyzer((vertex, currentDistance) -> {
            if (currentDistance > maxPathLength) {
                return false;
            }
            return true;
        });

        minRadiationDijkstra = new Dijkstra(finishVertexForRadiation, true);
        minRadiationDijkstraAnalyzer = new MaxLengthPathVertexAnalyzer(finishVertexForRadiation, minRadiationDijkstra,
                maxPathLength, minPathDijkstra.getDistances());
        minRadiationDijkstra.setAnalyzer(minRadiationDijkstraAnalyzer);
    }

    @Override
    public void run() {
        minPathDijkstra.run();
        minRadiationDijkstra.run();
    }

    @Override
    public Path getPath() {
        return minRadiationDijkstra.getPathToStartFrom(startVertexForRadiation);
    }

    @Override
    public double getPathLength() {
        return minRadiationDijkstraAnalyzer.getRealDistances().get(startVertexForRadiation);
    }
}
