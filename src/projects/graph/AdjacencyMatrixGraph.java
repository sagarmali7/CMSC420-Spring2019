package projects.graph;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * <p>{@link AdjacencyMatrixGraph} is a {@link Graph} implemented as an <b>adjacency matrix</b>. An adjacency matrix
 * is a V x V matrix where M(i, j) is the weight of the edge from i to j. If there is no edge between i and j,
 * the weight should be zero. </p>
 *
 * <p>Adjacency matrices answer {@link #edgeBetween(int, int)} in O(1) time. Insertion and deletion of edges, as well
 * as retrieval of the weight of a given edge  are all O(1) operations as well. Retrieval of all neighbors of a given node
 * happens in O(V) time. </p>
 *
 * <p>The main drawbacks of adjacency matrices are: </p>
 *  <ol>
 *      <li>They occupy O(V^2) <b>contiguous</b> memory space, which for sparse graphs can be a significant memory footprint. </li>
 *      <li>addNode() runs in O(V^2) time, since new array storage needs to be allocated for the extra row and column,
 *      and the old data need be copied to the new array. </li>
 *  </ol>
 *
 * @author --- YOUR NAME HERE! ---
 * @see Graph
 * @see SparseAdjacencyMatrixGraph
 * @see AdjacencyListGraph
 */
public class AdjacencyMatrixGraph extends Graph {

    /* ****************************************************** */
    /* THE FOLLOWING DATA FIELD IS THE INNER REPRESENTATION */
    /* OF YOUR GRAPH. YOU SHOULD NOT CHANGE THIS DATA FIELD! */
    /* ******************************************************  */

    private int[][] matrix;

    /* ***************************************************** */
    /* PLACE ANY EXTRA PRIVATE DATA MEMBERS OR METHODS HERE: */
    /* ***************************************************** */
    private int numNodes, numEdges;

    /* ************************************************** */
    /* IMPLEMENT THE FOLLOWING PUBLIC METHODS. MAKE SURE  */
    /* YOU ERASE THE LINES THAT THROW EXCEPTIONS.         */
    /* ************************************************** */

    /**
     * A default (no-arg) constructor for {@link AdjacencyMatrixGraph} <b>should</b> exist,
     * even if you don't do anything with it.
     */
    public AdjacencyMatrixGraph(){
        // You might not want this constructor to do anything, depending on your design.
        // At any rate, DO NOT ERASE IT!
        numNodes = numEdges = 0;
        matrix = new int[numNodes][numNodes];
    }

    @Override
    public void addNode() {
        int[][] oldMatrix = matrix;
        numNodes++;
        matrix = new int[numNodes][numNodes];
        for(int i = 0; i < numNodes - 1; i++) {
            matrix[i] = copyAndExpand(numNodes, oldMatrix[i]);
        }
        matrix[numNodes - 1] = new int[numNodes];
    }

    private int[] copyAndExpand(int length, int[] array){
        assert array.length < length : "copyAndExpand(): provided array should be smaller than numNodes.";
        int i;
        int[] retVal = new int[length];
        for(i = 0; i < array.length; i++) {
            retVal[i] = array[i]; // The rest will be zeroes.
        }
        return retVal;
    }

    @Override
    public void addEdge(int source, int dest, int weight) {
        // I throw an AssertionError if either node isn't within parameters. Behavior open to implementation according to docs.
        assert ( source >= 0 && source < numNodes ) && ( dest >= 0 && dest < numNodes ) : "addEdge(): Invalid node parameters given: " +
            "source=" + source + ", dest=" + dest  +" and numNodes=" + numNodes + ".";
        if(weight < 0 || weight > INFINITY) {
            throw new RuntimeException("addEdge(): Weight given out of bounds (weight=" + weight + ").");
        }
        if(matrix[source][dest]== 0) {// We don't want to update the num of edges
            numEdges++;             // if addEdge() was called for a weight update.
        }
        matrix[source][dest] = weight;
    }


    @Override
    public void deleteEdge(int source, int dest) {
        assert ( source >= 0 && source < numNodes ) && ( dest >= 0 && dest < numNodes ) : "deleteEdge(): Invalid node parameters given: " +
                "source=" + source + ", dest=" + dest  +" and numNodes=" + numNodes + ".";
        matrix[source][dest] = 0;
        numEdges--;
    }

    @Override
    public boolean edgeBetween(int source, int dest) {
        assert ( source >= 0 && source < numNodes ) && ( dest >= 0 && dest < numNodes ) : "edgeBetween(): Invalid node parameters given: " +
                "source=" + source + ", dest=" + dest  +" and numNodes=" + numNodes + ".";
        return matrix[source][dest] != 0;
    }

    @Override
    public int getEdgeWeight(int source, int dest) {
        if( source < 0 || source >= numNodes  ||  dest < 0 || dest >= numNodes) {
            return 0;
        }
        return matrix[source][dest];
    }

    @Override
    public Set<Integer> getNeighbors(int node) {
        assert node >= 0 && node < numNodes : "getNeighbors(): Invalid node parameter given: " + node + ".";
        Set<Integer> neighbors = new HashSet<>();
        for(int i = 0; i < numNodes; i++) {
            if (matrix[node][i] > 0) {
                neighbors.add(i);
            }
        }
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

    @Override
    public void clear() {
        numNodes = numEdges = 0;
        matrix = new int[numNodes][numNodes];
    }


    /* Methods specific to this class follow. */

    /**
     * Returns a sparsified representation of the adjacency matrix, i.e a linked list of its non-null elements (non-zero,
     * in this case) and their coordinates. The matrix should be scanned in <b>row-major order</b> to populate
     * the elements of the list (<b>and we test for proper element insertion order!</b>).
     *
     * You are <b>not</b> allowed to implement this method by using other transformation methods. For example, you
     * <b>cannot</b> implement it with the line of code toAdjacencyListGraph().toSparseAdjacencyMatrixGraph().
     *
     * @return A {@link SparseAdjacencyMatrixGraph} instance.
     */
    public SparseAdjacencyMatrixGraph toSparseAdjacencyMatrixGraph(){
        SparseAdjacencyMatrixGraph spAdjMatGraph = new SparseAdjacencyMatrixGraph();
        IntStream.range(0, numNodes).forEach(ignored->spAdjMatGraph.addNode());

        addAllToGraph(spAdjMatGraph);

        return spAdjMatGraph;
    }

    /**
     * Returns a representation of the {@link Graph} as an {@link AdjacencyListGraph}. Remember that an {@link AdjacencyListGraph}
     * is implemented as an array of linked lists, where A(i) is a linked list containing the neighbors of node i.  Remember that an
     * {@link AdjacencyListGraph} is implemented as an array of linked lists, where A(i) is a linked list containing the neighbors of node i.
     *
     * You are <b>not</b> allowed to implement this method by using other transformation methods. For example, you
     *    <b>cannot</b> implement it with the line of code toSparseAdjacencyMatrixGraph().toAdjacencyListGraph().
     *
     * @return  An {@link AdjacencyListGraph} instance.
     */
    public AdjacencyListGraph toAdjacencyListGraph(){
        AdjacencyListGraph adjListGraph = new AdjacencyListGraph();
        IntStream.range(0, numNodes).forEach(ignored->adjListGraph.addNode());

        addAllToGraph(adjListGraph);

        return adjListGraph;
    }

    private void addAllToGraph(Graph graph){
        if(numNodes > 0) {
            for (int i = 0; i < numNodes; i++) {
                for (int j = 0; j < numNodes; j++) {
                    if (matrix[i][j] > 0) {
                        graph.addEdge(i, j, matrix[i][j]);
                    }
                }
            }
        }
    }
}