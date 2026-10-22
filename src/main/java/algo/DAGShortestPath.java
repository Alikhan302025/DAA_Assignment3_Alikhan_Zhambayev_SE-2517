package algo;

import graph.Edge;
import graph.Graph;
import metrics.Metrics;
import metrics.Result;

import java.util.Arrays;
import java.util.List;

public class DAGShortestPath {

    public static Result<int[]> findShortestPath(Graph graph, int source) {
        Metrics metrics = new Metrics();
        int n = graph.getVerticesCount();
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        Result<List<Integer>> topoResult = SCCAlgorithms.kahn(graph);
        List<Integer> topoOrder = topoResult.getValue();

        Metrics topoMetrics = topoResult.getMetrics();
        for (long i = 0; i < topoMetrics.getEdgeScans(); i++) metrics.addEdgeScan();
        for (long i = 0; i < topoMetrics.getComparisons(); i++) metrics.addComparison();

        for (int u : topoOrder) {
            if (dist[u] != Integer.MAX_VALUE) {
                for (Edge edge : graph.getEdges(u)) {
                    metrics.addEdgeScan();
                    int v = edge.getDestination();
                    int weight = edge.getWeight();
                    metrics.addComparison();
                    if (dist[u] + weight < dist[v]) {
                        dist[v] = dist[u] + weight;
                    }
                }
            }
        }

        return new Result<>(dist, metrics);
    }
}