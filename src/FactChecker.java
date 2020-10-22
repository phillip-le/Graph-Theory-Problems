import java.util.*;

public class FactChecker {

    /**
     * Represents timing between two people as edge
     */
    private static class Edge {
        private String to;
        private Fact.FactType factType;
        private Edge(String to, Fact.FactType factType) {
            this.to = to;
            this.factType = factType;
        }
    }

    /**
     * Represents the timing of people at the party as graph
     */
    private static class Graph {
        // Stores the timing between people
        private List<List<Edge>> edges;

        // Maps person to index
        private Map<String, Integer> personMapping;

        // Number of people
        private int n;

        /**
         * Graph Constructor
         */
        private Graph() {
            edges = new ArrayList<>();
            personMapping = new HashMap<>();
            n = 0;
        }

        /**
         * Adds person to graph.
         * @param personA - the person to add
         */
        private void addVertex(String personA) {
            edges.add(new ArrayList<>());
            personMapping.put(personA, n++);
        }

        /**
         * Adds timing to graph.
         * @param personA - first person
         * @param personB - second person
         * @param factType - the type of fact
         */
        private void addEdge(String personA, String personB,
                Fact.FactType factType) {
            getEdges(personA).add(new Edge(personB, factType));
        }

        /**
         * Gets the people who were at the party at the same time as from or
         * left after from.
         * @param from - the person to get the edges of.
         * @return the people who were at the party at the same time as from or
         * left after from.
         */
        private List<Edge> getEdges(String from) {
            return edges.get(personMapping.get(from));
        }

        /**
         * Gets the index of person.
         * @param person the person to get the index of.
         * @return the index of person.
         */
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

    /**
     * Inserts fact into graph.
     * @param graph - the graph to insert fact into.
     * @param fact - the fact to insert.
     */
    private static void insertFact(Graph graph, Fact fact) {
        if (!graph.personMapping.containsKey(fact.getPersonA())) {
            graph.addVertex(fact.getPersonA());
        }
        if (!graph.personMapping.containsKey(fact.getPersonB())) {
            graph.addVertex(fact.getPersonB());
        }

        // Type two facts are treated are inserted as unweighted edges
        graph.addEdge(fact.getPersonA(), fact.getPersonB(), fact.getType());
        if (fact.getType() == Fact.FactType.TYPE_TWO) {
            graph.addEdge(fact.getPersonB(), fact.getPersonA(), fact.getType());
        }
    }

    /**
     * Determine if there is an invalid cycle in the graph.
     * @param graph the graph to search.
     * @return true if there is an invalid cycle, else false.
     */
    private static boolean cycleDFS(Graph graph) {
        boolean[] visited = new boolean[graph.n];
        boolean[] exploring = new boolean[graph.n];
        Stack<Edge> path = new Stack<>();
        for (int i = 0; i < graph.n; i++) {
            if (!visited[i]) {
                if (dfs(graph, i, visited, path, exploring)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Conducts depth first search on graph to determine if there is an invalid
     * cycle.
     * @param graph - the graph to search.
     * @param current - the index of the current person
     * @param visited - the people who have been seen so far
     * @param pathEdges - the edges of the current path
     * @param exploring - tracks who has been searched in the current path
     * @return true if there is an invalid cycle, else false.
     */
    private static boolean dfs(Graph graph, int current, boolean[] visited,
            Stack<Edge> pathEdges, boolean[] exploring) {
        if (exploring[current]) {
            // Check if it is a cycle between two vertices
            Edge secondLastEdge = pathEdges.get(pathEdges.size() - 2);
            return !typeTwoEdge(pathEdges.peek(), secondLastEdge);
        }
        if (visited[current]) {
            return false;
        }
        visited[current] = true;
        exploring[current] = true;
        for (Edge e : graph.edges.get(current)) {
            int next = graph.getPersonIdx(e.to);
            pathEdges.push(e);
            if (dfs(graph, next, visited, pathEdges, exploring)) {
                return true;
            }
            pathEdges.pop();
        }
        exploring[current] = false;
        return false;
    }

    /**
     * Checks if the two edges are FactType Two.
     * @param e1 first edge
     * @param e2 second edge
     * @return true if the two edges are FactType Two, else false.
     */
    private static boolean typeTwoEdge(Edge e1, Edge e2) {
        return e1.factType == Fact.FactType.TYPE_TWO &&
                e2.factType == Fact.FactType.TYPE_TWO;
    }
}
