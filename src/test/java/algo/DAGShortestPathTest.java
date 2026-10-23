package algo;

import graph.AdjacencyListGraph;
import graph.Graph;
import metrics.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DAGShortestPathTest {

    @Test
    void testShortestPath() {
        Graph graph = new AdjacencyListGraph(4, true);
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 3);

        Result<int[]> result = DAGShortestPath.findShortestPath(graph, 0);
        int[] dist = result.getValue();

        assertEquals(0, dist[0]);
        assertEquals(2, dist[1]);
        assertEquals(3, dist[2]); // 0 -> 1 -> 2
        assertEquals(6, dist[3]); // 0 -> 1 -> 2 -> 3
    }
}
