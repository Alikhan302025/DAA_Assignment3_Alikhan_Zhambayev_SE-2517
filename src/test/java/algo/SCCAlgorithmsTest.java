package algo;

import graph.AdjacencyListGraph;
import graph.Graph;
import metrics.Result;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SCCAlgorithmsTest {

    @Test
    void testKosaraju() {
        Graph graph = new AdjacencyListGraph(5, true);
        graph.addEdge(1, 0, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(2, 1, 1);
        graph.addEdge(0, 3, 1);
        graph.addEdge(3, 4, 1);

        Result<List<List<Integer>>> result = SCCAlgorithms.kosaraju(graph);
        assertEquals(3, result.getValue().size());
    }

    @Test
    void testKahn() {
        Graph graph = new AdjacencyListGraph(4, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);

        Result<List<Integer>> result = SCCAlgorithms.kahn(graph);
        List<Integer> order = result.getValue();

        assertEquals(4, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(1) < order.indexOf(3));
    }
}