package graph;

/**
 * Represents a directed edge in a digraph
 */
public class DirectedEdge {
    private final int from;
    private final int to;
    
    /**
     * Initializes a directed edge from vertex v to vertex w
     * @param from tail vertex
     * @param to head vertex
     */
    public DirectedEdge(int from, int to) {
        if (from < 0) throw new IllegalArgumentException("Vertex names must be non-negative");
        if (to < 0) throw new IllegalArgumentException("Vertex names must be non-negative");
        this.from = from;
        this.to = to;
    }
    
    /**
     * Returns the tail vertex of the directed edge
     * @return tail vertex
     */
    public int from() {
        return from;
    }
    
    /**
     * Returns the head vertex of the directed edge
     * @return head vertex
     */
    public int to() {
        return to;
    }
    
    /**
     * Returns a string representation of this edge
     * @return string representation
     */
    @Override
    public String toString() {
        return from + "->" + to;
    }
}