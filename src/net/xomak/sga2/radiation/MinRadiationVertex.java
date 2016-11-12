package net.xomak.sga2.radiation;

import net.xomak.sga2.field.Field;
import net.xomak.sga2.field.Node;
import net.xomak.sga2.field.VertexWithNode;
import net.xomak.sga2.graph.Edge;
import net.xomak.sga2.graph.SimpleEdge;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashSet;
import java.util.Set;

/**
 * Vertex in graph, constructed as such vertexes and edges with weights, equal to the weight of distance vertex.
 * Build one vertex - another will be constructed recursively.
 */
public class MinRadiationVertex extends VertexWithNode {

    private Node node;
    private Field field;
    private double[][] radiation;

    public MinRadiationVertex(final Field field, int x, int y, double[][] radiation) {
        this.field = field;
        this.node = field.getNodeByCoordinates(x, y);
        this.radiation = radiation;
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
            edges.add(new SimpleEdge(new MinRadiationVertex(field, currentNode.getX(), currentNode.getY(), radiation), this,
                    radiation[currentNode.getX()][currentNode.getY()]));
        }
        return edges;
    }

    @Override
    public String toString() {
        return "MinRadiationVertex{" +
                "node=" + node +
                '}';
    }


    public Node getNode() {
        return node;
    }
}
