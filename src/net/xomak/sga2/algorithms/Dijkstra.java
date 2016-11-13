package net.xomak.sga2.algorithms;

import net.xomak.sga2.graph.Edge;
import net.xomak.sga2.graph.Path;
import net.xomak.sga2.graph.Vertex;
import net.xomak.sga2.graph.VertexAnalyzer;

import java.util.*;

public class Dijkstra {

    private Map<Vertex, Double> distances = new HashMap<>();
    private Map<Vertex, Edge> edgesToStartVertex = new HashMap<>();
    private Set<Vertex> visited = new HashSet<>();
    private Set<Edge> excludedEdges = new HashSet<>();
    private Vertex startVertex;
    private boolean isSearchInversed;
    private PriorityQueue<Vertex> pendingVertexes = null;
    private VertexAnalyzer analyzer;

    public Dijkstra(final Vertex startVertex, boolean inversed) {
        this.isSearchInversed = inversed;
        this.startVertex = startVertex;
        VertexComparator vertexComparator = new VertexComparator(distances);
        this.pendingVertexes = new PriorityQueue<>(vertexComparator);
    }

    public Dijkstra(final Vertex startVertex, boolean inversed, VertexAnalyzer analyzer) {
        this.isSearchInversed = inversed;
        this.startVertex = startVertex;
        this.analyzer = analyzer;
        VertexComparator vertexComparator = new VertexComparator(distances);
        this.pendingVertexes = new PriorityQueue<>(vertexComparator);
    }

    public Map<Vertex, Double> getDistances() {
        return distances;
    }

    public void addExcludedEdge(final Edge excludedEdge) {
        this.excludedEdges.add(excludedEdge);
    }

    public Vertex getStartVertex() {
        return startVertex;
    }

    public VertexAnalyzer getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(final VertexAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public void run() {
        distances.put(startVertex, 0.0);
        pendingVertexes.add(startVertex);

        while (!pendingVertexes.isEmpty()) {
            Vertex currentVertex = pendingVertexes.poll();
            handleVertex(currentVertex);
        }
    }

    public Edge getEdgeToStart(final Vertex vertex) {
        return edgesToStartVertex.get(vertex);
    }

    public Double getDistanceFromStart(final Vertex vertex) {
        return distances.get(vertex);
    }

    public Path getPathToStartFrom(final Vertex vertex) {
        Vertex currentVertex = vertex;
        List<Edge> path = new LinkedList<>();
        while (!currentVertex.equals(startVertex)) {
            Edge currentEdge = edgesToStartVertex.get(currentVertex);
            if (currentEdge != null) {
                path.add(currentEdge);
                currentVertex = currentEdge.getOppositeVertex(currentVertex);
            } else {
                return null;
            }
        }
        if (!isSearchInversed) {
            Collections.reverse(path);
        }
        return new Path(path);
    }

    private void handleVertex(final Vertex vertex) {
        if (analyzer == null || analyzer.shouldConsider(vertex, distances.get(vertex))) {
            Set<Edge> edgesFromVertexSet = isSearchInversed ? vertex.getIncomingEdges() : vertex.getOutgoingEdges();
            List<Edge> edgesFromVertexList = new LinkedList<>(edgesFromVertexSet);
            while (!edgesFromVertexList.isEmpty()) {
                Edge currentEdge = edgesFromVertexList.remove(0);
                Vertex neighborVertex = currentEdge.getOppositeVertex(vertex);

                if (!visited.contains(neighborVertex) && !excludedEdges.contains(currentEdge)) {
                    double newDst = distances.get(vertex) + currentEdge.getWeight();
                    Double oldDst = distances.get(neighborVertex);
                    if (oldDst == null || newDst < oldDst) {
                        distances.put(neighborVertex, newDst);
                        edgesToStartVertex.put(neighborVertex, currentEdge);
                        if (pendingVertexes.contains(neighborVertex)) {
                            pendingVertexes.remove(neighborVertex);
                        }
                        pendingVertexes.add(neighborVertex);
                    }
                }
            }
        }
        visited.add(vertex);
    }

}

class VertexComparator implements Comparator<Vertex> {
    private Map<Vertex, Double> distances;

    public VertexComparator(final Map<Vertex, Double> distances) {
        this.distances = distances;
    }


    @Override
    public int compare(final Vertex o1, final Vertex o2) {
        Double dst1 = distances.get(o1);
        Double dst2 = distances.get(o2);
        if (dst1 == null && dst2 == null) {
            return 0;
        } else if (dst1 == null) {
            return -1;
        } else if (dst2 == null) {
            return 1;
        } else {
            return Double.compare(dst1, dst2);
        }
    }
}
