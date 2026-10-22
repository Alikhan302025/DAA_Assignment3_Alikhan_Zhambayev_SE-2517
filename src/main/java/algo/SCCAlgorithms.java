package algo;

import graph.AdjacencyListGraph;
import graph.Edge;
import graph.Graph;
import metrics.Metrics;
import metrics.Result;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class SCCAlgorithms {

    public static Result<List<List<Integer>>> kosaraju(Graph graph) {
        Metrics metrics = new Metrics();
        int n = graph.getVerticesCount();
        List<List<Integer>> sccs = new ArrayList<>();
        if (n == 0) return new Result<>(sccs, metrics);

        boolean[] visited = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                iterativeDfsFirstPass(graph, i, visited, stack, metrics);
            }
        }

        Graph transpose = transposeGraph(graph);

        boolean[] visitedTranspose = new boolean[n];
        while (!stack.isEmpty()) {
            int u = stack.pop();
            if (!visitedTranspose[u]) {
                List<Integer> component = new ArrayList<>();
                iterativeDfsSecondPass(transpose, u, visitedTranspose, component, metrics);
                sccs.add(component);
            }
        }

        return new Result<>(sccs, metrics);
    }

    private static void iterativeDfsFirstPass(Graph graph, int start, boolean[] visited, Deque<Integer> stack, Metrics metrics) {
        Deque<int[]> dfsStack = new ArrayDeque<>();
        dfsStack.push(new int[]{start, 0});
        visited[start] = true;

        while (!dfsStack.isEmpty()) {
            metrics.updateMaxDepth(dfsStack.size());
            int[] top = dfsStack.peek();
            int u = top[0];
            int edgeIdx = top[1];

            List<Edge> edges = graph.getEdges(u);
            if (edgeIdx < edges.size()) {
                top[1]++;
                metrics.addEdgeScan();
                Edge edge = edges.get(edgeIdx);
                int v = edge.getDestination();
                if (!visited[v]) {
                    visited[v] = true;
                    dfsStack.push(new int[]{v, 0});
                }
            } else {
                dfsStack.pop();
                stack.push(u);
            }
        }
    }

    private static void iterativeDfsSecondPass(Graph graph, int start, boolean[] visited, List<Integer> component, Metrics metrics) {
        Deque<Integer> dfsStack = new ArrayDeque<>();
        dfsStack.push(start);
        visited[start] = true;

        while (!dfsStack.isEmpty()) {
            metrics.updateMaxDepth(dfsStack.size());
            int u = dfsStack.pop();
            component.add(u);

            for (Edge edge : graph.getEdges(u)) {
                metrics.addEdgeScan();
                int v = edge.getDestination();
                if (!visited[v]) {
                    visited[v] = true;
                    dfsStack.push(v);
                }
            }
        }
    }

    private static Graph transposeGraph(Graph graph) {
        int n = graph.getVerticesCount();
        Graph transpose = new AdjacencyListGraph(n, true);
        for (Edge edge : graph.getAllEdges()) {
            transpose.addEdge(edge.getDestination(), edge.getSource(), edge.getWeight());
        }
        return transpose;
    }

    public static Result<List<Integer>> kahn(Graph graph) {
        Metrics metrics = new Metrics();
        int n = graph.getVerticesCount();
        int[] inDegree = new int[n];

        for (Edge edge : graph.getAllEdges()) {
            metrics.addEdgeScan();
            inDegree[edge.getDestination()]++;
        }

        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        List<Integer> topoOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            metrics.updateMaxDepth(queue.size());
            int u = queue.poll();
            topoOrder.add(u);

            for (Edge edge : graph.getEdges(u)) {
                metrics.addEdgeScan();
                int v = edge.getDestination();
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    queue.add(v);
                }
            }
        }

        return new Result<>(topoOrder, metrics);
    }
}