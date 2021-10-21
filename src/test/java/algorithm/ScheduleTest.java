package algorithm;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import data.ApplicationState;
import data.Schedule;

/**
 * Author: Peter Goedeke Date: 08/08/2021
 *
 * This is a test class for ListScheduleHeuristic class which tests the methods
 * within that class.
 */

public class ScheduleTest {
    private Schedule schedule;
    private DependencyGraph dg;

    @Before
    public void setUp() {
        schedule = new Schedule();
        
        ApplicationState.getInstance().setInputFile("src/test/input/test.dot");
        dg = DependencyGraph.getDependencyGraph();
        dg.readFile();
    }

    /**
     * Test to see if adding a node works
     */
    @Test
    public void testAddNode() {
        schedule.addTask(dg.nodeMap.get("a"), 0);
        Assert.assertEquals(2, schedule.getDuration());
    }
    /**
     * Test to see if adding a node to the other processor works equally well
     */
    @Test
    public void testAddNodeOtherProcessor() {
        schedule.addTask(dg.nodeMap.get("a"), 1);
        Assert.assertEquals(2, schedule.getDuration());
    }
    /**
     * Test to see if adding a node which depends on a node on a different processor works
     */
    @Test
    public void testAddNodeWaitForDelay() {
        schedule.addTask(dg.nodeMap.get("a"), 0);
        schedule.addTask(dg.nodeMap.get("b"), 1);
        Assert.assertEquals(6, schedule.getDuration());
    }
    /**
     * Test to see if adding a node to the same processor as a node it depends on doesn't
     * wait for the communication delay
     */
    @Test
    public void testAddNodeDontWaitForDelay() {
        schedule.addTask(dg.nodeMap.get("a"), 0);
        schedule.addTask(dg.nodeMap.get("b"), 0);
        Assert.assertEquals(5, schedule.getDuration());
    }
    /**
     * Test to see if the correct processor is chosen when not explicitly set
     */
    @Test
    public void testAddNodeChooseBestProcessor() {
        schedule.addTask(dg.nodeMap.get("a"), 0);
        schedule.addTask(dg.nodeMap.get("b"));
        Assert.assertEquals(5, schedule.getDuration());
    }
    /**
     * Test to see that the correct number of free nodes is returned with an empty schedule
     */
    @Test
    public void testFreeNodes() {
        Assert.assertEquals(1, schedule.getFreeNodes().size());
    }
    /**
     * Test to see that the free nodes are updated as nodes as scheduled
     */
    @Test
    public void testFreeNodesAfterAddNode() {
        schedule.addTask(dg.nodeMap.get("a"), 0);
        Assert.assertEquals(2, schedule.getFreeNodes().size());
    }
}