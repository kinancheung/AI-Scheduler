package data;

/*
    Author: Benjamin Howard
    Date: 05/08/2021

    This class contains all relevant information relating to an edge between 2 Task Nodes
 */

public class TaskEdge {
    private TaskNode parentNode;
    private TaskNode childNode;
    private int delay;

    public TaskEdge(TaskNode parent, TaskNode child, int delay) {
        parentNode = parent;
        childNode = child;
        this.delay = delay;
    }

    /**
     * @return the parent node or null
     */
    public TaskNode getParentNode() {
        return parentNode;
    }

    /**
     * @return the child node or null
     */
    public TaskNode getChildNode() {
        return childNode;
    }

    /**
     * @return the time it require to switch processor
     */
    public int getDelay() {
        return delay;
    }
}
