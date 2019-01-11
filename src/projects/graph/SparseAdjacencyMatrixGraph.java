package projects.graph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * <p>{@link SparseAdjacencyMatrixGraph} is a {@link Graph} implemented as a <b>sparse adjacency matrix</b>, i.e a
 * linked list of the matrix's non-null (or, in this case, non-zero) elements.</p>
 *
 * <p>Inserting a new node has no influence in the inner storage of this class, because the only storage it provides
 * is for <b>edges</b>. This does not mean that you should neglect to store information about a new node having
 * been inserted: it just means that a new node needs to be connected to an existing one if it is to appear as a new
 * node <b>in the list</b>. Think about the (dense) adjacency matrix representation: there, if we add a new node,
 * we have to pay O(V^2) to re-allocate the space of the old array to the new array and add a new row and column
 * of zeroes. There is no <b>novel</b> assignment in the matrix! In the case of the sparsified representation, the inner
 * representation does not have to change at all! But you will <b>still</b> need to make note of the fact that a new node was
 * inserted. After all, we want {@link #getNumNodes()} to work correctly, don't we? </p>
 *
 * <p>Adding a new edge is also O(1), because there's no need to keep the list ordered; we can just add the element in the
 * front without any further hassle. If you are having trouble understanding why the list can be unordered, check the docs
 * of {@link #toAdjacencyMatrixGraph()}.</p>
 *
 *
 *<p>{@link #edgeBetween(int, int)}, {@link #deleteEdge(int, int)}, {@link #getEdgeWeight(int, int)} and
 * {@link #getNeighbors(int)} all run in O(E) time.</p>
 *
 * <p>The space occupied by an instance of {@link SparseAdjacencyMatrixGraph} is O(E). </p>
 *
 * @author --- YOUR NAME HERE! ---
 *
 * @see Graph
 * @see AdjacencyMatrixGraph
 * @see AdjacencyListGraph
 */
public class SparseAdjacencyMatrixGraph extends Graph {

    /* ******************************************************************************* */
    /* THE FOLLOWING CLASS DECLARATION AND DATA FIELD MAKE UP THE INNER REPRESENTATION */
    /* OF YOUR GRAPH. YOU SHOULD NOT CHANGE THIS DATA FIELD!                           */
    /* ******************************************************************************  */

    private class EdgeData {
        int source;
        int dest;
        int weight;

        /* A three-arg constructor for easy initialization of fields */
        EdgeData(int source, int dest, int weight){
            this.source = source;
            this.dest = dest;
            this.weight = weight;
        }

        @Override
        public boolean equals(Object o){
            if(o == null || o.getClass() != this.getClass())
                return  false;
            EdgeData oCasted = null;
            try {
                oCasted = (EdgeData)o;
            } catch(ClassCastException ignored){
                return false;
            }
            return  (oCasted.source == source) &&
                    (oCasted.dest == dest) &&
                    (oCasted.weight == weight);
        }
    }

    /* ***************************************************** */
    /* PLACE ANY EXTRA PRIVATE DATA MEMBERS OR METHODS HERE: */
    /* ***************************************************** */

    private List<EdgeData> list; // This is a standard and safe usage of Java's generics.
    private int nodeCount;

    /* ************************************************** */
    /* IMPLEMENT THE FOLLOWING PUBLIC METHODS. MAKE SURE  */
    /* YOU ERASE THE LINES THAT THROW EXCEPTIONS.         */
    /* ************************************************** */

    /**
     * A default (no-arg) constructor for {@link SparseAdjacencyMatrixGraph} <b>should</b> exist,
     * even if you don't do anything with it.
     */
    public SparseAdjacencyMatrixGraph(){
        // You might not want this constructor to do anything, depending on your design.
        // At any rate, DO NOT ERASE IT!
        list = new LinkedList<>();
        nodeCount = 0;
    }

    @Override
    public void addNode() {
        nodeCount++;
    }

    @Override
    public void addEdge(int source, int dest, int weight) {
        list.add(0, new EdgeData(source, dest, weight)); // addFront() for efficiency.
    }

    @Override
    public void deleteEdge(int source, int dest) {
        list.removeIf((EdgeData edge) -> (edge.source == source && edge.dest == dest));
    }

    @Override
    public boolean edgeBetween(int source, int dest) {
        for(EdgeData edge: list)
            if(edge.source == source && edge.dest == dest)
                return  true;
        return false;
    }

    @Override
    public int getEdgeWeight(int source, int dest) {
        for(EdgeData edge : list)
            if(edge.source == source && edge.dest == dest)
                return edge.weight;
        return 0;
    }

    @Override
    public Set<Integer> getNeighbors(int node) {
        Set<Integer> neighbors = new HashSet<>();
        for(EdgeData edge : list)
            if(edge.source == node)
                neighbors.add(edge.source);
        return neighbors;
    }

    @Override
    public int getNumNodes() {
        return nodeCount;
    }

    @Override
    public int getNumEdges() {
        return list.size();
    }

    /* Methods specific to this class follow. */

    /**
     * Creates the dense representation of the matrix, corresponding to an instance of {@link AdjacencyMatrixGraph}. The adjacency
     * matrix will occupy O(V^2) space. Since every node in the list that makes up the sparsified representation containsKVPair a triple (i, j, weight), simply
     * looping through the list in O(E) and assigning the corresponding cell of the matrix to the appropriate weight is sufficient. For this
     * reason, it is not important whether the list is sorted in any way, <b>except for the special case of having been immediately created
     * by {@link AdjacencyMatrixGraph#toSparseAdjacencyMatrixGraph()}, where we want you to create the adjacency list following row-major
     * order.</b> However, because of this simple iterative algorithm we just explained, addEdge() in this class is an O(1) operation: we can just insert
     * a new node as the "head" of the list, and we are done.
     *
     * You are <b>not</b> allowed to implement this method by using other transformation methods. For example, you
     *  <b>cannot</b> implement it with the line of code toAdjacencyListGraph().toAdjacencyMatrixGraph().
     *
     * @return An {@link AdjacencyMatrixGraph} instance.
     */
    public AdjacencyMatrixGraph toAdjacencyMatrixGraph(){
        AdjacencyMatrixGraph adjMat = new AdjacencyMatrixGraph();
        IntStream.range(0, nodeCount).forEach(n->adjMat.addNode());
        list.forEach(n->adjMat.addEdge(n.source, n.dest, n.weight));
        return adjMat;
    }

    /**
     * Transforms this to an {@link AdjacencyListGraph}instance.  Remember that an {@link AdjacencyListGraph}
     * is implemented as an array of linked lists, where A(i) is a linked list containing the neighbors of node i.
     *
     * You are <b>not</b> allowed to implement this method by using other transformation methods. For example, you
     *     <b>cannot</b> implement it with the line of code toAdjacencyMatixGraph().toAdjacencyListGraph().
     *
     * @return An {@link AdjacencyListGraph} instance.
     */
    public AdjacencyListGraph toAdjacencyListGraph(){
        AdjacencyListGraph adjListGraph = new AdjacencyListGraph();
        IntStream.range(0, nodeCount).forEach(n->adjListGraph.addNode());
        list.forEach(n->adjListGraph.addEdge(n.source, n.dest, n.weight));
        return adjListGraph
    }
}