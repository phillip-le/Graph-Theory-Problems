## Graph Theory Problems
### ContactTracer
A basic implementation of virus contact tracing. Given a list of tuples where each tuple is of the form (personA, personB, timeOfContact), ContactTracer determines who has contracted the virus if:
* A person comes into contact with the virus at time t and they do not already have it, they contract the virus and become contagious at exactly t + 60 minutes
* A person is contagious with the virus at time t, then they instantly spread it to anybody they come into contact with at or after time t

### ErdosNumbers
The Erdos Number describes the "collaborative distance" between the mathematician Paul Erdos and another person on research papers. The calculation of Erdos numbers can be done through graphs, where each node represents a distinct person, and edges between people indicate a paper co-authorship. The length of the shortest path between a person and Paul Erdos is that person's Erdos number. Another variant of the Erdos Number called the "weighted Erdos number" considers weights on each edge in the graph between two authors a and b as $\frac{1}{c(a, b)}$, where c(a,b) is the number of papers the two authors have published together. 

The Erdos Number problem was modelled as an unweighted, undirected graph that was solved using Dijkstra's algorithm. The weighted variant was modelled as an weighted, undirected graph that was also solved using Dijkstra's algorithm. Dijstra's algorithm was chosen because the distance of all nodes from a single source must be calculated. This implementation preprocessed the relationships between authors so that Erdos number queries can be completed O(1) time. 

### FactChecker
Suppose there was a party where attendees came and went at different times. A Fact describes either "person A left before person B" or "person A was at the party at the same time as person B". The FactChecker class takes in a list of Facts and determines if the facts are logically possible.

This problem was modelled as an unweighted, directed graph where the vertices represented people at the party and edges represented that person A was at the party before or at the same time as person B. If a fact stated that person A was at the party at the same time as person B, there would be directed edges from A to B and B to A. This graph problem was solved using a modified DFS algorithm which checked for cycles. 