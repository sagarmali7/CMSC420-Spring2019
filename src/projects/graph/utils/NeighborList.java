package projects.graph.utils;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * <p>{@link NeighborList} is a simple linked list with head and tail references. It offers constant-time addition to the
 * front and the back. It also offers methods for searching and deletion.</p>
 *
 * <p>The only reason for the existence of this list is because {@link projects.graph.AdjacencyListGraph} contains a single-dimensional
 * raw array over lists of nodes. Creating a raw array of generics in Java is a pain, with several castings that are usually unsafe and
 * flagged by the compiler as such (or making the runtime vulnerable). See the writeup for more.</p>
 *
 * <p>Finally, note that this class has been declared {@link Iterable} over {@link Neighbor} instances. This means that it implements a
 * method called {@link #iterator()} which returns an instance of {@link Iterator} over instances of the class {@link Neighbor}. As a result,
 * this list can be looped through using a &quot;for-each&quot; loop. To make the iterator &quot;fail-fast&quot;, we make sure that
 * calls to {@link Iterator#next()} after calls to insertion or removal methods throw an instance of {@link java.util.ConcurrentModificationException}.
 * If you want more information on &quot;fail-fast&quot; and &quot;fail-safe&quot; iterators, a good place to start would be the documentation of
 * {@link java.util.ConcurrentModificationException}, located online <a href="https://docs.oracle.com/javase/8/docs/api/java/util/ConcurrentModificationException.html">here</a>.</p>
 *
 * @author <a href="mailto:jason.filippou@gmail.com">Jason Filippou</a>
 * @see Neighbor
 * @see projects.graph.AdjacencyListGraph
 */
public class NeighborList implements Iterable<Neighbor> {

    private class Node {
        Neighbor neighbor;
        Node next;

        Node(int node, int weight, Node next) {
            neighbor = new Neighbor(node, weight);
            this.next = next;
        }

        Node(int node, int weight) {
            this(node, weight, null);
        }
    }

    private Node head, tail; // Tail pointer added for efficiency.
    private int count;
    private boolean modificationFlag; // This will be required to make the iterator fail-fast.

    /**
     * Simple constructor.
     */
    public NeighborList() {
        head = tail = null;
        count = 0;
        modificationFlag = false;
    }

    /**
     * Non-default constructor which initializes the list as a single-element list.
     *
     * @param node   The ID of the neighbor node.
     * @param weight The weight associated with the edge to the neighbor node.
     */
    public NeighborList(int node, int weight) {
        head = tail = new Node(node, weight);
        count = 1;
        modificationFlag = false;
    }

    /**
     * Stores the (node, weight) pair at the back of the list.
     *
     * @param node   The ID of the neighbor node.
     * @param weight The weight associated with the edge to the neighbor node.
     */
    public void addBack(int node, int weight) {
        if (tail == null) {
            assert head == null : "Tail of list being null implies its head also being null.";
            head = tail = new Node(node, weight);
        } else {
            tail.next = new Node(node, weight);
            tail = tail.next;
        }
        count++;
        modificationFlag = true;
    }

    /**
     * Stores the (node, weight) pair at the front of the list.
     *
     * @param node   The ID of the neighbor node.
     * @param weight The weight associated with the edge to the neighbor node.
     */
    public void addFront(int node, int weight) {
        if (head == null) {
            assert tail == null : "Head and Tail can only be null together.";
            head = tail = new Node(node, weight);
        } else {
            head = new Node(node, weight, head);
        }
        count++;
        modificationFlag = true;
    }

    /**
     * Asks the list about the existence of a particular neighbor.
     *
     * @param node The ID of the neighbor node.
     * @return <tt>true</tt> if, and only if, node is a neighbor node of the node we called the method for, false otherwise.
     */
    public boolean containsNeighbor(int node) {
        Node current = head;
        while (current != null) {
            if (current.neighbor.getNode() == node)
                return true;
            current = current.next;
        }
        return false;
    }

    /**
     * If existent, removes the provided node from the neighbor list. Otherwise, has no effect.
     *
     * @param node The ID of the neighbor node.
     */
    public void remove(int node) {
        Node current = head;
        Node previous = null;
        while (current != null) {
            if (current.neighbor.getNode() == node) {
                if (previous != null) {
                    previous.next = current.next; // Throw away the node
                }
                if (current == head) {
                    assert previous == null : "If we find the element at the beginning of the list, previous should be null.";
                    head = head.next;
                }
                if (current == tail) {
                    assert current.next == null : "If we find the element at the end of the list, the next element should be null.";
                    tail = tail.next;
                }
                count--;
                break;
            }
            previous = current;
            current = current.next;
        }
        modificationFlag = true;
    }

    /**
     * Sets the weight of node to weight.
     *
     * @param node   The ID of the neighbor node.
     * @param weight The weight with which we want to associate the edge to the neighbor node.
     */
    public void setWeight(int node, int weight) {
        Node current = head;
        while (current != null) {
            if (current.neighbor.getNode() == node) {
                current.neighbor.setWeight(weight);
                break;
            }
            current = current.next;
        }
        // We won't set modificationFlag for this method, since it doesn't affect the number of nodes
        // in the list. In fact, it is one of the methods that might be called by the Iterator<Neighbor>
        // returned by iterator().
    }


    /**
     * Stores the (node, weight) pair at the back of the list.
     *
     * @param node The ID of the neighbor node.
     * @return The weight of the edge between the node for which we called the method and the argument node.
     */
    public int getWeight(int node) {
        Node current = head;
        while (current != null) {
            if (current.neighbor.getNode() == node)
                return current.neighbor.getWeight();
            current = current.next;
        }
        return 0;
    }

    /**
     * Queries the list about its count (number of nodes).
     *
     * @return The number of nodes in the list.
     */
    public int getCount() {
        assert count >= 0 : "getCount(): Inconsistent value of list count encountered: " + count + ".";
        return count;
    }

    @Override
    public Iterator<Neighbor> iterator() {
        return new NeighborListIterator();
    }

    private class NeighborListIterator implements Iterator<Neighbor> {

        private Node currNode;

        NeighborListIterator() {
            modificationFlag = false; // All modifications until this iterator is constructed are not relevant.
            currNode = head;
        }

        @Override
        public boolean hasNext() {
            return currNode != null;
        }

        @Override
        public Neighbor next() {
            if (modificationFlag) // Some other method modified the collection before we called next()...
                throw new ConcurrentModificationException("NeighborListIterator::next(): Encountered a concurrent modification.");
            Neighbor retVal = currNode.neighbor;
            currNode = currNode.next;
            return retVal;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("This Iterator does not implement remove().");
        }
    }

}
