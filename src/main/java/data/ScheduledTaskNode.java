package data;

/*
    Author: Ben Howard
    Date: 05/08/2021

    This class contains all information relevant to a Task Node which has been added to a schedule
 */

public class ScheduledTaskNode {
    private TaskNode task;
    private int processor;
    private int startTime;

    public ScheduledTaskNode(TaskNode task, int processor, int startTime) {
        this.task = task;
        this.processor = processor;
        this.startTime = startTime;
    }

    /**
     * Get Task node
     * @return Task node as type Tasknode
     */
    public TaskNode getTask() {
        return task;
    }

    /**
     * Get node start time
     * @return start time as an integer
     */
    public int getStartTime() {
        return startTime;
    }

    /**
     * Get node end time
     * @return end time as integer
     */
    public int getEndTime() {
        return startTime + task.getDuration();
    }

    /**
     * Get processor which the node brlongs to
     * @return processor number
     */
    public int getProcessor() {
        return processor;
    }
}
