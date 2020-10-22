import java.util.*;

public class ContactTracer {

    /**
     * Representation of relationship between people as Edge
     */
    private static class Edge {
        // The other person in contact
        private String to;

        // The time when they had contact
        private int time;

        /**
         * Edge Constructor
         * @param to - the other person they had in contact
         * @param time - the time when they had contact
         */
        private Edge(String to, int time) {
            this.to = to;
            this.time = time;
        }
    }

    /**
     * Representation of Contacts as Graph
     */
    private static class Graph {
        // Stores list of people that a person has been in contact with
        private List<PriorityQueue<Edge>> edges;

        // Stores the mapping from person to index
        private Map<String, Integer> personMapping;

        // Number of people
        private int n;

        /**
         * Graph constructor.
         */
        private Graph() {
            edges = new ArrayList<>();
            personMapping = new HashMap<>();
            n = 0;
        }

        /**
         * Adds a person to the graph
         * @param personA - the person to add
         */
        private void addVertex(String personA) {
            edges.add(new PriorityQueue<>(new Comparator<Edge>() {
                @Override
                public int compare(Edge e1, Edge e2) {
                    return e1.time - e2.time;
                }
            }));
            personMapping.put(personA, n++);
        }

        /**
         * Adds a contact edge between two people at a certain time
         * @param from first person
         * @param to second person
         * @param time that they met
         */
        private void addEdge(String from, String to, int time) {
            getEdges(from).add(new Edge(to, time));
        }

        /**
         * Gets the people that the person has been in contact with
         * @param from the person to get contacts
         * @return the people that the person has been in contact with
         */
        private PriorityQueue<Edge> getEdges(String from) {
            return edges.get(personMapping.get(from));
        }

        /**
         * Gets the index of the person
         * @param person the person to get the index of
         * @return the index of the person
         */
        private int getPersonIdx(String person) {
            return personMapping.get(person);
        }
    }

    // Stores the graph
    private Graph graph;

    /**
     * Initialises an empty ContactTracer with no populated contact traces.
     */
    public ContactTracer() {
        graph = new Graph();
    }

    /**
     * Initialises the ContactTracer and populates the internal data structures
     * with the given list of contract traces.
     * 
     * @param traces to populate with
     * @require traces != null
     */
    public ContactTracer(List<Trace> traces) {
        // TODO: implement this!
        for (Trace trace : traces) {
            addTrace(trace);
        }
    }

    /**
     * Adds a new contact trace to 
     * 
     * If a contact trace involving the same two people at the exact same time is
     * already stored, do nothing.
     * 
     * @param trace to add
     * @require trace != null
     */
    public void addTrace(Trace trace) {
        // TODO: implement this!
        if (!graph.personMapping.containsKey(trace.getPerson1())) {
            graph.addVertex(trace.getPerson1());
        }
        if (!graph.personMapping.containsKey(trace.getPerson2())) {
            graph.addVertex(trace.getPerson2());
        }
        graph.addEdge(trace.getPerson1(), trace.getPerson2(), trace.getTime());
        graph.addEdge(trace.getPerson2(), trace.getPerson1(), trace.getTime());
    }

    /**
     * Gets a list of times that person1 and person2 have come into direct 
     * contact (as per the tracing data).
     *
     * If the two people haven't come into contact before, an empty list is returned.
     * 
     * Otherwise the list should be sorted in ascending order.
     * 
     * @param person1 
     * @param person2
     * @return a list of contact times, in ascending order.
     * @require person1 != null && person2 != null
     */
    public List<Integer> getContactTimes(String person1, String person2) {
        // TODO: implement this!
        List<Integer> contactTimes = new ArrayList<>();
        for (Edge e : graph.getEdges(person1)) {
            if (e.to.equals(person2)) {
                contactTimes.add(e.time);
            }
        }
        return contactTimes;
    }

    /**
     * Gets all the people that the given person has been in direct contact with
     * over the entire history of the tracing dataset.
     * 
     * @param person to list direct contacts of
     * @return set of the person's direct contacts
     */
    public Set<String> getContacts(String person) {
        // TODO: implement this!
        Set<String> contacts = new HashSet<>();
        for (Edge e : graph.getEdges(person)) {
            contacts.add(e.to);
        }
        return contacts;
    }

    /**
     * Gets all the people that the given person has been in direct contact with
     * at OR after the given timestamp (i.e. inclusive).
     * 
     * @param person to list direct contacts of
     * @param timestamp to filter contacts being at or after
     * @return set of the person's direct contacts at or after the timestamp
     */
    public Set<String> getContactsAfter(String person, int timestamp) {
        // TODO: implement this!
        Set<String> contacts = new HashSet<>();
        for (Edge e : graph.getEdges(person)) {
            if (e.time >= timestamp) {
                contacts.add(e.to);
            }
        }
        return contacts;
    }

    /**
     * Initiates a contact trace starting with the given person, who
     * became contagious at timeOfContagion.
     * 
     * Note that the return set shouldn't include the original person the trace started from.
     * 
     * @param person to start contact tracing from
     * @param timeOfContagion the exact time person became contagious
     * @return set of people who may have contracted the disease, originating from person
     */
    public Set<String> contactTrace(String person, int timeOfContagion) {
        // TODO: implement this!
        boolean[] visited = new boolean[graph.n];
        Set<String> infected = new HashSet<>();
        bfs(person, visited, infected, timeOfContagion);
        infected.remove(person);
        return infected;
    }

    /**
     * Conducts a breadth first search that adds people
     * @param current the current person to search
     * @param visited tracks who has been searched so far
     * @param infected the people who have been infected
     * @param time the time that the original person was contagious
     */
    private void bfs(String current, boolean[] visited, Set<String> infected,
            int time) {
        if (visited[graph.getPersonIdx(current)]) {
            return;
        }
        visited[graph.getPersonIdx(current)] = true;
        infected.add(current);
        for (Edge e : graph.getEdges(current)) {
            if (e.time >= time) {
                bfs(e.to, visited, infected, e.time + 60);
            }
        }
    }
}
