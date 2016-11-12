package net.xomak.sga2.graph;

import java.util.Set;

public interface Vertex {
    long getId();
    Set<Edge> getOutgoingEdges();
    Set<Edge> getIncomingEdges();
}
