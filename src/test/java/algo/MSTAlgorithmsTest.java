package algo;

import graph.AdjacencyListGraph;
import graph.Edge;
import graph.Graph;
import metrics.Result;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MSTAlgorithmsTest {

    @Test
    void testKruskalAndPrimSameWeight() {
        Graph graph = new AdjacencyListGraph(4, false);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 3, 3);
        graph.addEdge(0, 3, 4);

        Result<List<Edge>> kruskalRes = MSTAlgorithms.kruskal(graph);
        Result<List<Edge>> primRes = MSTAlgorithms.prim(graph);

        int kruskalWeight = kruskalRes.getValue().stream().mapToInt(Edge::getWeight).sum();
        int primWeight = primRes.getValue().stream().mapToInt(Edge::getWeight).sum();

        assertEquals(6, kruskalWeight);
        assertEquals(6, primWeight);
        assertEquals(3, kruskalRes.getValue().size());
        assertEquals(3, primRes.getValue().size());
    }
}
