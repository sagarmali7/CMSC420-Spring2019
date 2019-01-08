package projects.graph.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>{@link NeighborList} is a simple linked list with head and tail references. It offers constant-time addition to the
 * front and the back. It also offers methods for searching and deletion.</p>
 *
 * <p>The only reason for the existence of this list is because {@link projects.graph.AdjacencyListGraph}</p> contains a single-dimensional
 * raw array over lists of nodes. Creating a raw array of generics in Java is a pain, with several castings that are usually unsafe and
 * flagged by the compiler as such (or making the runtime vulnerable). See the writeup for more.</p>
 *
 * @author  <a href="mailto:jason.filippou@gmail.com">Jason Filippou</a>
 *
 * @see projects.graph.AdjacencyListGraph
 */
public class NeighborList {

    private class Node {
        int node, weight;
        Node next;

        Node(int node, int weight, Node next){
            this.node = node;
            this.weight = weight;
            this.next = next;
        }

        Node(int node, int weight){
            this(node, weight, null);
        }
    }

    private Node head, tail; // Tail pointer added for efficiency.
    private int count;

    /**
     * Simple constructor.
     */
    public NeighborList(){
        head = tail = null;
        count = 0;
    }

    /**
     * Non-default constructor which initializes the list as a single-element list.
     * @param node The ID of the neighbor node.
     * @param weight The weight associated with the edge to the neighbor node.
     */
    public NeighborList(int node, int weight){
        head = tail = new Node(node, weight);
        count = 1;
    }

    /**
     * Stores the (node, weight) pair at the back of the list.
     * @param node The ID of the neighbor node.
     * @param weight The weight associated with the edge to the neighbor node.
     */
    public void addBack(int node, int weight){
        if(tail == null){
            assert head == null : "Tail of list being null implies its head also being null.";
            head = tail = new Node(node, weight);
        } else {
            tail.next = new Node(node, weight);
            tail = tail.next;
        }
        count++;
    }

    /**
     * Stores the (node, weight) pair at the front of the list.
     * @param node The ID of the neighbor node.
     * @param weight The weight associated with the edge to the neighbor node.
     */
    public void addFront(int node, int weight){
        if(head == null){
            assert tail == null : "Head and Tail can only be null together.";
            head = tail = new Node(node, weight);
        } else {
            head = new Node(node, weight, head);
        }
    }

    /**
     * Asks the list about the existence of a particular neighbor.
     * @param node The ID of the neighbor node.
     * @return <tt>true</tt> if, and only if, node is a neighbor node of the node we called the method for, false otherwise.
     */
    public boolean containsNeighbor(int node){
        while(head !=null) {
            if (head.node == node)
                return true;
            head = head.next;
        }
        return false;
    }

    /**
     * If existent, removes the provided node from the neighbor list. Otherwise, has no effect.
     * @param node The ID of the neighbor node.
     */
    public void remove(int node){
        Node current = head;
        Node previous = null;
        while(current != null){
            if(current.node == node){
                if(previous != null){
                    previous.next = current.next; // Throw away the node
                }
                if(current == head){
                    assert previous == null : "If we find the element at the beginning of the list, previous should be null.";
                    head = head.next;
                }
                if(current == tail){
                    assert current.next == null : "If we find the element at the end of the list, the next element should be null.";
                    tail = tail.next;
                }
                count--;
                break;
            }
            previous = current;
            current = current.next;
        }
    }

    /**
     * Sets the weight of node to weight.
     * @param node The ID of the neighbor node.
     * @param weight The weight with which we want to associate the edge to the neighbor node.
     */
    public void setWeight(int node, int weight){
        Node current = head;
        while(current != null) {
            if (current.node == node) {
                current.weight = weight;
                break;
            }
            current = current.next;
        }
     }


    /**
     * Stores the (node, weight) pair at the back of the list.
     * @param node The ID of the neighbor node.
     * @return The weight of the edge between the node for which we called the method and the argument node.
     */
    public int getWeight(int node){
        Node current = head;
        while(current != null) {
            if (current.node == node)
                return current.weight;
            current = current.next;
        }
        return 0;
    }

    /**
     * Returns a {@link Set} that contains the node ids stored in the list.
     * @return an instance of {@link Set} with the node ids stored in the current list.
     * @see Set
     */
    public Set<Integer> toSet(){
        HashSet<Integer> set = new HashSet<>();
        Node current = head;
        while(current != null){
            set.add(current.node);
            current = current.next;
        }
        return set;
    }
}
