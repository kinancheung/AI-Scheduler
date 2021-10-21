package algorithm;

import data.ApplicationState;
import data.Schedule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class BranchAndBoundTest {
    private DependencyGraph dependencyGraph;

    @Before
    public void setup() {
        ListScheduleHeuristic.deleteInstance();
    }
    
    /**
     * Test branch and bound on simple multi-input.
     */
    @Test
    public void branchAndBoundRoutineMultipleInput() {
        dependencyGraph = DependencyGraph.getDependencyGraph();
        ApplicationState.getInstance().setInputFile("./src/test/input/testMultipleEntryPoint.dot");
        dependencyGraph.readFile();

        // Get List Heuristic
        ListScheduleHeuristic listScheduleHeuristic = ListScheduleHeuristic.getInstance();
        Schedule initialSchedule = listScheduleHeuristic.schedule;

        // Load Branch and Bound
        BranchAndBound branchAndBound = new BranchAndBound(initialSchedule.getDuration(), initialSchedule);
        Schedule optimal = branchAndBound.BranchAndBoundRoutine();

        String actual = optimal.toDotFile();
        String expected = "digraph \"outputexample\" {\n" +
                "\ta [Weight=9,Start=0,Processor=0];\n" +
                "\tb [Weight=2,Start=9,Processor=0];\n" +
                "\tc [Weight=2,Start=0,Processor=1];\n" +
                "\ta -> b [Weight=1];\n" +
                "\tc -> b [Weight=3];\n" +
                "}";

        Assert.assertEquals(expected, actual);
    }

    /**
     * Test if given an invalid initial schedule, does the schedule remain unchanged.
     */
    @Test
    public void branchAndBoundRoutineInvalidInitialDuration() {
        dependencyGraph = DependencyGraph.getDependencyGraph();
        ApplicationState.getInstance().setInputFile("./src/test/input/test.dot");
        dependencyGraph.readFile();

        // Get List Heuristic
        ListScheduleHeuristic listScheduleHeuristic = ListScheduleHeuristic.getInstance();
        Schedule initialSchedule = listScheduleHeuristic.schedule;

        // Load Branch and Bound
        BranchAndBound branchAndBound = new BranchAndBound(2, initialSchedule);
        Schedule optimal = branchAndBound.BranchAndBoundRoutine();

        String actual = optimal.toDotFile();
        String expected = "digraph \"outputexample\" {\n" +
                "\ta [Weight=2,Start=0,Processor=0];\n" +
                "\tb [Weight=3,Start=2,Processor=0];\n" +
                "\tc [Weight=3,Start=4,Processor=1];\n" +
                "\td [Weight=2,Start=7,Processor=1];\n" +
                "\ta -> b [Weight=1];\n" +
                "\ta -> c [Weight=2];\n" +
                "\tb -> d [Weight=2];\n" +
                "\tc -> d [Weight=1];\n" +
                "}";

        Assert.assertEquals(expected, actual);
    }
}