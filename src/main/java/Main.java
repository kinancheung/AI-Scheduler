import algorithm.Algorithm;
import algorithm.BranchAndBound;
import algorithm.BranchAndBoundParallel;
import algorithm.DependencyGraph;
import algorithm.ListScheduleHeuristic;
import javafx.application.Application;
import visualisation.App;
import data.ApplicationState;
import data.Schedule;
import exception.InvalidInputException;
import io.InputParser;
import io.OutputWriter;

public class Main {
    public static void main(String args[]) {  
        
        try {
            InputParser inputParser = new InputParser(ApplicationState.getInstance());
            inputParser.parse(args);
            DependencyGraph.getDependencyGraph().readFile();


            if (ApplicationState.getInstance().getVisualise()){
                Application.launch(App.class);
            } else {
                long start = System.currentTimeMillis();

                System.out.println("Starting scheduling without visualization...");
                ListScheduleHeuristic list =  ListScheduleHeuristic.getInstance();

                boolean isParallel = ApplicationState.getInstance().getParallelize();

                if (isParallel) {
                    System.out.println("-Using parallelization");
                } else {
                    System.out.println("-Using no parallelization");
                }

                Algorithm branchAndBound = isParallel ? new BranchAndBoundParallel(list.schedule.getDuration(), list.schedule) : new BranchAndBound(list.schedule.getDuration(), list.schedule);
                Schedule opt = branchAndBound.BranchAndBoundRoutine();
                OutputWriter.writeOutputDot(opt);

                long end = System.currentTimeMillis();
                long elapsedTime = end - start;

                System.out.println("Time taken to produce optimal schedule: " + elapsedTime + " ms");
                System.out.println("The optimal schedule is located in: " + ApplicationState.getInstance().getOutputFileName());
            }

        } catch (InvalidInputException e) {
            System.err.println(e.getMessage());
            //print help message
            InputParser.printHelp();
        }
    }
}