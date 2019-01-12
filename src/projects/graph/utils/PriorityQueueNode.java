package projects.graph.utils;

import projects.graph.Graph;

/**
 * <p>{@link PriorityQueueNode} is a class that describes the nodes held by the {@link java.util.PriorityQueue} instance
 * used in {@link Graph#shortestPath(int, int)}. {@link PriorityQueueNode} instances also contain information about the
 * order that a node was created and inserted into the priority queue, to help break ties in FIFO order. </p>
 *
 * @see Graph
 * @see Graph#shortestPath(int, int)
 * @see java.util.PriorityQueue
 *
 * @author <a href="mailto:jason.filippou@gmail.com">Jason Filippou</a>
 */
public class PriorityQueueNode {

    private int node, distance, order;

    /*
     * Simple constructor. As a side effect, stores the index of insertion into the container priority queue.
     * @param node The node id.
     * @param distance The distance between the source node and the provided node.
     */
    public PriorityQueueNode(int node, int distance, int order){
        this.node = node;
        this.distance = distance;
        this.order = order;
    }

    /**
     * One-arg constructor. Calls two-arg constructor in body.
     */
    public PriorityQueueNode(int node, int order){
        this(node, Graph.INFINITY, order);
    }

    /**
     * Simple accessor for the node id.
     * @return The node id.
     */
    public int getNode(){
        return node;
    }

    /**
     * Simple accessor for the distance.
     * @return The distance from the source node to the stored node.
     */
    public int getDistance(){
        return distance;
    }

    /**
     * Simple accessor for the order that this
     */
    public int getOrder(){
        return order;
    }

    /** <p> Type-safe equality method that only checks for equality of node  (after the runtime class check). To be clear,
     * two {@link PriorityQueueNode} instances will be considered equal if they contain the same node id. We do not care about
     * weight and order for equality considerations. </p>
     *
     * @return true iff the argument is of runtime type {@link PriorityQueueNode} and the node id contained by
     * the argument is the same one as the node id stored in this.
     */
    @Override
    public boolean equals(Object o){
        if(o == null || o.getClass() != this.getClass())
            return false;
        PriorityQueueNode oCasted = null;
        try {
            oCasted = (PriorityQueueNode)o;
        } catch (ClassCastException ignored){
            return  false;
        }
        // We will equate PriorityQueueNode instances based only on the
        // node field, because of the requirements of Graph::shortestPath().
        return node == oCasted.node;
    }

}
