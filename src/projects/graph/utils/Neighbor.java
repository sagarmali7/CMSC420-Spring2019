package projects.graph.utils;

/** <p>{@link Neighbor} is a class that provides a simple abstraction over nodes to which an implied original node is
 * connected, as well as the weight of the relevant edge. It provides simple construction methods, type-safe equality check,
 * accessors and mutators. </p>
 *
 * @author <a href="mailto:jason.filippou@gmail.com">Jason Filippou</a>
 * @see NeighborList
 */
public class Neighbor {

    private int node, weight;

    /**
     * Simple constructor.
     * @param node The id of the target node of the edge.
     * @param weight The weight of the edge.
     */
    public Neighbor(int node, int weight){
        this.node = node;
        this.weight = weight;
    }

    /**
     * Simple copy-constructor.
     * @param n The {@link Neighbor} instance to base the construction of the current neighbor on.
     * @throws NullPointerException If n = null.
     */
    public Neighbor(Neighbor n){
        this(n.node, n.weight);
    }

    /**
     * Simple setter for node id.
     * @param node The node id to set this with.
     */
    public void setNode(int node){
        this.node = node;
    }

    /**
     * Simple setter for weight.
     * @param  weight The weight to set this with.
     */
    public void setWeight(int weight){
        this.weight = weight;
    }

    /**
     * Simple getter for node id.
     * @return The node id associated with this.
     */
    public int getNode(){
        return node;
    }

    /**
     * Simple getter for weight.
     * @return The weight associated with the edge.
     */
    public int getWeight(){
        return weight;
    }

    @Override
    public boolean equals(Object o){
        if(o == null || o.getClass() != this.getClass()) // Shallow copy check fine for getClass().
            return false;
        Neighbor oCasted = null;
        try {
            oCasted = (Neighbor)o;
        } catch(ClassCastException ignored){
            return false;
        }
        return (oCasted.node == node) && (oCasted.weight == weight);
    }
}
