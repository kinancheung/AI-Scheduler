package algorithm;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import data.Schedule;
import data.TaskEdge;
import data.TaskNode;

/*
    Author: Benjamin Howard
    Date: 31-07-21

    ListScheduleHeuristic is a heuristic algorithm that produces an initial valid schedule.
    The schedule is used as the initial input for the Branch and Bound algorithm.
    Produces a Valid Schedule.
 */

public class ListScheduleHeuristic {
    private static ListScheduleHeuristic listScheduleHeuristic;
    private List<TaskNode> heuristicPriorityList;
    public Schedule schedule;

    public static ListScheduleHeuristic getInstance() {
        if (listScheduleHeuristic == null) {
            listScheduleHeuristic = new ListScheduleHeuristic();
        }
        return listScheduleHeuristic;
    }

    private ListScheduleHeuristic() {
        heuristicPriorityList = heuristicPriority();
        schedule = new Schedule();

        for (TaskNode node: heuristicPriorityList) {
            schedule.addTask(node);
        }
    }

    public static void deleteInstance() {
        listScheduleHeuristic = null;
    }

    /*
        Method to produce an order tasks from input DAG according to Bottom Level (computation) Weight,
        and respecting topological ordering.
     */
    public List<TaskNode> heuristicPriority() {
        List<TaskNode> scheduleOrdering = new ArrayList<TaskNode>();
        DependencyGraph dependencyGraph = DependencyGraph.getDependencyGraph();

        // Calculate the Bottom Level Weight (blw) for each Node.
        for (TaskNode node: dependencyGraph.nodeMap.values()) {
            node.setBottomLevelWeight(bottomLevelWeightRecursive(node));
            scheduleOrdering.add(node);
        }

        // Order list of TaskNodes by Bottom Level (Computation) Weight.
        scheduleOrdering.sort(Comparator.comparing(TaskNode::getBottomLevelWeight).reversed());

        // Find topological ordering, respecting Bottom Level (Computation) Weight (blw).
        scheduleOrdering = topologicalOrdering(scheduleOrdering);

        return scheduleOrdering;
    }

    /**
     * Recursive function to find Bottom Level (Computation) Weight.
     */
    public int bottomLevelWeightRecursive(TaskNode node) {
        int max_child_blw = 0;

        // If no children just return own computation weight.
        if (node.getOutbound().isEmpty()) {
            return node.getDuration();
        }

        // For each Child Node recursively find its bottom level weight (blw).
        for (TaskEdge edge: node.getOutbound()) {
            TaskNode child_node = edge.getChildNode();
            max_child_blw = Math.max(max_child_blw, bottomLevelWeightRecursive(child_node));
        }

        // Base Bottom Level Weight is Tasks Computation Time.
        return node.getDuration() + max_child_blw;
    }

    /**
     * Create a topological ordering of the tasks
     * @param nodes the tasks to be ordered
     * @return the tasks in topological order
     */
    public List<TaskNode> topologicalOrdering(List<TaskNode> nodes) {
        List<TaskNode> freeNodes = new ArrayList<TaskNode>();
        List<TaskNode> unfreeNodes = nodes;

        while (!unfreeNodes.isEmpty()) {
            Boolean free;

            // If all parents are in free nodes, then can add.
            for (TaskNode node: unfreeNodes) {
                // If node has no inbound edges, then can be added.
                if (node.getInbound().isEmpty()) {
                    freeNodes.add(node);
                    unfreeNodes.remove(node);
                    break;
                }

                // If node has missing inbound edge, then do not add and break.
                free = true;
                for (TaskEdge edge: node.getInbound()) {
                    if (!freeNodes.contains(edge.getParentNode())) {
                        free = false;
                        break;
                    }
                }

                // Add node only if all parent nodes are in free graph.
                if (free) {
                    freeNodes.add(node);
                    unfreeNodes.remove(node);
                    break;
                }
            }
        }
        return freeNodes;
    }
}
