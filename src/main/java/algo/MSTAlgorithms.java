package algo;

import graph.Edge;
import graph.Graph;
import metrics.Metrics;
import metrics.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class MSTAlgorithms {

    public static Result<List<Edge>> kruskal(Graph graph) {
        Metrics metrics = new Metrics();
        List<Edge> result = new ArrayList<>();
        List<Edge> edges = graph.getAllEdges();

        Collections.sort(edges, (e1, e2) -> {
            metrics.addComparison();
            return Integer.compare(e1.getWeight(), e2.getWeight());
        });

        DSU dsu = new DSU(graph.getVerticesCount(), metrics);

        for (Edge edge : edges) {
            metrics.addEdgeScan();
            if (dsu.union(edge.getSource(), edge.getDestination())) {
                result.add(edge);
            }
        }

        return new Result<>(result, metrics);
    }

    public static Result<List<Edge>> prim(Graph graph) {
        Metrics metrics = new Metrics();
        List<Edge> result = new ArrayList<>();
        int n = graph.getVerticesCount();
        if (n == 0) return new Result<>(result, metrics);

        boolean[] inMST = new boolean[n];
        PriorityQueue<Edge> pq = new PriorityQueue<>((e1, e2) -> {
            metrics.addComparison();
            return Integer.compare(e1.getWeight(), e2.getWeight());
        });

        inMST[0] = true;
        for (Edge edge : graph.getEdges(0)) {
            metrics.addEdgeScan();
            pq.add(edge);
        }

        while (!pq.isEmpty()) {
            Edge edge = pq.poll();
            metrics.addEdgeScan();
            int u = edge.getDestination();

            if (inMST[u]) continue;

            inMST[u] = true;
            result.add(edge);

            for (Edge nextEdge : graph.getEdges(u)) {
                metrics.addEdgeScan();
                if (!inMST[nextEdge.getDestination()]) {
                    pq.add(nextEdge);
                }
            }
        }

        return new Result<>(result, metrics);
    }
}