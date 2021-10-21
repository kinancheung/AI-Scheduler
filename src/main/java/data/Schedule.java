package data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import algorithm.DependencyGraph;

/*
    Author: Peter Goedeke, Kinan Cheung
    Date: 08/08/21

    This class contains all relevant information to managing a schedule
 */
public class Schedule {
    // the time at which the last scheduled task on each processor ends
    // i.e. the time when the processor becomes free
    private ArrayList<Integer> processorFreeTimes;

    // a map of task names to scheduled nodes
    private HashMap<String, ScheduledTaskNode> scheduledNodes;

    private HashMap<String, TaskNode> freeNodes;

    public Schedule() {
        this.processorFreeTimes = new ArrayList<>();
        int processors = ApplicationState.getInstance().getProcessorAmount();
        for (int i = 0; i < processors; i++) {
            processorFreeTimes.add(0);
        }
        this.scheduledNodes = new HashMap<>();
        this.freeNodes = new HashMap<>();
        DependencyGraph.getDependencyGraph().nodeMap.values().stream().filter(node -> node.getInbound().size() == 0)
                .forEach(node -> this.freeNodes.put(node.getTaskName(), node));
    }

    private Schedule(ArrayList<Integer> processorFreeTimes, HashMap<String, ScheduledTaskNode> scheduledNodes,
            HashMap<String, TaskNode> freeNodes) {
        this.processorFreeTimes = processorFreeTimes;
        this.scheduledNodes = scheduledNodes;
        this.freeNodes = freeNodes;
    }

