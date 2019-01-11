package projects.graph;

import projects.graph.utils.Neighbor;
import projects.graph.utils.NeighborList;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;
/**
 * <p>{@link AdjacencyListGraph} is a {@link Graph} implemented as an adjacency list, i.e a one-dimensional array of linked lists,
 * where A(i) is a linked list containing the neighbors of node i and the corresponding edges' weights. <b>The neighbors of a given node are defined as the nodes it  points to</b> (if any). </p>
 *
 * <p>Other implementations besides linked lists are possible (e.g BSTs over the weight of the edge), yet for this project we
 * will keep it simple and stick to that basic implementation. One of its advantages is that, because the lists do not need
 * to be sorted in any way, the insertion of a new edge is a O(1) operation (find the list corresponding to the source node in O(1)
 * and add the new list node up front.</p>
 *
 * @author --- YOUR NAME HERE! ---
 *
 * @see Graph
 * @see AdjacencyMatrixGraph
 * @see SparseAdjacencyMatrixGraph
 * @see NeighborList
 */
public class AdjacencyListGraph extends Graph {

    /* *********************************************************** */
    /* THE FOLLOWING DATA FIELD MAKES UP THE INNER REPRESENTATION */
    /* OF YOUR GRAPH. YOU SHOULD NOT CHANGE THIS DATA FIELD!      */
    /* *********************************************************  */

    private NeighborList[] list;

    /* ***************************************************** */
    /* PLACE ANY EXTRA PRIVATE DATA MEMBERS OR METHODS HERE: */
    /* ***************************************************** */

    private int numNodes, numEdges;

    /* ************************************************** */
    /* IMPLEMENT THE FOLLOWING PUBLIC METHODS. MAKE SURE  */
    /* YOU ERASE THE LINES THAT THROW EXCEPTIONS.         */
    /* ************************************************** */

    /**
     * A default (no-arg) constructor for {@link AdjacencyListGraph} <b>should</b> exist,
     * even if you don't do anything with it.
     */
    public AdjacencyListGraph(){
        // You might not want this constructor to do anything, depending on your design.
        // At any rate, DO NOT ERASE IT!
        numNodes = numEdges = 0;
        list = new NeighborList[numNodes];
    }

    @Override
    public void addNode() {
        NeighborList[] old = list;
        list = new NeighborList[list.length + 1];
        for(int i = 0; i < list.length - 1; i++)
            list[i] = old[i];
        list[list.length - 1] = new NeighborList();
        numNodes++;
    }

    @Override
    public void addEdge(int source, int dest, int weight) {
        assert ( source >= 0 && source < numNodes ) && ( dest >= 0 && dest < numNodes ) : "addEdge(): Invalid node parameters given: " +
                "source=" + source + ", dest=" + dest  +" and numNodes=" + numNodes + ".";
        if(weight < 0 || weight > INFINITY)
            throw new RuntimeException("addEdge(): Weight given out of bounds (weight=" + weight + ").");

        list[source].addFront(dest, weight);
        numEdges++;
    }


    @Override
    public void deleteEdge(int source, int dest) {
        assert ( source >= 0 && source < numNodes ) && ( dest >= 0 && dest < numNodes ) : "addEdge(): Invalid node parameters given: " +
                "source=" + source + ", dest=" + dest  +" and numNodes=" + numNodes + ".";
        list[source].remove(dest);
        numEdges--;
    }

    @Override
    public boolean edgeBetween(int source, int dest) {
        assert ( source >= 0 && source < numNodes ) && ( dest >= 0 && dest < numNodes ) : "addEdge(): Invalid node parameters given: " +
                "source=" + source + ", dest=" + dest  +" and numNodes=" + numNodes + ".";
        return list[source].containsNeighbor(dest);
    }

    @Override
    public int getEdgeWeight(int source, int dest) {
        assert ( source >= 0 && source < numNodes ) && ( dest >= 0 && dest < numNodes ) : "addEdge(): Invalid node parameters given: " +
                "source=" + source + ", dest=" + dest  +" and numNodes=" + numNodes + ".";
        return list[source].getWeight(dest);
    }

    @Override
    public Set<Integer> getNeighbors(int node) {
        assert node >= 0 && node < numNodes : "getNeighbors(): Invalid node parameter given: " + node + ".";
        HashSet<Integer> neighbors = new HashSet<>();
        list[node].forEach(n->neighbors.add(n.getNode()));
        return neighbors;
    }

    @Override
    public int getNumNodes() {
        return numNodes;
    }

    @Override
    public int getNumEdges() {
        return numEdges;
    }

    /* Methods specific to this class follow. */

    /**
     * Transforms this into an instance of {@link AdjacencyMatrixGraph}. This is an O(E) operation.
     *
     * You are <b>not</b> allowed to implement this method by using other transformation methods. For example, you
     *   <b>cannot</b> implement it with the line of code toSparseAdjacencyMatrixGraph().toAdjacencyMatrixGraph().
     *
     * @return An instance of {@link AdjacencyMatrixGraph}.
     */
    public AdjacencyMatrixGraph toAdjacencyMatrixGraph(){
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph();
        addAllToGraph(graph);
        return graph;
    }

    /**
     * Transforms this into an instance of {@link AdjacencyMatrixGraph}. This is an O(E) operation.
     *
     * You are <b>not</b> allowed to implement this method by using other transformation methods. For example, you
     * <b>cannot</b> implement it with the line of code toAdjacencyMatrixGraph().toSparseAdjacencyMatrixGraph().
     *
     * @return An instance of {@link AdjacencyMatrixGraph}.
     */
    public SparseAdjacencyMatrixGraph toSparseAdjacencyMatrixGraph(){
        SparseAdjacencyMatrixGraph graph = new SparseAdjacencyMatrixGraph();
        addAllToGraph(graph);
        return graph;
    }

    private void addAllToGraph(Graph graph){
        IntStream.range(0, numNodes).forEach(ignored->graph.addNode());
        for(int i = 0; i < numNodes; i++)
            for(Neighbor n: list[i])
                graph.addEdge(i, n.getNode(), n.getWeight());
    }
}