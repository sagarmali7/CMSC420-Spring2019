package projects.graph;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 * <p><tt>StudentTestsPrototype</tt> is an example class that contains some basic examples. You may write your own tests here.
 * It is <b>very important</b> that you write your own tests! </p>
 * @author --- YOUR NAME HERE! -----
 * @see Graph
 */

public class StudentTests {

    @Test
    public void testEmptyListGraph(){

        AdjacencyListGraph graph = new AdjacencyListGraph();

        // assertTrue is overloaded, which means that we can call it with just one argument (a boolean condition
        // that must be met), or two: a String which should be human-readable and describes what went wrong,
        // followed by our familiar condition. This version is more verbal and gives you more information
        // about what went wrong. This is how we structure our tests on submit.cs so that you have a lot more information
        // about where your tests are failing: replicate this behavior yourselves and save some time!
        assertTrue("After creation, graph should have no nodes.",graph.getNumNodes() == 0);
        assertTrue("After creation, graph should have no edges.", graph.getNumEdges() == 0);

        // Notice how the three-argument version of assertEquals() works: The first argument is a String,
        // which should be human-readable and describes what went wrong, the second is the exact value we expected,
        // while the third one is the value that we got! Remember: (message, expected, actual). *NOT*
        // (message, actual, expected).
        assertEquals("After creation, graph should have no edges and no nodes and edgeBetween() should return false.", false, graph.edgeBetween(0,1));

    }

    @Test
    public void testSimpleAddNodesAndEdgesMatrixGraph(){

        //Initializing an AdjacencyMatrixGraph
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph();
        graph.addNode(); //Adding first node
        assertTrue("After creation, graph should have 1 node after the first insertion.",graph.getNumNodes() == 1);
        graph.addNode(); //Adding second node
        assertTrue("After creation, graph should have 2 nodes after the second insertion.",graph.getNumNodes() == 2);
        graph.addEdge(0,1,5); //Adding first edge
        graph.addEdge(1,0,10); //Adding second edge
        assertTrue("Graph should have 2 edges",graph.getNumEdges() == 2);

    }

    @Test
    public void testSimpleDeletionsSparseGraph(){

        //Initializing a SparseAdjacencyMatrixGraph
        SparseAdjacencyMatrixGraph graph = new SparseAdjacencyMatrixGraph();
        for(int i = 0; i < 5; i++){ // Adding 5 nodes

            graph.addNode();

        }
        //Adding 4 edges
        graph.addEdge(0,1,1);
        graph.addEdge(1,2,1);
        graph.addEdge(2,3,1);
        graph.addEdge(3,4,1);

        //Deleting an edge that doesn't exist. Doing so should not make any changes to the graph.
        graph.deleteEdge(1,0);
        assertTrue("After trying to remove a non-existent edge, the number of edges should not be changed.", graph.getNumEdges() == 4);

        //Deleting an existing edge. Deleting an edge should never remove a node.
        graph.deleteEdge(0,1);
        assertTrue("After trying to remove an existing edge, the graph should have 3 edges",
                graph.getNumEdges() == 3);
        assertTrue("After the successful removal of an edge, the number of nodes should be unchanged.", graph.getNumNodes() == 5);


    }

    @Test
    public void testEdgeStressTest(){

        //Initializing each type of graph
        SparseAdjacencyMatrixGraph sparse = new SparseAdjacencyMatrixGraph();
        AdjacencyListGraph list = new AdjacencyListGraph();
        AdjacencyMatrixGraph matrix = new AdjacencyMatrixGraph();

        for(int i = 0; i < 30; i++){ //Adding 30 nodes

            sparse.addNode();
            list.addNode();
            matrix.addNode();

        }

        for(int i = 0; i < 30; i++){ //Adding 900 edges

            for(int j = 0; j < 30; j++){

                sparse.addEdge(i,j,1);
                list.addEdge(i,j,1);
                matrix.addEdge(i,j,1);

            }

        }

        assertTrue("Sparse matrix graph should have 900 edges but it had " + sparse.getNumEdges() + " edges.",sparse.getNumEdges() == 900);
        assertTrue("List graph should have 900 edges but it had " + list.getNumEdges() + " edges.", list.getNumEdges() == 900);
        assertTrue("Matrix graph should have 900 edges but it had " + matrix.getNumEdges() + " edges.", matrix.getNumEdges() == 900);


    }

}