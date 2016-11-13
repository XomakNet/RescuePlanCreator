package net.xomak.sga2.field;

import java.util.HashMap;
import java.util.Map;

/**
 * Container for VertexWithNode, intended to prevent object dublicates
 *
 * @param <T> Exact type of VertexWithNode
 */
public class VertexesWithNodeContainer<T extends VertexWithNode> {
    private Map<Long, T> map = new HashMap<Long, T>();

    /**
     * Returns vertex with node.id equals to the node.id of given vertex, if it exists
     * or adds this vertex to the container.
     *
     * @param vertex Vertex to get
     * @return Vertex, which is guaranteed to be the only one with given node.id
     */
    public T getVertex(final T vertex) {
        T vertexToReturn = map.get(vertex.getId());
        if (vertexToReturn == null) {
            vertexToReturn = vertex;
            map.put(vertex.getId(), vertexToReturn);
        }
        return vertexToReturn;
    }
}
