package net.xomak.sga2.field;

import net.xomak.sga2.graph.Vertex;

/***
 * Defines the vertex with node from field.
 */
public abstract class VertexWithNode implements Vertex {
    /**
     * Gets associated node
     *
     * @return node
     */
    public abstract Node getNode();

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof VertexWithNode)) return false;

        VertexWithNode that = (VertexWithNode) o;

        return getNode().equals(that.getNode());

    }

    @Override
    public int hashCode() {
        return getNode().hashCode();
    }
}
