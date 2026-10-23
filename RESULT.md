# DAA Assignment 3: Graphs Report
**Author:** Alikhan Zhambayev  
**Group:** SE-2517

## 1. Complexity Table

| Algorithm | Time Complexity | Space Complexity | Justification |
| :--- | :--- | :--- | :--- |
| **DSU** | $O(\alpha(V))$ per op | $O(V)$ | Union by rank/size and path compression keep the tree depth almost constant (inverse Ackermann function $\alpha$). |
| **Kruskal's MST** | $O(E \log E)$ | $O(V + E)$ | Sorting the edges takes $O(E \log E)$; DSU operations take $O(E \alpha(V))$, which is dominated by sorting. |
| **Prim's MST** | $O(E \log V)$ | $O(V + E)$ | Using a min-priority queue, extracting the minimum and updating keys takes $O(\log V)$ for each of the $E$ edges. |
| **Kosaraju's SCC** | $O(V + E)$ | $O(V + E)$ | Requires exactly two DFS passes (forward and transposed). Each vertex and edge is visited a constant number of times. |
| **Kahn's Algo** | $O(V + E)$ | $O(V + E)$ | Iterates through all vertices and decreases in-degrees for all outgoing edges exactly once using a queue. |
| **DAG Shortest Path**| $O(V + E)$ | $O(V + E)$ | Edges are relaxed exactly once in topological order; no priority queue is needed. |

## 2. Counters vs Formulas (Empirical Validation)
To empirically confirm the complexity classes, we divide the measured operation counters from `results_2.csv` by the theoretical formulas ($m = E$):
*   **Kosaraju (`edge_scans` / $m$):** Stays exactly at **2.0** across all sizes, confirming the two full DFS passes.
*   **Kahn (`edge_scans` / $m$):** Stays exactly at **1.0**, confirming every edge is processed exactly once.
*   **DAG Shortest Path (`edge_scans` / $m$):** Stays exactly at **1.0**, confirming a single relaxation pass.
*   **Kruskal (`edge_scans` / $m$):** Stays exactly at **1.0** (all edges are scanned during the DSU union phase).

These constant ratios across graph sizes (from $n=1,000$ to $n=100,000$) prove that the algorithms behave exactly as their theoretical linear bounds dictate.

## 3. Correctness Arguments
**Cut Property (MST):**
The cut property states that for any cut of a graph dividing it into two disjoint sets of vertices, the minimum weight edge crossing this cut must belong to the Minimum Spanning Tree. **Kruskal's algorithm** inherently utilizes this by sorting edges globally; when it processes the lightest edge connecting two different components, it is picking the lightest edge across the cut separating those sets. **Prim's algorithm** actively maintains a single growing tree and a cut between the tree and the remaining vertices, greedily selecting the absolute lightest edge crossing that boundary at each step.

**Induction on Topological Order (DAG Shortest Paths):**
In a Directed Acyclic Graph, a topological sort guarantees that for every directed edge $u \to v$, vertex $u$ is strictly processed before vertex $v$. By the principle of induction, when the algorithm reaches vertex $v$, all possible incoming paths from its predecessors ($u$) have already been fully resolved and optimally relaxed. Therefore, a single linear pass over the topological order is mathematically sufficient to compute all shortest paths. This also explains why negative weights are safely allowed here: since there are no cycles, infinite negative loops are impossible.

## 4. Which Algorithm When?

| Scenario / Graph Type | Recommended Algorithm | Reason / Justification |
| :--- | :--- | :--- |
| **Sparse Graph ($m \approx 4n$)** | Kruskal or Prim | Both perform excellently, but Kruskal is often simpler to reason about. Prim does slightly fewer comparisons. |
| **Dense Graph ($m \approx 20n$)** | Prim | $O(E \log V)$ scales significantly better than Kruskal's $O(E \log E)$ edge sorting overhead when $E \approx V^2$. |
| **Detecting Deadlocks/Cycles** | Kahn's Algorithm | If the queue empties before all $V$ vertices are processed, a cycle is immediately and safely detected. |
| **Finding strongly connected parts** | Kosaraju's Algorithm | Identifies independent isolated clusters efficiently using robust iterative DFS passes (avoiding StackOverflow). |
| **Routing in a dependency tree** | DAG Shortest Path | Runs in absolute optimal $O(V+E)$ time without the heavy $O(E \log V)$ priority queue overhead of Dijkstra. |

## 5. Slope Check & Hardware Independence
**Slope Check:** On a log-log axis, the slope of `edge_scans` vs $n$ (where $m \propto n$) fits perfectly to **1.00** for the traversal algorithms (Kahn, Kosaraju, DAG), proving strictly linear work. Kruskal's sorting phase introduces a slightly steeper slope due to the log factor, which becomes visible in the metrics but is strictly bounded by the $O(E \log E)$ constraint.

**Why Wall-Clock Time is Unreliable:**
Measurements in `time_ms` fluctuate unpredictably due to JVM warm-up, Just-In-Time (JIT) compilation, garbage collection pauses, and OS background load. Relying on exact internal operation counters (`edge_scans`, `dsu_ops`, `comparisons`) extracted directly into `results_2.csv` provides deterministic, reproducible, and completely hardware-independent evidence of algorithmic efficiency.