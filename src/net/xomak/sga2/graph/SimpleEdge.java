package net.xomak.sga2.graph;


public class SimpleEdge implements Edge, Comparable<SimpleEdge> {
    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
    }

    public Double getWeight() {
        return weight;
    }

    private Vertex from;
    private Vertex to;
    private double weight;


    public SimpleEdge(final Vertex from, final Vertex to, final double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;

        Edge edge = (Edge) o;

        if (!from.equals(edge.getFrom())) return false;
        return to.equals(edge.getTo());

    }

    public Vertex getOppositeVertex(final Vertex vertex) {
        if (vertex.equals(this.from)) {
            return this.getTo();
        } else if (vertex.equals(this.to)) {
            return this.getFrom();
        } else {
            throw new IllegalArgumentException("Given vertex is not participating in this edge");
        }
    }

    @Override
    public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        return result;
    }

    @Override
    public int compareTo(final SimpleEdge o) {
        return Double.compare(this.weight, o.getWeight());
    }

    @Override
    public String toString() {
        return "{" + from.getId() + "-(" + weight + ")->" + to.getId() + "}";
    }
}
