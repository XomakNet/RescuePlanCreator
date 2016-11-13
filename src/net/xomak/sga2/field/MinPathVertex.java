package net.xomak.sga2.field;

import net.xomak.sga2.graph.Edge;
import net.xomak.sga2.graph.SimpleEdge;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashSet;
import java.util.Set;


/**
 * Vertex in graph, constructed as such vertexes and edges with weights, equal to the geometric length between them.
 * Build one vertex - another will be constructed recursively.
 */
public class MinPathVertex extends VertexWithNode {

    private Node node;
    private Field field;
    private VertexesWithNodeContainer<MinPathVertex> container;

    public MinPathVertex(final Field field, int x, int y, VertexesWithNodeContainer container) {
        this.field = field;
        this.node = field.getNodeByCoordinates(x, y);
        this.container = container;
    }


    @Override
    public long getId() {
        return node.getId();
    }

    @Override
    public Set<Edge> getOutgoingEdges() {
        throw new NotImplementedException();
    }

    @Override
    public Set<Edge> getIncomingEdges() {
        Set<Edge> edges = new HashSet<>();
        for (Node currentNode : field.getAchievableNodes(node)) {
            double weight = (currentNode.getX() != node.getX() && currentNode.getY() != node.getY()) ? Math.sqrt(2) : 1;
            MinPathVertex vertex = container.getVertex(new MinPathVertex(field, currentNode.getX(), currentNode.getY(),
                    container));
            edges.add(new SimpleEdge(vertex, this, weight));
        }
        return edges;
    }

    @Override
    public String toString() {
        return "MinPathVertex{" +
                "node=" + node +
                '}';
    }

    public Node getNode() {
        return node;
    }
}