package graph;

import java.util.List;

public interface Graph {
    int getVerticesCount();
    void addEdge(int source, int destination, int weight);
    List<Edge> getEdges(int vertex);
    List<Edge> getAllEdges();
    boolean isDirected();
}