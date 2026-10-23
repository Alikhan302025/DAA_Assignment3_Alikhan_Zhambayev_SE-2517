package graph;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdjacencyListGraphTest {

    @Test
    void testGraphCreationAndEdges() {
        Graph graph = new AdjacencyListGraph(4, true);
        graph.addEdge(0, 1, 10);
        graph.addEdge(1, 2, 20);

        assertEquals(4, graph.getVerticesCount());
        assertTrue(graph.isDirected());
        assertEquals(1, graph.getEdges(0).size());
        assertEquals(2, graph.getAllEdges().size());
    }

    @Test
    void testSelfLoopsAndIsolatedVertices() {
        Graph graph = new AdjacencyListGraph(3, false);
        graph.addEdge(0, 0, 5); // Петля

        assertEquals(1, graph.getEdges(0).size());
        assertEquals(0, graph.getEdges(1).size()); // Изолированная вершина
    }

    @Test
    void testInvalidVertexAccess() {
        Graph graph = new AdjacencyListGraph(3, true);
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge(-1, 2, 5));
        assertThrows(IllegalArgumentException.class, () -> graph.getEdges(5));
    }
}
