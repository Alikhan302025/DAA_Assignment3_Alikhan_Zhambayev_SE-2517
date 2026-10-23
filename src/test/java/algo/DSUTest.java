package algo;

import metrics.Metrics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DSUTest {

    @Test
    void testUnionAndFind() {
        Metrics metrics = new Metrics();
        DSU dsu = new DSU(5, metrics);

        assertTrue(dsu.union(0, 1));
        assertTrue(dsu.union(1, 2));
        assertFalse(dsu.union(0, 2)); // Уже в одном множестве

        assertEquals(dsu.find(0), dsu.find(2));
        assertNotEquals(dsu.find(0), dsu.find(3));
        assertTrue(metrics.getDsuOps() > 0);
    }
}
