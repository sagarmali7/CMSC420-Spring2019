package projects.graph;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.fail;


/**
 * <p>A testing framework for {@link Graph} instances.</p>
 * @author <a href="mailto:jason.filippou@gmail.com">Jason Filippou</a>
 * @see Graph
 * @see AdjacencyMatrixGraph
 * @see SparseAdjacencyMatrixGraph
 * @see AdjacencyListGraph
 */
public class ReleaseTests {

    /* ********************************************************************************************/
    /* *************************************** PRIVATE METHODS ************************************/
    /* ********************************************************************************************/

    private static String format(Throwable t){
        return "Caught a " + t.getClass()+ " with message: " + t.getMessage() + ".";
    }

    /* We will make calls to the same helper methods, defined below, from all of our possible
     * Graph instances.
     */


    private void testAddNode(Graph graph){
        for(int ignored = 0; ignored < 7; ignored++)
            graph.addNode();
    }

    private void testAddEdge(Graph graph){
        graph.addEdge(0, 2, 2);
        graph.addEdge(0, 2, 3);// Should update the weight
        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 5, 2);
        graph.addEdge(1, 2, 3);
        graph.addEdge(1, 6, 4);
        graph.addEdge(2, 4, 5);
        graph.addEdge(3, 2, 8);
        graph.addEdge(3, 6, 6);
        graph.addEdge(4, 0, 4);
        graph.addEdge(4, 3, 4);
        graph.addEdge(5, 1, 2);
        graph.addEdge(5, 3, 1);
        graph.addEdge(6, 4, 1);
        graph.addEdge(6, 5, 3);
        RuntimeException exc = null;
        try {
            graph.addEdge(6, 0, -1);
        } catch(RuntimeException thrown){
            exc = thrown;
        }
        if(exc == null) {
            throw new AssertionError("Tried to add a negative edge " +
                    "and didn't receive a RuntimeException instance");
        }

    }

    private void testDeleteEdge(Graph graph, boolean rebuildGraph){
        if(rebuildGraph)
            buildGraph(graph);

        // And then test whether deletions happen ok.
        graph.deleteEdge(1, 5); // Whether they are deletions of valid edges
        graph.deleteEdge(0, 6);; // Or invalid edges
        graph.addEdge(3, 6, 0); // Equivalent to deletion of a valid edge
        graph.addEdge(6, 6, 0); // And of an invalid edge.
    }

    private void testIsConnected(Graph graph, boolean rebuildGraph){
        if(rebuildGraph)
            buildGraph(graph);

        // Make sure the code respects the directionality of the edges
        if(!graph.edgeBetween(4, 3))
            throw new AssertionError("Node 3 *is* connected to node 4 through an edge "
                    + " of weight " + graph.getEdgeWeight(4, 3) + ".");
        if(graph.edgeBetween(3, 4))
            throw new AssertionError("While node 3 *is* connected to node 4, node " +
                    "4 is *not* connected to node 3!");
        if(graph.edgeBetween(4, 6))
            throw new AssertionError("Node 4 is *not* connected to node 6.");
        if(!graph.edgeBetween(6, 4))
            throw new AssertionError("Node 6 *is* connected to node 4 with an " +
                    "edge of weight " + graph.getEdgeWeight(6, 4));

    }

    private void testGetEdgeWeight(Graph graph, boolean rebuildGraph){
        if(rebuildGraph)
            buildGraph(graph);

        if(graph.getEdgeWeight(4, 0) != 4)   // Retrieve the weight of an existing edge...
            throw new AssertionError("The weight on the edge connecting node 4 to node 0 " +
                    "should be 4, but instead it is: " + graph.getEdgeWeight(4, 0));

        // And of some non-existing ones.
        if(graph.getEdgeWeight(7, 3) != 0) // Node 7 not even in the graph
            throw new AssertionError("Node 7 does not even exist in our graph, " +
                    "which means that the weight of the edge from node 7 to node 3 should be 0");
        if(graph.getEdgeWeight(1, 4) != 0)
            throw new AssertionError("Node 1 is not connected to node 4, which " +
                    "means that the weight of the edge from node 1 to node 4 should be 0.");

        // Make sure directionality is respected once again...
        if(graph.getEdgeWeight(2, 1) != 0)
            throw new AssertionError("Node 1 is connected to node 2 with a weight of " +
                graph.getEdgeWeight(1, 2) + ", but node 2 is *not* connected to node 1. " +
                    "Therefore, the weight of the edge from 2 to 1 should be 0.");

    }

    private void testGetNumNodes(Graph graph, boolean rebuildGraph){
        if(rebuildGraph)
            buildGraph(graph);

        if(graph.getNumNodes() != 7)
            throw new AssertionError("We built a graph with 7 nodes, but " +
                    "the graph reported " + graph.getNumNodes() + " nodes.");

    }

    private void testGetNumEdges(Graph graph, boolean rebuildGraph){
        if(rebuildGraph)
            buildGraph(graph);

        if(graph.getNumEdges() != 14)
            throw new AssertionError("We built a graph with 14 edges, but " +
                    "the graph reported " + graph.getNumEdges() + " edges.");
    }

    private void testGetNeighbors(Graph graph, boolean rebuildGraph){
        if(rebuildGraph)
            buildGraph(graph);

        // Node 1 has 2 and 6 as neighbors.
        Set<Integer> neighborsOf1 = graph.getNeighbors(1);
        if(!neighborsOf1.contains(2)  ||  !neighborsOf1.contains(6))
            throw new AssertionError("Neighbors of node 1 are 2 and 6, instead reported: "
                + neighborsOf1 + ".");

        // Node 1 is pointed to by 0 and 5, but that doesn't mean they're neighbors. In fact, they're not!
        // Furthermore, 3 and 4 are also not neighbors.
        if(neighborsOf1.stream().anyMatch(Arrays.asList(0, 1,  3, 4, 5)::contains))
            throw new AssertionError("0, 3, 4, and 5 are *not* neighbors of 1. Instead, neighbors of 1 " +
                    "reported: " + neighborsOf1 + ".");


        // Node 2 has 4 as its single neighbor.
        Set<Integer> neighborsOf2 = graph.getNeighbors(2);
        if(!neighborsOf2.contains(4))
            throw new AssertionError("4 is 2's single neighbor. Instead, 2 reported the following neighbors: " +
                    neighborsOf2);
        if(neighborsOf2.stream().anyMatch(Arrays.asList(0, 1, 2, 3, 5, 6)::contains))
            throw new AssertionError("0, 1, 2,  3, 5, and 6 are *not* neighbors of 2. Instead, neighbors of 2 are: " +
                    "reported: " + neighborsOf2 + ".");

        // Add 2 as a neighbor of itself, and re-check neighbor set.
        graph.addEdge(2, 2, 3);
        if(!neighborsOf2.contains(4) || !neighborsOf2.contains(2))
            throw new AssertionError("4 and 2 are the neighbors of 2. Instead of those, 2 reported the following neighbors: " +
                    neighborsOf2);
        if(neighborsOf2.stream().anyMatch(Arrays.asList(0, 1, 3, 5, 6)::contains))
            throw new AssertionError("0, 1, 3, 5, and 6 are *not* neighbors of 2. Instead, neighbors of 1 " +
                    "reported: " + neighborsOf2 + ".");

        // Delete the edge and re-check neighbor set. Same checks as before.
        graph.deleteEdge(2, 2);
        neighborsOf2 = graph.getNeighbors(2);
        if(!neighborsOf2.contains(4))
            throw new AssertionError("4 is 2's single neighbor. Instead, 2 reported the following neighbors: " +
                    neighborsOf2);
        if(neighborsOf2.stream().anyMatch(Arrays.asList(0, 1, 2, 3, 5, 6)::contains))
            throw new AssertionError("0, 1, 2,  3, 5, and 6 are *not* neighbors of 2. Instead, neighbors of 2 are:" +
                    "reported: " + neighborsOf2 + ".");

        // Make sure that a new node's neighbor query returns an empty Set.
        graph.addNode(); // Node 7.
        if(!graph.getNeighbors(7).isEmpty())
            throw new AssertionError("A freshly created node should have no neighbors");

        // But when we connect it to an existing node, that node should appear in the neighbors.
        graph.addEdge(7, 5, 1);
        if(!graph.getNeighbors(7).contains(5))
            throw new AssertionError("We created the node 7, connected it to 5, but 5 did not appear" +
                    " in 7's neighbors.");
    }

    private int sumEdgeWeights(Graph graph, Integer... nodes){
        assert nodes != null : "sumEdgeWeights(): received null argument";
        int retVal = 0;
        for(int i = 0; i < nodes.length - 1; i++)
            retVal += graph.getEdgeWeight(nodes[i], nodes[i+1]);
        retVal += graph.getEdgeWeight(nodes[nodes.length - 2], nodes[nodes.length - 1]);
        return  retVal;
    }

    private boolean examineShortestPath(List<Integer> computedPath, int computedCost,
                                        int actualCost, Integer... actualPath){
        assert computedPath != null && actualPath != null : "examineShortestPath(): received null arguments.";
        if(actualCost == Graph.INFINITY)
            return computedPath.isEmpty(); // If the node is not reachable, then make sure no path was generated.
        if(computedCost != actualCost)
            return false;
        int index = 0;
        for(Integer i : computedPath)
            if (!i.equals(actualPath[index++]))
                return false;
        return true;
    }

    private void testShortestPath(Graph graph, boolean rebuildGraph){
        if(rebuildGraph)
            buildGraph(graph);
        List<Integer> sp1, sp2, sp3, sp4, sp5, sp6;

        // Shortest path from 0 to 6 is 0->5->1->6, with cost 8
        sp1 = graph.shortestPath(0, 6);
        int cost1 = sumEdgeWeights(graph, 0, 5, 1, 6);
        if(!examineShortestPath(sp1, cost1, 8, 0, 5, 1, 6))
            throw new AssertionError("Shortest path from 0 to 6 is 0->5->1->6, with cost 8. Code reported a path of: "
            + sp1 + " with cost: " + cost1 + ".");

        // Shortest path from 1 to 0 is 1->6->4->0 with cost 9
        sp2 = graph.shortestPath(1, 0);
        int cost2 = sumEdgeWeights(graph, 1, 6, 4, 0);
        if(!examineShortestPath(sp2, cost2, 9, 1, 6, 4, 0))
            throw new AssertionError("Shortest path from 1 to 0 is 1->6->4->0, with cost 9. " +
                    "Code reported a path of: "  + sp2 + " with cost: " + cost2 + ".");

        // Shortest path from 5 to 1 is the edge that connects them, 5->1, with cost 2
        sp3=graph.shortestPath(5, 1);
        int cost3 = sumEdgeWeights(graph, 5, 1);
        if(!examineShortestPath(sp3, cost3, 2, 5, 1))
            throw new AssertionError("Shortest path from 5 to 1 is 5->1, with cost 2. " +
                    "Code reported a path of: "  + sp3 + " with cost: " + cost3 + ".");

        // Shortest path from 1 to 1 is 1->6->5->1, with cost 9
        sp4 = graph.shortestPath(1, 1);
        int cost4 = sumEdgeWeights(graph, 1, 6, 5, 1);
        if(!examineShortestPath(sp4, cost4, 9, 1, 6, 5, 1))
            throw new AssertionError("Shortest path from 1 to 1 is 1->6->5->1, with cost 9. " +
                    "Code reported a path of: "  + sp4 + " with cost: " + cost4 + ".");

        // There is no path between a disconnected node and another node.
        graph.addNode();
        sp5 = graph.shortestPath(2, 7); // Disconnected
        if(!(sp5.isEmpty()))
            throw new AssertionError("We just added a (disconnected) node 7, so there should not exist " +
                    "a path from 2 to 7. Instead, code reported the path: " + sp5 + ".");

        // But once we connect it to 4 with an edge of 1, the shortest path
        // between 7 and 5 becomes 7->4->0->5, with a cost of 7.
        graph.addEdge(7, 4, 1);
        sp6 = graph.shortestPath(7, 5);
        int cost6 = sumEdgeWeights(graph, 7, 4, 0, 5); // cost5 never declared, but it's ok
        if(!examineShortestPath(sp6, cost6, 7, 7, 4, 0, 5))
            throw new AssertionError("Shortest path from newly connected 7 to 5 is 7->4->0->5, with cost 9. " +
                    "Code reported a path of: "  + sp6 + " with cost: " + cost6 + ".");

    }

    private void buildGraph(Graph graph){
        testAddNode(graph);
        testAddEdge(graph);
    }

    private void testGraph(Graph graph){
        // We provide a parameter of true for tests where we want the original graph
        // to be re-constructed. We do this because some of the tests, such as testDeleteEdge(),
        // mutate the graph and might make assumptions of the subsequent tests invalid.
        // So the first test *after* one that might mutate the graph should always be called
        // with a parameter of true. Tests that *follow* tests with a parameter of true
        // should *always* have a parameter of false.
        testDeleteEdge(graph, false);
        testIsConnected(graph, true);
        testGetEdgeWeight(graph, false);
        testGetNumNodes(graph, false);
        testGetNumEdges(graph, false);
        testGetNeighbors(graph, false);
        testShortestPath(graph, true);
    }

    private void runAllTests(Graph graph){
        buildGraph(graph);
        testGraph(graph);
    }

    /* ********************************************************************************************/
    /* *************************************** UNIT TESTS ****************************************/
    /* ********************************************************************************************/

    /* ********************************************************** */
    /* First, tests specific to adjacency matrix representation. */
    /* ********************************************************** */

    @Test
    public void testAddNodeAdjacencyMatrix(){
        try {
            testAddNode(new AdjacencyMatrixGraph());
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testAddEdgeAdjacencyMatrix(){
        try {
            testAddEdge(new AdjacencyMatrixGraph());
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testDeleteEdgeAdjacencyMatrix(){
        try {
            testDeleteEdge(new AdjacencyMatrixGraph(), true);
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testIsConnectedAdjacencyMatrix(){
        try {
            testIsConnected(new AdjacencyMatrixGraph(), true);
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testGetEdgeWeightAdjacencyMatrix() {
        try {
            testGetEdgeWeight(new AdjacencyMatrixGraph(), true);
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testNumNodesAdjacencyMatrix(){
        try {
            testGetNumNodes(new AdjacencyMatrixGraph(), true);
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testNumEdgesAdjacencyMatrix(){
        try {
            testGetNumEdges(new AdjacencyMatrixGraph(), true);
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testGetNeighborsAdjacencyMatrix(){
        try {
            testGetNeighbors(new AdjacencyMatrixGraph(), true);
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testShortestPathAdjacencyMatrix(){
        try {
            testShortestPath(new AdjacencyMatrixGraph(), true);
        } catch(Throwable t){
            fail(format(t));
        }
    }

    /* ***************************************************************** */
    /* Second, tests specific to sparse adjacency matrix representation. */
    /* ***************************************************************** */

    @Test
    public void testAddNodeSparseAdjacencyMatrix(){
        try {
            testAddNode(new SparseAdjacencyMatrixGraph());
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testAddEdgeSparseAdjacencyMatrix(){
        try {
            testAddEdge(new SparseAdjacencyMatrixGraph());
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testDeleteEdgeSparseAdjacencyMatrix(){
        try {
            testDeleteEdge(new SparseAdjacencyMatrixGraph(), true);
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testIsConnectedSparseAdjacencyMatrix(){
        try {
            testIsConnected(new SparseAdjacencyMatrixGraph(), true);
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testGetEdgeWeightSparseAdjacencyMatrix(){
        try {
            testGetEdgeWeight(new SparseAdjacencyMatrixGraph(), true);
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testNumNodesSparseAdjacencyMatrix(){
        try {
            testGetNumNodes(new SparseAdjacencyMatrixGraph(), true);
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testNumEdgesSparseAdjacencyMatrix(){
        try {
            testGetNumEdges(new SparseAdjacencyMatrixGraph(), true);
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testGetNeighborsSparseAdjacencyMatrix(){
        try {
            testGetNeighbors(new SparseAdjacencyMatrixGraph(), true);
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testShortestPathSparseAdjacencyMatrix(){
        try {
            testShortestPath(new SparseAdjacencyMatrixGraph(), true);
        } catch(Throwable t){
            fail(format(t));
        }
    }

    /* ********************************************************** */
    /* Third, tests specific to adjacency list representation. */
    /* ********************************************************** */

    @Test
    public void testAddNodeAdjacencyList(){
        try {
            testAddNode(new AdjacencyListGraph());
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testAddEdgeAdjacencyList(){
        try {
            testAddEdge(new AdjacencyListGraph());
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testDeleteEdgeAdjacencyList(){
        try {
            testDeleteEdge(new AdjacencyListGraph(), true);
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testIsConnectedAdjacencyList(){
        try {
            testIsConnected(new AdjacencyListGraph(), true);
        } catch(Throwable t){
            fail(format(t));
        }

    }

    @Test
    public void testGetEdgeWeightAdjacencyList(){
        try {
            testGetEdgeWeight(new AdjacencyListGraph(), true);
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testNumNodesAdjacencyList(){
        try {
            testGetNumNodes(new AdjacencyListGraph(), true);
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testNumEdgesAdjacencyList(){
        try {
            testGetNumEdges(new AdjacencyListGraph(), true);
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testGetNeighborsAdjacencyList(){
        try {
            testGetNeighbors(new AdjacencyListGraph(), true);
        } catch(Throwable t){
            fail(format(t));
        }
    }

    @Test
    public void testShortestPathAdjacencyList(){
        try {
            testShortestPath(new AdjacencyListGraph(), true);
        } catch(Throwable t){
            fail(format(t));
        }
    }


    /* **************************************************************************  */
    /* Fourth, testing for correct transformations from one type of graph to the   */
    /* other. These tests call the routines buildGraph() and testGraph().          */
    /* *************************************************************************** */


    @Test
    public void testFromAdjMatToSparseAdjMat(){
        Graph graph = new AdjacencyMatrixGraph();
        try {
            buildGraph(graph);
        } catch(Throwable t){
            fail(format(t));
        }
        graph = ((AdjacencyMatrixGraph)graph).toSparseAdjacencyMatrixGraph();
        try {
            testGraph(graph);
        } catch(Throwable t){
            fail("Transformation from AdjacencyMatrixGraph to SparseAdjacencyMatrixGraph: " + format(t));
        }
    }

    @Test
    public void testFromAdjMatToAdjList() {
        Graph graph = new AdjacencyMatrixGraph();
        try {
            buildGraph(graph);
        } catch(Throwable t){
            fail(format(t));
        }
        graph = ((AdjacencyMatrixGraph)graph).toAdjacencyListGraph();
        try {
            testGraph(graph);
        } catch(Throwable t){
            fail("Transformation from AdjacencyMatrixGraph to AdjacencyListGraph: " + format(t));
        }
    }

    @Test
    public void testFromSparseAdjMatToAdjMat(){
        Graph graph = new SparseAdjacencyMatrixGraph();
        try {
            buildGraph(graph);
        } catch(Throwable t){
            fail(format(t));
        }
        graph = ((SparseAdjacencyMatrixGraph)graph).toAdjacencyMatrixGraph();
        try {
            testGraph(graph);
        } catch(Throwable t){
            fail("Transformation from SparseAdjacencyMatrixGraph to AdjacencyMatrixGraph: " + format(t));
        }
    }

    @Test
    public void testFromSparseAdjMatToAdjList(){
        Graph graph = new SparseAdjacencyMatrixGraph();
        try {
            buildGraph(graph);
        } catch(Throwable t){
            fail(format(t));
        }
        graph = ((SparseAdjacencyMatrixGraph)graph).toAdjacencyListGraph();
        try {
            testGraph(graph);
        } catch(Throwable t){
            fail("Transformation from SparseAdjacencyMatrixGraph to AdjacencyListGraph: " + format(t));
        }
    }

    @Test
    public void testFromAdjListToAdjMat(){
        Graph graph = new AdjacencyListGraph();
        try {
            buildGraph(graph);
        } catch(Throwable t){
            fail(format(t));
        }
        graph = ((AdjacencyListGraph)graph).toAdjacencyMatrixGraph();
        try {
            testGraph(graph);
        } catch(Throwable t){
            fail("Transformation from AdjacencyListGraph to AdjacencyMatrixGraph: " + format(t));
        }
    }


    @Test
    public void testFromAdjListToSparseAdjMat(){
        Graph graph = new AdjacencyListGraph();
        try {
            buildGraph(graph);
        } catch(Throwable t){
            fail(format(t));
        }
        graph = ((AdjacencyListGraph)graph).toSparseAdjacencyMatrixGraph();
        try {
            testGraph(graph);
        } catch(Throwable t){
            fail("Transformation from AdjacencyListGraph to SparseAdjacencyMatrixGraph: " + format(t));
        }
    }

}