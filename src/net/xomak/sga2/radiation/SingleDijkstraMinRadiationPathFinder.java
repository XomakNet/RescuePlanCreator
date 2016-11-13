package net.xomak.sga2.radiation;

import net.xomak.sga2.algorithms.Dijkstra;
import net.xomak.sga2.field.Field;
import net.xomak.sga2.field.Node;
import net.xomak.sga2.field.VertexesWithNodeContainer;
import net.xomak.sga2.graph.Path;
import net.xomak.sga2.graph.Vertex;

public class SingleDijkstraMinRadiationPathFinder implements MinRadiationPathFinder {

    private Path path;
    private Dijkstra dijkstra;
    private MaxLengthPathVertexAnalyzer analyzer;
    private Vertex finishVertex;
    private Vertex startVertex;

    public SingleDijkstraMinRadiationPathFinder(final Field field, final double[][] radiation, final Node from, final Node to, final int maxPathLength) {


        VertexesWithNodeContainer<MinRadiationVertex> radiationVertexesContainer = new VertexesWithNodeContainer<>();
        finishVertex = new MinRadiationVertex(field, to.getX(), to.getY(), radiation, radiationVertexesContainer);
        startVertex = new MinRadiationVertex(field, from.getX(), from.getY(), radiation, radiationVertexesContainer);
        dijkstra = new Dijkstra(finishVertex, true);

        analyzer = new MaxLengthPathVertexAnalyzer(finishVertex, dijkstra, maxPathLength);
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

