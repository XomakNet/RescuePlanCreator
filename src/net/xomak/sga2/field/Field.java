package net.xomak.sga2.field;

import net.xomak.sga2.graph.Edge;
import net.xomak.sga2.graph.SimpleEdge;
import net.xomak.sga2.graph.Vertex;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashSet;
import java.util.Set;

public class Field {
    private Node pixels[][];
    private Node startNode;
    private Node finishNode;

    public Set<Node> getSourceNodes() {
        return sourceNodes;
    }

    private Set<Node> sourceNodes;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private int width;
    private int height;

    public Field(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new Node[width][height];
        sourceNodes = new HashSet<>();
    }

    public Node getNodeByCoordinates(final int x, final int y) {
        return pixels[x][y];
    }

    public void addNode(NodeType type, long nodeId, int x, int y) {
        Node node = new Node(type, nodeId, x, y);
        pixels[x][y] = node;

        if(type.equals(NodeType.START)) {
            startNode = node;
        }

        if(type.equals(NodeType.FINISH)) {
            finishNode = node;
        }

        if(type.equals(NodeType.SOURCE)) {
            sourceNodes.add(node);
        }
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * Returns set of achievable node (in one step) from current. Works only for START, FINISH and EMPTY nodes
     * @param node Node with type START, FINISH or EMPTY
     * @return Set of reachable nodes
     */
    public Set<Node> getAchievableNodes(final Node node) {
        int x = node.getX();
        int y = node.getY();
        Set<Node> nodes = new HashSet<>();
        for(int xOffset = -1; xOffset < 2; xOffset++) {
            for(int yOffset = -1; yOffset < 2; yOffset++) {
                if((xOffset != yOffset || xOffset != 0) && isInBounds(x+xOffset, y+yOffset)) {
                    Node current = this.getNodeByCoordinates(x+xOffset, y+yOffset);
                    if(!current.getType().equals(NodeType.SOURCE) && !current.getType().equals(NodeType.WALL)) {
                        nodes.add(current);
                    }
                }
            }
        }
        return nodes;
    }

    public Node getFinishNode() {
        return finishNode;
    }


    public Node getStartNode() {
        return startNode;
    }
}

