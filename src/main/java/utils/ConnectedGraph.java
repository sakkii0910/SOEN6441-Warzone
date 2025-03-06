package utils;

import java.util.ArrayList;

/**
 * A class to check if the graph is strongly connected or not
 */
public class ConnectedGraph {
    private int d_Vertices;
    private ArrayList[] d_Edges;

    ConnectedGraph(int p_Val) {
        this.d_Vertices = p_Val;
        d_Edges = new ArrayList[p_Val];
        for (int l_Index = 0; l_Index < p_Val; ++l_Index) {
            d_Edges[l_Index] = new ArrayList();
        }
    }

    /**
     * A function to add the  directions between nodes (connecting the nodes)
     *
     * @param p_Egde1 The first vertex
     * @param p_Edge2 The second vertex
     */
    void addEdge(int p_Egde1, int p_Edge2) {
        d_Edges[p_Egde1].add(p_Edge2);
    }

    /**
     * A function to perform DFS Traversal
     *
     * @param p_Node    The node where the DFS Traversal starts
     * @param p_Visited The array which holds the boolean value if node visited or not
     */
    private void dfsTraversal(int p_Node, Boolean[] p_Visited) {
        p_Visited[p_Node] = true;
        for (Integer l_Integer : (Iterable<Integer>) d_Edges[p_Node]) {
            int l_NextNode;
            l_NextNode = l_Integer;
            if (!p_Visited[l_NextNode]) {
                dfsTraversal(l_NextNode, p_Visited);
            }
        }
    }

    /**
     * A function to get the Transpose of the Graph
     *
     * @return the revered graph
     */
    private ConnectedGraph getTranspose() {
        ConnectedGraph l_Graph = new ConnectedGraph(d_Vertices);
        for (int l_Vertex = 0; l_Vertex < d_Vertices; l_Vertex++) {
            for (Integer l_Integer : (Iterable<Integer>) d_Edges[l_Vertex]) {
                l_Graph.d_Edges[l_Integer].add(l_Vertex);
            }
        }
        return l_Graph;
    }

    /**
     * A function to check if the graph is strongly connected or not
     * It checks if the graph is connected in both directions
     * @return true if strongly connected else false
     */
    Boolean checkIfStronglyConnected() {
        Boolean[] l_Visited = new Boolean[d_Vertices];
        for (int l_Index = 0; l_Index < d_Vertices; l_Index++) {
            l_Visited[l_Index] = false;
        }
        dfsTraversal(0, l_Visited);

        for (int l_Index = 0; l_Index < d_Vertices; l_Index++) {
            if (!l_Visited[l_Index]) {
                return false;
            }
        }
        ConnectedGraph l_Graph = getTranspose();
        for (int l_Index = 0; l_Index < d_Vertices; l_Index++) {
            l_Visited[l_Index] = false;
        }

        l_Graph.dfsTraversal(0, l_Visited);

        for (int l_i = 0; l_i < d_Vertices; l_i++) {
            if (!l_Visited[l_i]) {
                return false;
            }
        }
        return true;
    }
}