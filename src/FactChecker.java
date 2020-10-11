import java.util.*;

public class FactChecker {

    private static class Edge {
        private String from, to;

        private Edge(String from, String to) {
            this.from = from;
            this.to = to;
        }
    }

    private static class Graph {
        private List<List<Edge>> edges;
        private Map<String, Integer> personMapping;
        // Number of vertices
        private int n;

        private Graph() {
            edges = new ArrayList<>();
            personMapping = new HashMap<>();
            n = 0;
        }

        private void addVertex(String personA) {
            edges.add(new ArrayList<>());
            personMapping.put(personA, n++);
        }

        private void addEdge(String personA, String personB) {
            getEdges(personA).add(new Edge(personA, personB));
        }

        private List<Edge> getEdges(String from) {
            return edges.get(personMapping.get(from));
        }

        private int getPersonIdx(String person) {
            return personMapping.get(person);
        }
    }

    /**
     * Checks if a list of facts is internally consistent. 
     * That is, can they all hold true at the same time?
     * Or are two (or potentially more) facts logically incompatible?
     * 
     * @param facts list of facts to check consistency of
     * @return true if all the facts are internally consistent, otherwise false.
     */
    public static boolean areFactsConsistent(List<Fact> facts) {
        // TODO: implement this!
        Graph graph = new Graph();
        for (Fact f : facts) {
            insertFact(graph, f);
        }
        return !cycleDFS(graph);
    }

    private static void insertFact(Graph graph, Fact fact) {
        if (!graph.personMapping.containsKey(fact.getPersonA())) {
            graph.addVertex(fact.getPersonA());
        }
        if (!graph.personMapping.containsKey(fact.getPersonB())) {
            graph.addVertex(fact.getPersonB());
        }
        graph.addEdge(fact.getPersonA(), fact.getPersonB());
        if (fact.getType() == Fact.FactType.TYPE_TWO) {
            graph.addEdge(fact.getPersonB(), fact.getPersonA());
        }
    }

    private static boolean cycleDFS(Graph graph) {
        boolean[] visited = new boolean[graph.n];
        Stack<Integer> path = new Stack<>();
        for (int i = 0; i < graph.n; i++) {
            if (!visited[i]) {
                if (dfs(graph, i, visited, path)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean dfs(Graph graph, int current, boolean[] visited, Stack<Integer> path) {
        if (path.contains(current)) {
            // Check if it is a cycle between two vertices
            return path.get(path.size() - 2) != current;
        }
        if (visited[current]) {
            return false;
        }
        path.push(current);
        visited[current] = true;
        for (Edge e : graph.edges.get(current)) {
            int next = graph.getPersonIdx(e.to);
            if (dfs(graph, next, visited, path)) {
                return true;
            }
        }
        path.pop();
        return false;
    }
}