    /**
     * Convert the schedule to a dot file string.
     * 
     * @return the dot file string
     */
    public String toDotFile() {
        StringBuilder sb = new StringBuilder();
        String outputGraphName = "output" + DependencyGraph.getDependencyGraph().getGraphName();
        sb.append("digraph \"" + outputGraphName + "\" {\n");

        // add the nodes first with their weights, start times, and processors
        // this guarantees that nodes will be in the file when they are referenced by an
        // edge
        for (ScheduledTaskNode snode : scheduledNodes.values()) {
            String taskName = snode.getTask().getTaskName();
            int duration = snode.getTask().getDuration();
            int startTime = snode.getStartTime();
            int processor = snode.getProcessor();
            sb.append("\t" + taskName + " [Weight=" + duration + ",Start=" + startTime + ",Processor=" + processor
                    + "];\n");
        }
        // add the edges
        for (ScheduledTaskNode snode : scheduledNodes.values()) {
            for (TaskEdge edge : snode.getTask().getInbound()) {
                String parentName = edge.getParentNode().getTaskName();
                String childName = edge.getChildNode().getTaskName();
                sb.append("\t" + parentName + " -> " + childName + " [Weight=" + edge.getDelay() + "];\n");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Clone the schedule. This clone clones mutable parts of the schedule deeply
     * and immutable parts in a shallow fashion.
     * 
     * @return clone of schedule
     */
    @Override
    public Schedule clone() {
        ArrayList<Integer> newFreeTimes = (ArrayList<Integer>) processorFreeTimes.clone();
        HashMap<String, ScheduledTaskNode> newScheduledNodes = (HashMap<String, ScheduledTaskNode>) scheduledNodes
                .clone();
        HashMap<String, TaskNode> newFreeNodes = (HashMap<String, TaskNode>) freeNodes.clone();
        return new Schedule(newFreeTimes, newScheduledNodes, newFreeNodes);
    }

    /**
     * Get a string representation of the schedule which will be identical for
     * equivalent schedules, where equivalent schedules are those which have the
     * same start times for their tasks (i.e. the schedule is the same, even though
     * they might have their tasks on different processors).
     * 
     * This is used to prune the majority of schedules, saving time
     * 
     * @return the string representation
     */
    public String getEquivalenceHash() {
        String[] names = scheduledNodes.keySet().toArray(new String[scheduledNodes.size()]);
        Arrays.sort(names);
        StringBuilder sb = new StringBuilder();
        for (String name : names) {
            // sb.append(name + scheduledNodes.get(name).getStartTime());
            sb.append(scheduledNodes.get(name).getTask().getUniqueName() + scheduledNodes.get(name).getStartTime());
        }
        return sb.toString();
    }

    public TaskNode fixedOrder() {
        ArrayList<TaskNode> free = new ArrayList<>(getFreeNodes());
        int parentProcessor = -1;
        String taskName = "";

        for (TaskNode node : free) {
            if (node.getInbound().size() > 1 || node.getOutbound().size() > 1) {
                return null;
            }
            if (!taskName.equals("") && node.getOutbound().size() == 1
                    && !node.getOutbound().get(0).getChildNode().getTaskName().equals(taskName)) {
                return null;
            }
            if (node.getOutbound().size() == 1) {
                taskName = node.getOutbound().get(0).getChildNode().getTaskName();
            }

            if (node.getParents().size() == 1) {
                TaskNode parent = node.getParents().get(0);
                int processor = scheduledNodes.get(parent.getTaskName()).getProcessor();
                if (parentProcessor == -1) {
                    parentProcessor = processor;
                }
                if (parentProcessor != processor) {
                    return null;
                }
            }
        }

        Collections.sort(free, (a, b) -> {
            int aTime = 0;

            if (a.getParents().size() == 1) {
                ScheduledTaskNode snode = scheduledNodes.get(a.getParents().get(0).getTaskName());
                aTime = snode.getEndTime() + a.getInbound().get(0).getDelay();
                // aTime = snode.getEndTime() + snode.getTask().getOutbound().get(0).getDelay();
            }

            int bTime = 0;
            if (b.getParents().size() == 1) {
                ScheduledTaskNode snode = scheduledNodes.get(b.getParents().get(0).getTaskName());
                // bTime = snode.getEndTime() + snode.getTask().getOutbound().get(0).getDelay();
                bTime = snode.getEndTime() + b.getInbound().get(0).getDelay();
            }

            int val = aTime - bTime;
            if (val == 0) {
                int aEdge = a.getOutbound().size() == 0 ? 0 : a.getOutbound().get(0).getDelay();
                int bEdge = b.getOutbound().size() == 0 ? 0 : b.getOutbound().get(0).getDelay();
                val = bEdge - aEdge;
            }
            return val;
        });
        for (int i = 1; i < free.size(); i++) {
            int cost = free.get(i).getOutbound().size() == 0
                ? 0
                : free.get(i).getOutbound().get(0).getDelay();

            int previousCost = free.get(i - 1).getOutbound().size() == 0
                ? 0
                : free.get(i - 1).getOutbound().get(0).getDelay();
            
            if (cost > previousCost) {
                System.out.println(free.get(i).getTaskName() + free.get(i - 1).getTaskName());
                return null;
            }
        }
        return free.size() == 0 ? null : free.get(0);
    }

    /**
     * Get the duration of schedule (the finish time of the latest finishing task)
     * 
     * @return the duration
     */
    public int getDuration() {
        int max = 0;
        for (int i = 0; i < processorFreeTimes.size(); i++) {
            max = Math.max(max, processorFreeTimes.get(i));
        }
        return max;
    }

    /**
     * Get a list of nodes which are free to be scheduled with respect to the
     * current schedule.
     * 
     * @param dg the dependency graph containing all nodes
     * @return the list of free nodes
     */
    public Collection<TaskNode> getFreeNodes() {
        return freeNodes.values();
    }

    /**
     * Get the earliest possible start time for scheduling a node onto a processor.
     * 
     * Earliest possible start time is given by max(d,e) where: d represents the
     * latest ending communication delay the task will have to wait for before it
     * can begin (i.e. the dependency which will communicate required information to
     * the task last). This involves finding the latest dependency delay which is
     * not on the given processor, as tasks on the same processor don't have to be
     * waited for.
     * 
     * e represents the end time of the latest finishing scheduled task on the given
     * processor. This includes the dependencies
     * 
     * @param node      the node to be scheduled
     * @param processor the processor on which the node is to be scheduled
     * @return the earliest possible start time
     */
    private int getStartTimeForNodeForProcessor(TaskNode node, int processor) {
        int processorFreeTime = processorFreeTimes.get(processor);
        // thisDelay is used to decide whether longestDelay or secondLongestDelay is
        // relevant
        int longestDelay = 0;

        // find the longest and secondLongest delays and the delay on this processor
        for (TaskEdge edge : node.getInbound()) {
            ScheduledTaskNode snode = scheduledNodes.get(edge.getParentNode().getTaskName());
            // we don't have to wait for the communication delay of a process on this
            // processor
            if (snode.getProcessor() == processor) {
                continue;
            }
            int delay = snode.getEndTime() + edge.getDelay();
            if (delay >= longestDelay) {
                longestDelay = delay;
            }
        }
        return Math.max(processorFreeTime, longestDelay);
    }

    /**
     * Schedule a node on a given processor at a given time. This is used after
     * finding the earliest possible start time to update the schedule to reflect
     * the new desired schedule.
     * 
     * @param node      the node to schedule
     * @param processor the processor to schedule the node on
     * @param startTime the start time to schedule the node for
     */
    private void scheduleNode(TaskNode node, int processor, int startTime) {
        ScheduledTaskNode snode = new ScheduledTaskNode(node, processor, startTime);
        scheduledNodes.put(node.getTaskName(), snode);
        processorFreeTimes.set(processor, snode.getEndTime());

        freeNodes.remove(node.getTaskName());
        node.getOutbound().stream().map(edge -> edge.getChildNode())
                .filter(child -> child.getParents().stream()
                        .allMatch(parent -> scheduledNodes.containsKey(parent.getTaskName())))
                .forEach(child -> freeNodes.put(child.getTaskName(), child));
    }

    /**
     * Add a task at the earliest possible time on a specific processor.
     * 
     * @param node      the node to schedule
     * @param processor the processor to schedule the node on
     */
    public void addTask(TaskNode node, int processor) {
        int startTime = getStartTimeForNodeForProcessor(node, processor);
        scheduleNode(node, processor, startTime);
    }

    /**
     * Add a task on the processor which allows for the earliest possible time.
     * 
     * @param node the node to schedule
     */
    public void addTask(TaskNode node) {
        int bestStartTime = getStartTimeForNodeForProcessor(node, 0);
        int processor = 0;
        for (int i = 1; i < processorFreeTimes.size(); i++) {
            int startTime = getStartTimeForNodeForProcessor(node, i);
            if (startTime < bestStartTime) {
                bestStartTime = startTime;
                processor = i;
            }
        }
        scheduleNode(node, processor, bestStartTime);
    }

    /**
     * Find the lower bound of a Partial Schedule Max (start time of scheduled tasks
     * plus their bottom level)
     * 
     * @return max lower bound of the partial schedule
     */
    public int lowerBoundMax() {
        int max = 0;

        for (Map.Entry<String, ScheduledTaskNode> entry : scheduledNodes.entrySet()) {
            if (entry.getValue().getStartTime() + entry.getValue().getTask().getBottomLevelWeight() > max) {
                max = entry.getValue().getStartTime() + entry.getValue().getTask().getBottomLevelWeight();
            }
        }
        return max;
    }

    /**
     * Get scheduled nodes
     * @return hashmap which contains the scheduled nodes as value and name as key
     */
    public HashMap<String, ScheduledTaskNode> getScheduledNodes() {
        return scheduledNodes;
    }
}