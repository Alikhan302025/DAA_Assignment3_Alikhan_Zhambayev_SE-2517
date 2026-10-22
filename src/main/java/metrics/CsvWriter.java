package metrics;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CsvWriter {

    public static void writeHeader(String filename) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename, false))) {
            out.println("Algorithm,Vertices,Edges,DsuOps,EdgeScans,Comparisons,MaxDepth,TimeNs");
        } catch (IOException e) {
            System.err.println("Ошибка при записи заголовков CSV: " + e.getMessage());
        }
    }

    public static void writeRecord(String filename, String algoName, int vertices, int edges, Metrics metrics, long timeNs) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename, true))) {
            out.printf("%s,%d,%d,%d,%d,%d,%d,%d\n",
                    algoName,
                    vertices,
                    edges,
                    metrics.getDsuOps(),
                    metrics.getEdgeScans(),
                    metrics.getComparisons(),
                    metrics.getMaxDepth(),
                    timeNs);
        } catch (IOException e) {
            System.err.println("Ошибка при записи строки CSV: " + e.getMessage());
        }
    }
}