# DAA Assignment 3: Graph Algorithms Analysis

**Author:** Alikhan Zhambayev  
**Group:** SE-2517

## Project Overview
This project focuses on the implementation and empirical analysis of fundamental graph algorithms using custom adjacency list data structures without third-party graph libraries.

Implemented Algorithms:
1. **Disjoint Set Union (DSU):** Path compression and Union by Rank/Size.
2. **Minimum Spanning Tree (MST):** Kruskal's and Prim's algorithms.
3. **Strongly Connected Components (SCC):** Kosaraju's algorithm (using iterative DFS to prevent StackOverflow) and Kahn's algorithm for Topological Sort.
4. **Shortest Path:** Single-source shortest path algorithm on Directed Acyclic Graphs (DAG).

---

## Project Structure
```text
DAA_Assignment3_Alikhan_Zhambayev_SE-2517/
├── img/                       # Generated log-scale plots for report
├── results/                   # Contains results.csv benchmark output
├── src/
│   ├── main/java/
│   │   ├── algo/              # DSU, MSTAlgorithms, SCCAlgorithms, DAGShortestPath
│   │   ├── bench/             # GraphBenchmark and Benchmark entry point
│   │   ├── graph/             # Edge, Graph interface, AdjacencyListGraph
│   │   ├── metrics/           # Metrics, Result, CsvWriter
│   │   └── Main.java          # Main execution class
│   └── test/java/
│       ├── algo/              # Unit tests for algorithm correctness
│       └── graph/             # Unit tests for graph core and edge cases
├── generate_charts.py         # Python script to plot empirical metrics
├── pom.xml                    # Maven configuration (JUnit 5 dependency)
├── results.csv                # Metrics export file
└── README.md