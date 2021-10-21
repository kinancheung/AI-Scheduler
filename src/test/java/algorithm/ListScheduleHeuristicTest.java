package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import data.ApplicationState;
import data.TaskNode;

/**
 * Author: Kirsty Gong Date: 08/08/2021
 *
 * This is a test class for ListScheduleHeuristic class which tests the methods
 * within that class.
 */

public class ListScheduleHeuristicTest {
    private ListScheduleHeuristic listScheduleHeuristic;
    private DependencyGraph dependencyGraph;

    @Before
    public void setUp() {
        listScheduleHeuristic = ListScheduleHeuristic.getInstance();

    }

    /**
     * tests the getInstance method
     */
    @Test
    public void testGetInstance() {
        dependencyGraph = DependencyGraph.getDependencyGraph();
        ApplicationState.getInstance().setInputFile("./src/test/input/test.dot");
        dependencyGraph.readFile();
        ListScheduleHeuristic actual = ListScheduleHeuristic.getInstance();
        ListScheduleHeuristic expect = ListScheduleHeuristic.getInstance();
        Assert.assertSame(expect, actual);
    }

    /**
     * test the heuristicPriority method again a single branch test file
     */
    @Test
    public void testHeuristicPrioritySingleBranch() {
        dependencyGraph = DependencyGraph.getDependencyGraph();
        ApplicationState.getInstance().setInputFile("./src/test/input/testSingleFinishPoint.dot");
        dependencyGraph.readFile();
        listScheduleHeuristic = ListScheduleHeuristic.getInstance();
        List<TaskNode> node = listScheduleHeuristic.heuristicPriority();
        List<String> result = convertTaskNodeListToStringList(node);
        List<String> expect = Arrays.asList("a", "b", "c", "d", "e");
        Assert.assertEquals(expect, result);

    }

    /**
     * test the heuristicPriority method again a single entry and finish point test
     * file
     */
    @Test
    public void testHeuristicPrioritySingleRootAndSingleLeaf() {
        dependencyGraph = DependencyGraph.getDependencyGraph();
        ApplicationState.getInstance().setInputFile("./src/test/input/test.dot");
        dependencyGraph.readFile();
        listScheduleHeuristic = ListScheduleHeuristic.getInstance();
        List<TaskNode> node = listScheduleHeuristic.heuristicPriority();
        List<String> result = convertTaskNodeListToStringList(node);
        List<String> expect = Arrays.asList("a", "b", "c", "d");
        Assert.assertEquals(result, expect);

    }

    /**
     * test the heuristicPriority method again a test file with multiple leaf test
     * file
     */
    @Test
    public void testHeuristicPriorityMultipleLeaf() {
        dependencyGraph = DependencyGraph.getDependencyGraph();
        ApplicationState.getInstance().setInputFile("./src/test/input/Nodes_7_OutTree.dot");
        dependencyGraph.readFile();
        listScheduleHeuristic = ListScheduleHeuristic.getInstance();
        List<TaskNode> node = listScheduleHeuristic.heuristicPriority();
        List<String> result = convertTaskNodeListToStringList(node);
        List<String> expect = Arrays.asList("a", "b", "f", "g", "d", "c", "e");
        Assert.assertEquals(expect, result);
    }

    /**
     *
     * test the heuristicPriority method again multiple root test file
     */
    @Test
    public void testHeuristicPriorityMultipleRoot() {

        dependencyGraph = DependencyGraph.getDependencyGraph();
        ApplicationState.getInstance().setInputFile("./src/test/input/testMultipleEntryPoint.dot");
        dependencyGraph.readFile();
        listScheduleHeuristic = ListScheduleHeuristic.getInstance();
        List<TaskNode> node = listScheduleHeuristic.heuristicPriority();
        List<String> result = convertTaskNodeListToStringList(node);
        List<String> expect = Arrays.asList("a", "c", "b");
        Assert.assertEquals(expect, result);
    }

    /**
     *
     * test the bottomLevelWeightRecursive with a root node
     */
    @Test
    public void testBottomLevelWeightRecursiveRoot() {
        dependencyGraph = DependencyGraph.getDependencyGraph();
        ApplicationState.getInstance().setInputFile("./src/test/input/test.dot");
        dependencyGraph.readFile();
        listScheduleHeuristic = ListScheduleHeuristic.getInstance();
        TaskNode node = dependencyGraph.nodeMap.get("a");
        Assert.assertEquals(7, listScheduleHeuristic.bottomLevelWeightRecursive(node));
    }

    /**
     *
     * test the bottomLevelWeightRecursive with a leave node
     */
    @Test
    public void testBottomLevelWeightRecursiveLeaf() {
        dependencyGraph = DependencyGraph.getDependencyGraph();
        ApplicationState.getInstance().setInputFile("./src/test/input/test.dot");
        dependencyGraph.readFile();
        listScheduleHeuristic = ListScheduleHeuristic.getInstance();
        TaskNode node = dependencyGraph.nodeMap.get("d");
        Assert.assertEquals(2, listScheduleHeuristic.bottomLevelWeightRecursive(node));
    }

    /**
     * test the bottomLevelWeightRecursive with a node with multiple edges
     */
    @Test
    public void testBottomLevelWeightRecursiveMultipleEdge() {
        dependencyGraph = DependencyGraph.getDependencyGraph();
        ApplicationState.getInstance().setInputFile("./src/test/input/Nodes_7_OutTree.dot");
        dependencyGraph.readFile();
        listScheduleHeuristic = ListScheduleHeuristic.getInstance();
        TaskNode node = dependencyGraph.nodeMap.get("b");
        Assert.assertEquals(13, listScheduleHeuristic.bottomLevelWeightRecursive(node));
    }

    public List<String> convertTaskNodeListToStringList(List<TaskNode> nodes) {
        List<String> result = new ArrayList<>();
        for (TaskNode n : nodes) {
            result.add(n.getTaskName());
        }
        return result;
    }
}
