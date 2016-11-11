package net.xomak.sga2.graph;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a path in the graph
 */
public class Path implements Comparable<Path> {
    public List<Edge> getEdges() {
        return edges;
    }

    private List<Edge> edges;

    public double getWeight() {
        return weight;
    }

    private double weight;

    public Edge getByIndex(final int i) {
        return edges.get(i);
    }

    public int size() {
        return this.edges.size();
    }

    public int indexOf(final Edge requiredEdge) {
        return edges.indexOf(requiredEdge);
    }

    public boolean containsEdge(final Edge edge) {
        return edges.contains(edge);
    }

    public Vertex getStartVertex() {
        if(this.size() > 0) {
            return edges.get(0).getFrom();
        }
        else {
            return null;
        }
    }

    public String toString() {
        return "{(" + weight + "): " + edges + "}";
    }


    public Edge getLastEdge() {
        if(this.size() > 0) {
           return edges.get(edges.size() - 1);
        }
        return null;
    }

    public Path getSubPath(final int indexTo) {
        List<Edge> newPath = new LinkedList<>();
        int i = 0;
        for(Edge edge : this.edges) {
            if(i < indexTo) {
                newPath.add(edge);
                i++;
            }
            else break;
        }
        return new Path(newPath);
    }

    public void insertAtBeginning(final Path path) {
        if(edges.size() > 0 && path.size() > 0) {
            if (path.getLastEdge().getTo().equals(getStartVertex())) {
                this.weight += path.getWeight();
                this.edges.addAll(0, path.edges);
            }
        }
        else {
            this.weight = path.getWeight() + this.getWeight();
            this.edges.addAll(path.edges);
        }
    }

    public Path() {
        this.edges = new LinkedList<>();
        this.weight = 0;
    }

    private double getPathWeight(final List<Edge> edges) {
        double weight = 0;
        for(Edge edge : edges) {
            weight += edge.getWeight();
        }
        return weight;
    }

    public Path(final List<Edge> edges) {
        this.edges = edges;
        this.weight = getPathWeight(edges);
    }

    @Override
    public int compareTo(final Path o) {
        return Double.compare(this.weight, o.weight);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Path)) return false;

        Path path = (Path) o;

        return edges.equals(path.edges);

    }

    @Override
    public int hashCode() {
        return edges.hashCode();
    }
}