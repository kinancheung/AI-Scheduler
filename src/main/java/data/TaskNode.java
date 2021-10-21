package data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
    Author: Benjamin Howard, Kinan Cheung
    Date: 08/08/21

    This class contains the fields and information relating to a Task Node
 */

public class TaskNode {
    private String taskName;
    private int duration;
    private int bottomLevelWeight;
    private List<TaskEdge> inbound;
    private List<TaskEdge> outbound;
    private boolean hasUniqueName;
    private String uniqueName;

    public TaskNode(String name, int duration) {
        taskName = name;
        this.duration = duration;
        this.inbound = new ArrayList<TaskEdge>();
        this.outbound = new ArrayList<TaskEdge>();
    }

    /**
     * @return node name
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * @return duration of a node
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @return bottom level weight
     */
    public int getBottomLevelWeight() {
        return bottomLevelWeight;
    }

    /**
     * @param bottomLevelWeight
     */
    public void setBottomLevelWeight(int bottomLevelWeight) {
        this.bottomLevelWeight = bottomLevelWeight;
    }

    /**
     * @return a list of the inbound nodes
     */
    public List<TaskEdge> getInbound() {
        return inbound;
    }

    /**
     * @return a list of outbound nodes
     */
    public List<TaskEdge> getOutbound() {
        return outbound;
    }

    /**
     * Set inbound nodes
     * @param inbound
     */
    public void setInbound(List<TaskEdge> inbound) {
        this.inbound = inbound;
    }

    /**
     * set outbound notes
     * @param outbound
     */
    public void setOutbound(List<TaskEdge> outbound) {
        this.outbound = outbound;
    }

    /**
     * set uniqueness of the task name
     * @param uName
     */
    public void setUniqueName(String uName) {
        uniqueName = uName;
        hasUniqueName = true;
    }

    /**
     * @return the uniqueness of the task name
     */
    public boolean hasUniqueName() {
        return hasUniqueName;
    }

    /**
     * Get the unique name of the task node
     * @return
     */
    public String getUniqueName() {
        return uniqueName;
    }

    /**
     * @return the name of the task
     */
    public String toString() {
        return getTaskName();
    }

    /**
     * Get the parents of this task. These task nodes must be scheduled before this task node.
     * @return the parents
     */
    public List<TaskNode> getParents() {
        return getInbound()
            .stream()
            .map(edge -> edge.getParentNode())
            .collect(Collectors.toList());
    }
}
