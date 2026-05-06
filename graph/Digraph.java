package graph;

import java.util.ArrayList;
import java.util.List;

public class Digraph {
    private final int V;
    private int E;
    private List<Integer>[] adj;
    
    /**
     * Initializes an empty digraph with V vertices
     * @param V number of vertices
     */
    @SuppressWarnings("unchecked")
    public Digraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be non-negative");
        this.V = V;
        this.E = 0;
        adj = (List<Integer>[]) new List[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new ArrayList<>();
        }
    }
    
    /**
     * Returns the number of vertices in this digraph
     * @return number of vertices
     */
    public int V() {
        return V;
    }
    
    /**
     * Returns the number of edges in this digraph
     * @return number of edges
     */
    public int E() {
        return E;
    }
    
    /**
     * Validates that the vertex is within bounds
     * @param v vertex to validate
     */
    private void validateVertex(int v) {
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("Vertex " + v + " is not between 0 and " + (V-1));
        }
    }
    
    /**
     * Adds the directed edge v→w to this digraph
     * @param v tail vertex
     * @param w head vertex
     */
    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        adj[v].add(w);
        E++;
    }
    
    /**
     * Returns the vertices adjacent from vertex v in this digraph
     * @param v the vertex
     * @return the vertices adjacent from vertex v as an Iterable
     */
    public Iterable<Integer> adj(int v) {
        validateVertex(v);
        return adj[v];
    }
    
    /**
     * Returns the reverse of the digraph
     * @return the reverse of the digraph
     */
    public Digraph reverse() {
        Digraph reverse = new Digraph(V);
        for (int v = 0; v < V; v++) {
            for (int w : adj(v)) {
                reverse.addEdge(w, v);
            }
        }
        return reverse;
    }
    
    /**
     * Returns a string representation of this digraph
     * @return string representation
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V).append(" vertices, ").append(E).append(" edges\n");
        for (int v = 0; v < V; v++) {
            s.append(v).append(": ");
            for (int w : adj[v]) {
                s.append(w).append(" ");
            }
            s.append("\n");
        }
        return s.toString();
    }
}