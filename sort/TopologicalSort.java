package sort;

import graph.Digraph;
import java.util.*;

/**
 * Topological Sort implementation based on Algorithms, 4th Edition
 * Uses depth-first search to compute topological ordering
 */
public class TopologicalSort {
    private boolean[] marked;
    private boolean[] onStack;
    private int[] edgeTo;
    private Stack<Integer> reversePost;
    private boolean hasCycle;
    private List<Integer> cycle;
    
    /**
     * Determines whether the digraph has a topological order and,
     * if so, finds such a topological order
     * @param digraph the digraph
     */
    public TopologicalSort(Digraph digraph) {
        marked = new boolean[digraph.V()];
        onStack = new boolean[digraph.V()];
        edgeTo = new int[digraph.V()];
        reversePost = new Stack<>();
        hasCycle = false;
        cycle = null;
        
        // Run DFS from each vertex
        for (int v = 0; v < digraph.V(); v++) {
            if (!marked[v] && !hasCycle) {
                dfs(digraph, v);
            }
        }
    }
    
    /**
     * Depth-first search
     * @param digraph the digraph
     * @param v the vertex
     */
    private void dfs(Digraph digraph, int v) {
        marked[v] = true;
        onStack[v] = true;
        
        for (int w : digraph.adj(v)) {
            // Short circuit if cycle already found
            if (hasCycle) return;
            
            // Found new vertex, so recur
            else if (!marked[w]) {
                edgeTo[w] = v;
                dfs(digraph, w);
            }
            // Found a back edge (cycle)
            else if (onStack[w]) {
                hasCycle = true;
                cycle = new ArrayList<>();
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.add(x);
                }
                cycle.add(w);
                cycle.add(v);
                return;
            }
        }
        
        onStack[v] = false;
        reversePost.push(v);
    }
    
    /**
     * Returns true if the digraph has a cycle
     * @return true if the digraph has a cycle, false otherwise
     */
    public boolean hasCycle() {
        return hasCycle;
    }
    
    /**
     * Returns a cycle if the digraph has a cycle, null otherwise
     * @return a cycle (as an Iterable) if the digraph has a cycle, null otherwise
     */
    public Iterable<Integer> cycle() {
        if (!hasCycle) return null;
        Collections.reverse(cycle);
        return cycle;
    }
    
    /**
     * Returns true if the digraph has a topological order
     * @return true if the digraph has a topological order
     */
    public boolean hasOrder() {
        return !hasCycle;
    }
    
    /**
     * Returns a topological order if the digraph has a topological order.
     * The order is from first to last (prerequisites first, dependent courses last).
     * Since DFS reverse post-order gives us the correct topological order,
     * we reverse the stack to get first-to-last order.
     * 
     * @return a topological order of the vertices (first course to last course)
     */
    public Iterable<Integer> order() {
        if (hasCycle) return null;
        
        // The stack has the last-finished vertex on top
        // For topological order, we need to reverse it so prerequisites come first
        List<Integer> list = new ArrayList<>(reversePost);
        Collections.reverse(list);
        return list;
    }
    
    /**
     * Returns a topological order as a list (prerequisites first)
     * @return list of vertices in topological order
     */
    public List<Integer> getOrderList() {
        if (hasCycle) return null;
        List<Integer> list = new ArrayList<>(reversePost);
        Collections.reverse(list); // Reverse to get correct order
        return list;
    }
}