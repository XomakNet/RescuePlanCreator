package net.xomak.sga2.radiation;

import net.xomak.sga2.algorithms.Dijkstra;
import net.xomak.sga2.field.Node;
import net.xomak.sga2.field.VertexWithNode;
import net.xomak.sga2.graph.Vertex;
import net.xomak.sga2.graph.VertexAnalyzer;

import java.util.HashMap;
import java.util.Map;

/**
 * VertexAnalyzer, intended to restrict max path in the Graph, when algorithm is based on another weight
 * For example, there are edges' weights, sum of which should be minimal, but also there is a restriction, that
 * number of all nodes in path should not exceed some particular value.
 * In this case this analyzer will be useful.
 */
class MaxLengthPathVertexAnalyzer implements VertexAnalyzer {
    private Vertex finishVertex;
    private double maxLength;
    private Map<Vertex, Double> distancesToFinish;
    private Dijkstra dijkstra;
    private Map<Vertex, Double> realDistances;

    /**
     * Creates instance of an Anayzer with "prediction stop"
     * Pass distancesToFinish with distances to some point and, if on some step total distance to this vertex
     * will be more then maxPath, vertexes will be dropped.
     *
     * @param startVertex       Algorithm's start vertex (path to here is 0)
     * @param dijkstra          Instance of a Dijkstra's algorithm
     * @param maxLength         Path's max length
     * @param distancesToFinish Precalculated distances from all vertex to some
     */
    public MaxLengthPathVertexAnalyzer(final Vertex startVertex, final Dijkstra dijkstra, final double maxLength,
                                       final Map<Vertex, Double> distancesToFinish) {
        this.finishVertex = startVertex;
        this.dijkstra = dijkstra;
        this.maxLength = maxLength;
        this.distancesToFinish = distancesToFinish;
        realDistances = new HashMap<>();
        realDistances.put(startVertex, 0.0);
    }

    /**
     * Creates instance of an Anayzer
     *
     * @param startVertex Algorithm's start vertex (path to here is 0)
     * @param dijkstra    Instance of a Dijkstra's algorithm
     * @param maxLength   Path's max length
     */
    public MaxLengthPathVertexAnalyzer(final Vertex startVertex, final Dijkstra dijkstra, final double maxLength) {
        this(startVertex, dijkstra, maxLength, null);
    }

    public Map<Vertex, Double> getRealDistances() {
        return realDistances;
    }

    protected double calculateWeight(final Node node1, final Node node2) {
        return (node1.getX() != node2.getX() && node1.getY() != node2.getY()) ? Math.sqrt(2) : 1;
    }

    @Override
    public boolean shouldConsider(final Vertex vertex, Double distance) {

        if (!vertex.equals(finishVertex)) {
            Vertex previous = dijkstra.getEdgeToStart(vertex).getOppositeVertex(vertex);
            Double previousDistance = realDistances.get(previous);
            Node currentNode = ((MinRadiationVertex) vertex).getNode();
            Node node = ((VertexWithNode) previous).getNode();
            double weight = calculateWeight(currentNode, node);
            double finalDistance = previousDistance + weight;

            realDistances.put(vertex, finalDistance);
            if (distancesToFinish != null) {
                Double potentialDistance = distancesToFinish.get(vertex);
                if (potentialDistance == null || finalDistance + potentialDistance > maxLength) {
                    return false;
                }
            } else if (finalDistance > maxLength) {
                return false;
            }
        }
        return true;
    }
}
