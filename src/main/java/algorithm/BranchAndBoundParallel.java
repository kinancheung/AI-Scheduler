package algorithm;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import data.ApplicationState;
import data.ProgressState;
import data.Schedule;
import data.TaskNode;

/*
    Author: Kinan Cheung
    Date: 14-08-21

    BranchAndBoundParallel is the main routine for the Branch and Bound Schedule finding algorithm using multiple
    processors/threads that accelerates finding the optimal schedule.
    Uses a Depth First Search (DFS) algorithm and search space pruning in order to reduce search time.
    Produces an Optimal, Valid Schedule.

 */

public class BranchAndBoundParallel implements Algorithm{
    
    private static Schedule currentOptimalSchedule;
    private static int bound;
    private static ForkJoinPool pool;
    private static HashSet<String> existingSchedules = new HashSet<String>();

    private static void setCurrentOptimalSchedule(Schedule schedule) {
        currentOptimalSchedule = schedule;
        ApplicationState.getInstance().setOptimalSchedule(schedule);
    }

    public BranchAndBoundParallel(int initialBest, Schedule initialSchedule) {
        setCurrentOptimalSchedule(initialSchedule);
        bound = initialBest;
    }

    /**
     * The main routine for the Branch and Bound Parallel algorithm. A thread pool is created using the
     * Form and Join method which calls the recursive class BranchAndBoundRecursive and invokes the compute method
     * @return optimal schedule
     */
    public Schedule BranchAndBoundRoutine() {
        Schedule s = new Schedule();
        List<Schedule> taskList = new ArrayList<>();
        taskList.add(s);
        pool = new ForkJoinPool(ApplicationState.getInstance().getParallelizeCount());

        BranchAndBoundRecursive recursive = new BranchAndBoundRecursive(taskList);
        ApplicationState.getInstance().setProgressState(ProgressState.RUNNING);
        pool.invoke(recursive);
        ApplicationState.getInstance().setProgressState(ProgressState.FINISHED);
        return currentOptimalSchedule;
    }


    /**
     * Static class that contains the logic for finding valid, optimal schedules recursively.
     */
    static class BranchAndBoundRecursive extends RecursiveAction {
        private List<Schedule> taskList;
        private List<Schedule> foundTasks;
        private BranchAndBoundRecursive recursiveLeft;
        private BranchAndBoundRecursive recursiveRight;

        public BranchAndBoundRecursive(List<Schedule> taskList){
            this.taskList = taskList;
            this.foundTasks = new ArrayList<>();
        }

        @Override
        protected void compute() {
            // Check if schedules given are valid and more optimal than current found schedule
            if(!taskList.isEmpty()) {
                for (Schedule task : taskList) {
                    if (completeSchedule(task) && task.getDuration() < bound) {
                        setCurrentOptimalSchedule(task);

                        bound = task.getDuration();
                    } else {
                        // Else branch out to all possible next step (branches) in tree.
                        List<Schedule> branches = branch(task);

                        for (Schedule child : branches) {
                            // If branch has a lower bound better than B, then add it to stack.
                            if (child.lowerBoundMax() < bound && !existingSchedules.contains(child.getEquivalenceHash())) {
                                foundTasks.add(child);
                                existingSchedules.add(child.getEquivalenceHash());
                            } else {
                                // Delete the child if not needed.
                                child = null;
                            }
                        }
                    }
                }
                recursiveLeft = new BranchAndBoundRecursive(foundTasks.subList(0, foundTasks.size()/2));
                recursiveRight = new BranchAndBoundRecursive(foundTasks.subList(foundTasks.size()/2, foundTasks.size()));
                recursiveLeft.fork();
                recursiveRight.compute();
                recursiveLeft.join();
            }
        }
        /**
         * Create and return all possible Schedules from current schedule.
         * @param s partial schedule
         * @return list of schedules 1 layer deeper
         */
        private List<Schedule> branch(Schedule s) {
            List<Schedule> nextSchedules = new ArrayList<>();
            Collection<TaskNode> freeNodes = s.getFreeNodes();
            int processors = ApplicationState.getInstance().getProcessorAmount();

            // For all free nodes, clone the schedule and add each free task to every processor
            for (TaskNode free: freeNodes) {
                for (int i = 0; i < processors; i++) {
                    Schedule newSchedule = s.clone();
                    newSchedule.addTask(free, i);
                    nextSchedules.add(newSchedule);
                }
            }
            return nextSchedules;
        }

        /**
         * Check if a Schedule is complete and valid.
         * @param s schedule
         * @return boolean
         */
        private Boolean completeSchedule(Schedule s) {
            return s.getFreeNodes().isEmpty();
        }
    }
}
