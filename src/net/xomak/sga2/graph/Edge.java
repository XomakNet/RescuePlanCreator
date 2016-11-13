package net.xomak.sga2.graph;

public interface Edge {
    Vertex getOppositeVertex(final Vertex vertex);

    Vertex getTo();

    Vertex getFrom();

    Double getWeight();
}
