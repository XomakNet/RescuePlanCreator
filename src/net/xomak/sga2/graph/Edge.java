package net.xomak.sga2.graph;

/**
 * Created by regis on 11.11.2016.
 */
public interface Edge {
    Vertex getOppositeVertex(final Vertex vertex);
    Vertex getTo();
    Vertex getFrom();
    Double getWeight();
}
