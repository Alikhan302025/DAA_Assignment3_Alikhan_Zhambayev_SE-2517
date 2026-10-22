package graph;

import java.util.ArrayList;
import java.util.List;

public class AdjacencyListGraph implements Graph {
    private final int vertices;
    private final boolean directed;
    private final List<List<Edge>> adjList;

    public AdjacencyListGraph(int vertices, boolean directed) {
        this.vertices = vertices;
        this.directed = directed;
        this.adjList = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) {
            adjList.add(new ArrayList<>());
        }
    }

    @Override
    public int getVerticesCount() {
        return vertices;
    }

    @Override
    public void addEdge(int source, int destination, int weight) {
        if (source < 0 || source >= vertices || destination < 0 || destination >= vertices) {
            throw new IllegalArgumentException();
        }

        adjList.get(source).add(new Edge(source, destination, weight));

        if (!directed && source != destination) {
            adjList.get(destination).add(new Edge(destination, source, weight));
        }
    }

    @Override
    public List<Edge> getEdges(int vertex) {
        if (vertex < 0 || vertex >= vertices) {
            throw new IllegalArgumentException();
        }
        return adjList.get(vertex);
    }

    @Override
    public List<Edge> getAllEdges() {
        List<Edge> allEdges = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            allEdges.addAll(adjList.get(i));
        }
        return allEdges;
    }

    @Override
    public boolean isDirected() {
        return directed;
    }
}