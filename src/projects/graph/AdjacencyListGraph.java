package projects.graph;

import java.util.Set;
import java.util.List;
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
 */
public class AdjacencyListGraph extends Graph {

    /* ******************************************************************************* */
    /* THE FOLLOWING CLASS DECLARATION AND DATA FIELD MAKE UP THE INNER REPRESENTATION */
    /* OF YOUR GRAPH. YOU SHOULD NOT CHANGE THIS DATA FIELD!                           */
    /* ******************************************************************************  */

    class NeighborData{
        int neighborNode;
        int weight;
        NeighborData(int neighborNode, int weight){
            this.neighborNode = neighborNode;
            this.weight = weight;
        }
    }
    private List<NeighborData>[] list;

    /* ***************************************************** */
    /* PLACE ANY EXTRA PRIVATE DATA MEMBERS OR METHODS HERE: */
    /* ***************************************************** */


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
    }

    @Override
    public void addNode() {
        throw UNIMPL_METHOD;
    }

    @Override
    public void addEdge(int source, int dest, int weight) {
        throw UNIMPL_METHOD;
    }


    @Override
    public void deleteEdge(int source, int dest) {
        throw UNIMPL_METHOD;
    }

    @Override
    public boolean edgeBetween(int source, int dest) {
        throw UNIMPL_METHOD;
    }

    @Override
    public int getEdgeWeight(int source, int dest) {
        throw UNIMPL_METHOD;
    }

    @Override
    public Set<Integer> getNeighbors(int node) {
        throw UNIMPL_METHOD;
    }

    @Override
    public int getNumNodes() {
        throw UNIMPL_METHOD;
    }

    @Override
    public int getNumEdges() {
        throw UNIMPL_METHOD;
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
        throw UNIMPL_METHOD;
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
        throw UNIMPL_METHOD;
    }
}