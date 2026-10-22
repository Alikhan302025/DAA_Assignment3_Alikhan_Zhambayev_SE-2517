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
    private static final Random random = new Random(42);

    public static void runAll() {
        CsvWriter.writeHeader(CSV_FILE);

        int[] vertexCounts = {10, 100, 500, 1000, 3000};

        for (int v : vertexCounts) {
            int sparseE = v * 2;
            int denseE = Math.min(v * (v - 1) / 2, v * 15);

            runMstBenchmarks(v, sparseE);
            runMstBenchmarks(v, denseE);

            runSccBenchmarks(v, sparseE);
            runSccBenchmarks(v, denseE);

            runDagBenchmarks(v, sparseE);
            runDagBenchmarks(v, denseE);
        }
    }

    private static void runMstBenchmarks(int v, int e) {
        Graph graph = generateRandomUndirectedGraph(v, e);

        long startTime = System.nanoTime();
        Result<List<Edge>> kruskalResult = MSTAlgorithms.kruskal(graph);
        long timeKruskal = System.nanoTime() - startTime;
        CsvWriter.writeRecord(CSV_FILE, "Kruskal", v, e, kruskalResult.getMetrics(), timeKruskal);

        startTime = System.nanoTime();
        Result<List<Edge>> primResult = MSTAlgorithms.prim(graph);
        long timePrim = System.nanoTime() - startTime;
        CsvWriter.writeRecord(CSV_FILE, "Prim", v, e, primResult.getMetrics(), timePrim);
    }

    private static void runSccBenchmarks(int v, int e) {
        Graph graph = generateRandomDirectedGraph(v, e);

        long startTime = System.nanoTime();
        Result<List<List<Integer>>> kosarajuResult = SCCAlgorithms.kosaraju(graph);
        long timeKosaraju = System.nanoTime() - startTime;
        CsvWriter.writeRecord(CSV_FILE, "Kosaraju", v, e, kosarajuResult.getMetrics(), timeKosaraju);

        startTime = System.nanoTime();
        Result<List<Integer>> kahnResult = SCCAlgorithms.kahn(graph);
        long timeKahn = System.nanoTime() - startTime;
        CsvWriter.writeRecord(CSV_FILE, "Kahn", v, e, kahnResult.getMetrics(), timeKahn);
    }

    private static void runDagBenchmarks(int v, int e) {
        Graph dag = generateRandomDAG(v, e);

        long startTime = System.nanoTime();
        Result<int[]> dagResult = DAGShortestPath.findShortestPath(dag, 0);
        long timeDag = System.nanoTime() - startTime;
        CsvWriter.writeRecord(CSV_FILE, "DAG_ShortestPath", v, e, dagResult.getMetrics(), timeDag);
    }

    public static Graph generateRandomUndirectedGraph(int vertices, int edges) {
        Graph graph = new AdjacencyListGraph(vertices, false);
        int added = 0;
        while (added < edges) {
            int u = random.nextInt(vertices);
            int v = random.nextInt(vertices);
            if (u != v) {
                int weight = random.nextInt(100) + 1;
                graph.addEdge(u, v, weight);
                added++;
            }
        }
        return graph;
    }

    public static Graph generateRandomDirectedGraph(int vertices, int edges) {
        Graph graph = new AdjacencyListGraph(vertices, true);
        int added = 0;
        while (added < edges) {
            int u = random.nextInt(vertices);
            int v = random.nextInt(vertices);
            if (u != v) {
                int weight = random.nextInt(100) + 1;
                graph.addEdge(u, v, weight);
                added++;
            }
        }
        return graph;
    }

    public static Graph generateRandomDAG(int vertices, int edges) {
        Graph graph = new AdjacencyListGraph(vertices, true);
        int added = 0;
        while (added < edges) {
            int u = random.nextInt(vertices);
            int v = random.nextInt(vertices);
            if (u < v) {
                int weight = random.nextInt(100) + 1;
                graph.addEdge(u, v, weight);
                added++;
            }
        }
        return graph;
    }
}