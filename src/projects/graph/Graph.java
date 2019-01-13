package projects.graph;

import java.util.List;
import java.util.Set;

/**
 * <p>{@link Graph} is an abstraction over directed and weighted graphs. It supports insertion of nodes, insertion
 * and removal of edges, queries of connectedness, simple accessors, as well as shortest path computation.
 * The weights are <b>non-negative</b> and are bounded above by the field {@link #INFINITY}.</p>
 *
 * <p>Nodes in {@link Graph} are simplistic in that they are characterized just by an increasing index. So, the first node
 * inserted would be considered node 0 (zero), the second one node 1 and so on and so forth. For this reason, removal
 * of <b>nodes</b> from a {@link Graph} instance is <b>not</b> supported, otherwise inconsistencies with the internal
 * representation of the instance could occur. For example, if we have 8 nodes and we remove the first one inserted,
 * should we consider a new node to have index 9 or 8? It's unclear, so we just let go of the requirement. </p>
 *
 * @see AdjacencyMatrixGraph
 * @see SparseAdjacencyMatrixGraph
 * @see AdjacencyListGraph
 *
 * @author <a href ="mailto:jason.filippou@gmail.com">Jason Filippou</a>
 */
public abstract class Graph {

    /**
     * <p>A named constant that describes infinity. Useful for shortest path computation.</p>
     * @see #shortestPath(int, int)
     */
    public static final int INFINITY = Integer.MAX_VALUE;

    /**
     * <p>A {@link RuntimeException} instance that will be used by implementing subclasses
     * to warn you that you need to implement all the public methods.
     */
    protected static final RuntimeException UNIMPL_METHOD = new RuntimeException("Implement this method!");

    /**
     * <p>Add a new node to the graph. The node will be characterized by its index of insertion, such that the first
     * time this method is called, a node with index &#39; 0 &#39; is stored in the graph, the second one we will
     * store a node with index &#39; 1 &#39;, etc, etc.</p>
     */
    public abstract void addNode();

    /**
     * <p>Adds an edge from node source to node dest in the graph, attaching to it weight equal to
     * weight. In this implementation, we <b>don't allow</b> weights to be negative. If a weight that
     * is either negative or greater than {@link #INFINITY} is provided, the method should throw a
     * {@link RuntimeException}. If the edge already exists, <b>its weight should be updated to the parameter
     * value.</b></p>
     *
     * <p>If either source or dest don't exist in the graph, the behavior is <i>undefined</i>. This
     * means that you can do <b>whatever</b> you want, as long as you don't store extra edges or nodes in the graph. You
     * can throw a {@link RuntimeException}, return null, print a message, anything. We do <b>not</b> test for this case.</p>
     *
     * <p>We treat the addition of a weight with zero weight exactly the same way we would treat the deletion of an edge
     * through {@link #deleteEdge(int, int)}. So, the calls add(i, j, 0) and deleteEdge(i, j) are equivalent. </p>
     *
     * @param source The source node of the edge.
     * @param dest The &quot;sink&quot; node of the edge.
     * @param weight  The weight of the edge.
     *
     * @throws RuntimeException if weight &lt; 0 or weight &gt; {@link #INFINITY}
     */
    public abstract void addEdge(int source, int dest, int weight) throws RuntimeException;

    /**
     * <p>Delete the edge from source to dest. If the edge does not exist, or if either node does <b>not</b> exist
     * in the graph, no action should be performed.</p>
     * <p>We would like to particularly stress that <b>removing an edge does not in any way imply removing its constituent nodes,
     * even if the nodes end up being singleton disconnected components!</b></p>
     * @param source The &quot;source&quot; node of the edge.
     * @param dest The &quot;sink&quot; node of the edge.
     */
    public abstract void deleteEdge(int source, int dest);

