package metrics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CsvWriter {

    public static void writeHeader(String filename) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename, false))) {
            out.println("algorithm,graph,n,m,time_ms,dsu_ops,edge_scans,comparisons,max_depth,result");
        } catch (IOException e) {
            System.err.println("Ошибка при записи заголовков CSV: " + e.getMessage());
        }
    }

    public static void writeRecord(String filename, String algoName, String graphType, int n, int m, Metrics metrics, long timeNs, long resultValue) {
        long timeMs = timeNs / 1_000_000;
        try (PrintWriter out = new PrintWriter(new FileWriter(filename, true))) {
            out.printf("%s,%s,%d,%d,%d,%d,%d,%d,%d,%d\n",
                    algoName,
                    graphType,
                    n,
                    m,
                    timeMs,
                    metrics.getDsuOps(),
                    metrics.getEdgeScans(),
                    metrics.getComparisons(),
                    metrics.getMaxDepth(),
                    resultValue);
        } catch (IOException e) {
            System.err.println("Ошибка при записи строки CSV: " + e.getMessage());
        }
    }
}