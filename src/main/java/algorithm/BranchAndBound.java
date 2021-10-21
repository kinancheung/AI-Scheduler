package algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import data.ApplicationState;
import data.ProgressState;
import data.Schedule;
import data.TaskNode;

/*
    Author: Benjamin Howard, Kinan Cheung
    Date: 08-08-21

    BranchAndBound is the main routine for the Branch and Bound Schedule finding algorithm.
    Uses a Depth First Search (DFS) algorithm and search space pruning in order to reduce search time.
    Produces an Optimal, Valid Schedule.
 */

public class BranchAndBound implements Algorithm{
    private Stack<Schedule> scheduleStack;
    private Schedule currentOptimalSchedule;
    private int bound;
    private ApplicationState state;


    private void setCurrentOptimalSchedule(Schedule schedule) {
        currentOptimalSchedule = schedule;
        ApplicationState.getInstance().setOptimalSchedule(schedule);
    }

    public BranchAndBound(int initialBest, Schedule initialSchedule) {
        scheduleStack = new Stack<Schedule>();
        setCurrentOptimalSchedule(initialSchedule);
        bound = initialBest;
        state = ApplicationState.getInstance();
    }

    /**
     * The main routine for the Branch and Bound algorithm, uses DFS to create
     * Schedules and uses pruning to remove unnecessary computation (search space of
     * the tree).
     * @retyrn optimal schedule
     */
    public Schedule BranchAndBoundRoutine() {
        state.setProgressState(ProgressState.RUNNING);
        // Push an empty schedule to the stack
        Schedule s = new Schedule();
        HashSet<String> existingSchedules = new HashSet<String>();
        scheduleStack.push(s);

        // Iterate until no schedules left (DFS)
        while (!scheduleStack.empty()) {
            // See if new thread, chuck on another thread if its free otherwise do it on current.
            s = scheduleStack.pop();
            // Check if a better solution has been found
            if (completeSchedule(s) && s.getDuration() < bound) {
                setCurrentOptimalSchedule(s);
                bound = s.getDuration();
            } else {
                // Else branch out to all possible next step (branches) in tree.
                List<Schedule> branches = branch(s);

                for (Schedule child : branches) {
                    // If branch has a lower bound better than B, then add it to stack.
                    if (child.lowerBoundMax() < bound && !existingSchedules.contains(child.getEquivalenceHash())) {
                        existingSchedules.add(child.getEquivalenceHash());
                        scheduleStack.push(child);
                    }
                }
            }
        }
        state.setProgressState(ProgressState.FINISHED);
        return currentOptimalSchedule;
    }

    /*
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

    /*
     * Check if a Schedule is complete and valid.
     * @param s schedule
     * @return boolean
     */
    private Boolean completeSchedule(Schedule s) {
        if (s.getFreeNodes().isEmpty()) {
            return true;
        }
        return false;
    }
}
