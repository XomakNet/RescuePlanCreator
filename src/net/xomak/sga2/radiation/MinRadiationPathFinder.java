package net.xomak.sga2.radiation;

import net.xomak.sga2.algorithms.Dijkstra;
import net.xomak.sga2.field.Field;
import net.xomak.sga2.field.Node;
import net.xomak.sga2.graph.Path;
import net.xomak.sga2.graph.Vertex;
import net.xomak.sga2.graph.VertexAnalyzer;

import java.util.HashMap;
import java.util.Map;

public class MinRadiationPathFinder {

    private Path path;
    private Dijkstra dijkstra;
    private RadiationVertexAnalyzer analyzer;
    private Vertex finishVertex;
    private Vertex startVertex;

    public MinRadiationPathFinder(final Field field, final double[][] radiation, final Node from, final Node to, final int maxPathLength) {

        finishVertex = new MinRadiationVertex(field, to.getX(), to.getY(), radiation);
        startVertex = new MinRadiationVertex(field, from.getX(), from.getY(), radiation);
        dijkstra = new Dijkstra(finishVertex, true);

        analyzer = new RadiationVertexAnalyzer(finishVertex, dijkstra, maxPathLength);
        dijkstra.setAnalyzer(analyzer);
    }

    public void run() {
        dijkstra.run();
    }

    public Path getPath() {
        return dijkstra.getPathToStartFrom(startVertex);
    }

    public double getPathLength() {
        return analyzer.getRealDistances().get(startVertex);
    }
}

class RadiationVertexAnalyzer implements VertexAnalyzer {

    private Vertex finishVertex;
    private int maxLength;

    public RadiationVertexAnalyzer(final Vertex finishVertex, final Dijkstra dijkstra, final int maxLength) {
        this.finishVertex = finishVertex;
        this.dijkstra = dijkstra;
        this.maxLength = maxLength;
        realDistances = new HashMap<>();
        realDistances.put(finishVertex, 0.0);
    }

    private Dijkstra dijkstra;

    public Map<Vertex, Double> getRealDistances() {
        return realDistances;
    }

    private Map<Vertex, Double> realDistances;

    @Override
    public boolean shouldConsider(final Vertex vertex, Double distance) {

        if(!vertex.equals(finishVertex)) {
            Vertex previous = dijkstra.getEdgeToStart(vertex).getOppositeVertex(vertex);
            Double previousDistance = realDistances.get(previous);
            Node currentNode = ((MinRadiationVertex)vertex).getNode();
            Node node = ((MinRadiationVertex)previous).getNode();
            double weight = (currentNode.getX() != node.getX() && currentNode.getY() != node.getY()) ? Math.sqrt(2) : 1;
            double finalDistance = previousDistance + weight;
            realDistances.put(vertex, finalDistance);
            if(finalDistance > maxLength) {
                return false;
            }
        }
        //MinRadiationVertex mpv = (MinRadiationVertex)vertex;
        //bufferedImage.setRGB(mpv.getNode().getX(), mpv.getNode().getY(), 9999999);
        return true;
    }
}