package net.xomak.sga2.graph;


public interface VertexAnalyzer {
    boolean shouldConsider(final Vertex vertex, final Double currentDistance);
}
