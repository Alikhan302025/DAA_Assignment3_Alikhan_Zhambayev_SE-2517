package metrics;

public class Result<T> {
    private final T value;
    private final Metrics metrics;

    public Result(T value, Metrics metrics) {
        this.value = value;
        this.metrics = metrics;
    }

    public T getValue() {
        return value;
    }

    public Metrics getMetrics() {
        return metrics;
    }
}
