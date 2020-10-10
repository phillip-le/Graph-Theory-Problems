import java.util.*;

public class ErdosNumbers {
    /**
     * String representing Paul Erdos's name to check against
     */
    public static final String ERDOS = "Paul Erdös";

    private final double UNWEIGHTED = -1;
    private final double EPSILON = 1e-6;

    private static class Edge {
        private String from, to, paper;
        private double cost;

        private Edge(String from, String to, String paper, double cost) {
            this.from = from;
            this.to = to;
            this.paper = paper;
            this.cost = cost;
        }
    }

    private static class Node {
        private int key;
        private double value;

        private Node(int key, double value) {
            this.key = key;
            this.value = value;
        }
    }

    private class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node x, Node y) {
            double result = x.value - y.value;
            if (result < EPSILON) {
                return 0;
            } else if (result > 0) {
                return 1;
            }
            return -1;
        }
    }

    private static class Graph {
        private int numOfVertices;

        // Stores the unweighted Erdos Number of each author
        private double[] unweightedDists;

        // Maps each author to an index
        private Map<String, Integer> authorMapping;

        // Stores the vertices of the graph
        private List<List<Edge>> edges;

        private Graph() {
            numOfVertices = 0;
            authorMapping = new HashMap<>();
            edges = new ArrayList<>();
        }

        private void addAuthor(String author) {
            authorMapping.put(author, numOfVertices++);
            edges.add(new ArrayList<>());
        }

        private void addEdge(String from, String to, String paper,
                double cost) {
            getEdges(from).add(new Edge(from, to, paper,
                    cost));
        }

        private List<Edge> getEdges(String from) {
            return edges.get(authorMapping.get(from));
        }

        private boolean contains(String author) {
            return authorMapping.containsKey(author);
        }
    }

    // Stores all graph information as an adjacency list
    private Graph graph;

    /**
     * Initialises the class with a list of papers and authors.
     *
     * Each element in 'papers' corresponds to a String of the form:
     * 
     * [paper name]:[author1][|author2[|...]]]
     *
     * Note that for this constructor and the below methods, authors and papers
     * are unique (i.e. there can't be multiple authors or papers with the exact same name or title).
     * 
     * @param papers List of papers and their authors
     */
    public ErdosNumbers(List<String> papers) {
        graph = new Graph();
        insertPapers(papers);
        graph.unweightedDists = calculateShortestPaths(false);
    }

    private void insertPapers(List<String> rawPapers) {
        for (String rawPaper : rawPapers) {
            String[] paper = rawPaper.split(":");
            String paperName = paper[0];
            String[] authors = paper[1].split("\\|");
            for (String author1 : authors) {
                if (!graph.contains(author1)) {
                    graph.addAuthor(author1);
                }
                for (String author2 : authors) {
                    if (!author1.equals(author2)) {
                        graph.addEdge(author1, author2, paperName, UNWEIGHTED);
                    }
                }
            }
        }
    }

    /**
     * Gets all the unique papers the author has written (either solely or
     * as a co-author).
     * 
     * @param author to get the papers for.
     * @return the unique set of papers this author has written.
     */
    public Set<String> getPapers(String author) {
        // TODO: implement this
        Set<String> papers = new HashSet<>();
        for (Edge e : graph.getEdges(author)) {
            papers.add(e.paper);
        }
        return papers;
    }

    /**
     * Gets all the unique co-authors the author has written a paper with.
     *
     * @param author to get collaborators for
     * @return the unique co-authors the author has written with.
     */
    public Set<String> getCollaborators(String author) {
        Set<String> collaborators = new HashSet<>();
        for (Edge e : graph.getEdges(author)) {
            collaborators.add(e.to);
        }
        return collaborators;
    }

    /**
     * Checks if Erdos is connected to all other author's given as input to
     * the class constructor.
     * 
     * In other words, does every author in the dataset have an Erdos number?
     * 
     * @return the connectivity of Erdos to all other authors.
     */
    public boolean isErdosConnectedToAll() {
        // TODO: implement this
        for (double d : graph.unweightedDists) {
            if (Double.isInfinite(d)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculate the Erdos number of an author. 
     * 
     * This is defined as the length of the shortest path on a graph of paper 
     * collaborations (as explained in the assignment specification).
     * 
     * If the author isn't connected to Erdos (and in other words, doesn't have
     * a defined Erdos number), returns Integer.MAX_VALUE.
     * 
     * Note: Erdos himself has an Erdos number of 0.
     * 
     * @param author to calculate the Erdos number of
     * @return authors' Erdos number or otherwise Integer.MAX_VALUE
     */
    public int calculateErdosNumber(String author) {
        // TODO: implement this
        double dist = graph.unweightedDists[graph.authorMapping.get(author)];
        return Double.isInfinite(dist) ? Integer.MAX_VALUE : (int) dist;
    }

    /**
     * Gets the average Erdos number of all the authors on a paper.
     * If a paper has just a single author, this is just the author's Erdos number.
     *
     * Note: Erdos himself has an Erdos number of 0.
     *
     * @param paper to calculate it for
     * @return average Erdos number of paper's authors
     */
    public double averageErdosNumber(String paper) {
        // TODO: implement this
        
        return 0;
    }

    /**
     * Calculates the "weighted Erdos number" of an author.
     * 
     * If the author isn't connected to Erdos (and in other words, doesn't have
     * an Erdos number), returns Double.MAX_VALUE.
     *
     * Note: Erdos himself has a weighted Erdos number of 0.
     * 
     * @param author to calculate it for
     * @return author's weighted Erdos number
     */
    public double calculateWeightedErdosNumber(String author) {
        // TODO: implement this

        return 0;
    }

    private double[] calculateShortestPaths(boolean weighted) {
        double[] dists = new double[graph.numOfVertices];
        Arrays.fill(dists, Double.POSITIVE_INFINITY);
        // Distance between start and itself is 0
        dists[graph.authorMapping.get(ERDOS)] = 0;

        boolean[] visited = new boolean[graph.numOfVertices];
        Arrays.fill(visited, false);
        PriorityQueue<Node> pq = new PriorityQueue<>(graph.numOfVertices,
                new NodeComparator());
        pq.add(new Node(graph.authorMapping.get(ERDOS), 0));
        while (!pq.isEmpty()) {
            Node node = pq.poll();
            visited[node.key] = true;
            if (dists[node.key] < node.value) {
                continue;
            }
            List<Edge> outboundEdges = graph.edges.get(node.key);
            for (Edge e : outboundEdges) {
                int oppositeEdgeIdx = graph.authorMapping.get(e.to);
                if (visited[oppositeEdgeIdx]) {
                    continue;
                }

                // Relax step
                double weight = weighted ? e.cost : 1;
                if (dists[node.key] + weight < dists[oppositeEdgeIdx]) {
                    dists[oppositeEdgeIdx] = dists[node.key] + weight;
                    pq.add(new Node(oppositeEdgeIdx, dists[oppositeEdgeIdx]));
                }
            }
        }
        return dists;
    }
}
