package bench;

import algo.DAGShortestPath;
import algo.MSTAlgorithms;
import algo.SCCAlgorithms;
import graph.AdjacencyListGraph;
import graph.Edge;
import graph.Graph;
import metrics.CsvWriter;
import metrics.Result;

import java.util.List;
import java.util.Random;

public class GraphBenchmark {
    private static final String CSV_FILE = "results.csv";

    // Единый Random(42) для воспроизводимости, как сказано в задании
    private static Random random;

    public static void runAll() {
        CsvWriter.writeHeader(CSV_FILE);

        int[] vertexCounts = {1000, 3162, 10000, 31623, 100000};

        for (int n : vertexCounts) {
            int mSparse = 4 * n;
            int mDense = 20 * n;

            runBenchmarksForSize(n, mSparse);
            runBenchmarksForSize(n, mDense);
        }
    }

    private static void runBenchmarksForSize(int n, int m) {
        // 1. Undirected graph for MST
        random = new Random(42);
        Graph undirectedGraph = generateRandomUndirectedGraph(n, m);

        long[] times = runMultiple(5, () -> MSTAlgorithms.kruskal(undirectedGraph));
        Result<List<Edge>> kruskalResult = MSTAlgorithms.kruskal(undirectedGraph);
        long kruskalWeight = kruskalResult.getValue().stream().mapToLong(Edge::getWeight).sum();
        CsvWriter.writeRecord(CSV_FILE, "Kruskal", "undirected", n, m, kruskalResult.getMetrics(), getMedian(times), kruskalWeight);

        times = runMultiple(5, () -> MSTAlgorithms.prim(undirectedGraph));
        Result<List<Edge>> primResult = MSTAlgorithms.prim(undirectedGraph);
        long primWeight = primResult.getValue().stream().mapToLong(Edge::getWeight).sum();
        CsvWriter.writeRecord(CSV_FILE, "Prim", "undirected", n, m, primResult.getMetrics(), getMedian(times), primWeight);

        // 2. Directed graph for SCC (Kosaraju)
        random = new Random(42);
        Graph directedGraph = generateRandomDirectedGraph(n, m);

        times = runMultiple(5, () -> SCCAlgorithms.kosaraju(directedGraph));
        Result<List<List<Integer>>> kosarajuResult = SCCAlgorithms.kosaraju(directedGraph);
        CsvWriter.writeRecord(CSV_FILE, "Kosaraju", "directed", n, m, kosarajuResult.getMetrics(), getMedian(times), kosarajuResult.getValue().size());

        // 3. DAG for Kahn and Shortest Paths
        random = new Random(42);
        Graph dag = generateRandomDAG(n, m);

        times = runMultiple(5, () -> SCCAlgorithms.kahn(dag));
        Result<List<Integer>> kahnResult = SCCAlgorithms.kahn(dag);
        CsvWriter.writeRecord(CSV_FILE, "Kahn", "DAG", n, m, kahnResult.getMetrics(), getMedian(times), kahnResult.getValue().size());

        times = runMultiple(5, () -> DAGShortestPath.findShortestPath(dag, 0));
        Result<int[]> dagResult = DAGShortestPath.findShortestPath(dag, 0);
        long distSum = 0;
        for (int d : dagResult.getValue()) {
            if (d != Integer.MAX_VALUE) {
                distSum += d;
            }
        }
        CsvWriter.writeRecord(CSV_FILE, "DagShortestPath", "DAG", n, m, dagResult.getMetrics(), getMedian(times), distSum);
    }

    private static Graph generateRandomUndirectedGraph(int n, int m) {
        Graph graph = new AdjacencyListGraph(n, false);
        int[] perm = new int[n];
        for (int i = 0; i < n; i++) perm[i] = i;

        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = perm[i];
            perm[i] = perm[j];
            perm[j] = temp;
        }

        for (int i = 1; i < n; i++) {
            int u = perm[random.nextInt(i)];
            int v = perm[i];
            int weight = 1 + random.nextInt(1000);
            graph.addEdge(u, v, weight);
        }

        int added = n - 1;
        while (added < m) {
            int u = random.nextInt(n);
            int v = random.nextInt(n);
            if (u != v) {
                int weight = 1 + random.nextInt(1000);
                graph.addEdge(u, v, weight);
                added++;
            }
        }
        return graph;
    }

    private static Graph generateRandomDirectedGraph(int n, int m) {
        Graph graph = new AdjacencyListGraph(n, true);
        int added = 0;
        while (added < m) {
            int u = random.nextInt(n);
            int v = random.nextInt(n);
            if (u != v) {
                int weight = 1 + random.nextInt(1000);
                graph.addEdge(u, v, weight);
                added++;
            }
        }
        return graph;
    }

    private static Graph generateRandomDAG(int n, int m) {
        Graph graph = new AdjacencyListGraph(n, true);
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) rank[i] = i;

        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = rank[i];
            rank[i] = rank[j];
            rank[j] = temp;
        }

        int added = 0;
        while (added < m) {
            int u = random.nextInt(n);
            int v = random.nextInt(n);
            if (u != v) {
                if (rank[u] > rank[v]) {
                    int temp = u;
                    u = v;
                    v = temp;
                }
                int weight = 1 + random.nextInt(1000);
                graph.addEdge(u, v, weight);
                added++;
            }
        }
        return graph;
    }

    private static long[] runMultiple(int times, Runnable task) {
        long[] results = new long[times];
        for (int i = 0; i < times; i++) {
            long start = System.nanoTime();
            task.run();
            results[i] = System.nanoTime() - start;
        }
        return results;
    }

    private static long getMedian(long[] times) {
        java.util.Arrays.sort(times);
        return times[times.length / 2];
    }
}