    /**
     * <p>Queries the graph about the existence of an edge from source to dest. If either source
     * or dest are not contained by the graph, then the edge itself cannot possibly exist and the method should return
     * <b>false</b>.</p>
     * @param source The source node of the edge.
     * @param dest The &quot; sink &quot; of the edge.
     * @return true if, and only if, source, dest and the edge source-&gt;dest <b>all</b> exist in the graph, false otherwise.
     */
    public abstract boolean edgeBetween(int source, int dest);

    /**
     * <p>Returns the weight of the edge source-&gt;dest. If the edge does <b>not</b> exist (either
     * because at least one of the two nodes does not exist or because they both exist but there
     * is no edge between them), this method should return 0. </p>
     * @param source The source  node of the edge.
     * @param dest The &quot; sink &quot; of the edge.
     * @return the weight of the edge source-&gt;dest
     */
    public abstract int getEdgeWeight(int source, int dest);

    /**
     * <p>Retrieves the &quot;neighbor&quot; nodes of the provided node, i.e all the nodes to which it points.
     * If the node does <b>not</b> exist in the graph, the behavior is <b>undefined</b>; you can do whatever you want (and that helps you debug!)
     * and we don't test for it. If the node <b>does</b> exist in the graph but it has no neighbors, the returned {@link Set} should be non-null,
     * but  <b>empty (size 0)</b>.</p>
     * @param node The node to retrieve the neighbors of.
     * @return A {@link Set} of all nodes to which node is incident (whether it is the source <b>OR</b> sink).
     */
    public abstract Set<Integer> getNeighbors(int node);

    /**
     * <p>Retrieves the number of nodes in the graph.</p>
     * @return the number of nodes in the graph.
     */
    public abstract int getNumNodes();

    /**
     * <p>Retrieves the number of edges in the graph. </p>
     * @return the number of edges in the graph.
     */
    public abstract int getNumEdges();

    /**
     * Clears the graph of all its nodes and edges, resulting in an empty graph.
     */
    public abstract void clear();

    /** <p>Returns the shortest path between source and dest in the graph, where the shortest path
     * is defined as the path with a minimal sum of weights of the constituent edges. Since we don't allow
     * negative weight edges, you can implement this with Dijkstra's, Bellman-Ford or Johnson's algorithm, whichever one
     * you prefer!</p>
     *
     * <p>Shortest path computation only requires methods of {@link Graph}'s interface (i.e the other public
     * methods). For this reason, defining it only once, in the base class, is the best approach. This <b>does</b>,
     * however, require that {@link Graph} is made into an abstract class instead of an interface.</p>
     * <p>Notes:</p>
     *
     * <ol>
     *      <li>The code will have to be <b>your own</b>. No taking ready-made libraries out there, whether they
     *       are part of Oracle's standard library or not. However, if you choose to implement Dijkstra's algorithm,
     *       you <b>are</b> allowed to use a third-party implementation of a <b>priority queue</b>, which is a data
     *       structure required for an efficient implementation of Dijkstra's. You can also use other ready-made
     *       data structures, such as hash tables or lists.</li>
     *
     *      <li>If either source or dest are not part of the graph, the behavior is <b>undefined</b>
     *          (we don't test for this case).</li>
     *
     *      <li>Since {@link Graph} represents <b>directed graphs</b>, even if source == dest, there
     *          <b>may not exist</b> a path that connects source and dest.</li>
     *
     *      <li>If there is no path from source to dest, the list reference returned should be non-null, but
     *          the list itself should be <b>completely empty</b>.</li>
     *
     *      <li>We do <b>not</b> test you on &lt; source, dest &gt; pairs for which there exists more than one
     *              shortest paths, so your implementation can handle such cases in whichever way it wants. </li>
     *</ol>
     *
     * @param source The source node of the edge.
     * @param dest The &quot; sink &quot; node of the edge.
     * @return An ordered {@link List} whose first (head) element is source, the last (tail) element is dest
     * and all the intermediate nodes make up the path from source to dest.
     */
    public List<Integer> shortestPath(int source, int dest){ throw UNIMPL_METHOD; }

}