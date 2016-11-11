package net.xomak.sga2.graph;

import java.util.Set;

/**
 * Created by regis on 11.11.2016.
 */
public interface Vertex {
    long getId();
    Set<Edge> getOutgoingEdges();
    Set<Edge> getIncomingEdges();
}
