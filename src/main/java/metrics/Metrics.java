package metrics;

public class Metrics {
    private long dsuOps;
    private long edgeScans;
    private long comparisons;
    private long maxDepth;

    public Metrics() {
        this.dsuOps = 0;
        this.edgeScans = 0;
        this.comparisons = 0;
        this.maxDepth = 0;
    }

    public void addDsuOp() { this.dsuOps++; }
    public void addEdgeScan() { this.edgeScans++; }
    public void addComparison() { this.comparisons++; }

    public void updateMaxDepth(long depth) {
        if (depth > this.maxDepth) {
            this.maxDepth = depth;
        }
    }


    public long getDsuOps() { return dsuOps; }
    public long getEdgeScans() { return edgeScans; }
    public long getComparisons() { return comparisons; }
    public long getMaxDepth() { return maxDepth; }
}