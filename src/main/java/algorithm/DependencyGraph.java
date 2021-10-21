package algorithm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import data.ApplicationState;
import data.TaskEdge;
import data.TaskNode;

/*
    Author: Peter Goedeke, Kinan Cheung
    Date: 08/08/21

    This class manages reading the .dot files and creates a dependency graph for class Schedule to call upon.
    It contains a list of all nodes in the input file
 */
public class DependencyGraph {
    // public String filePath = "./test.dot";
    public HashMap<String, TaskNode> nodeMap;

    private static DependencyGraph dg;
    private String graphName;

    private DependencyGraph() {
        nodeMap = new HashMap<>();
    }

    public static DependencyGraph getDependencyGraph() {
        if (dg == null) {
            dg = new DependencyGraph();
        }
        return dg;
    }

    public List<TaskNode> getNodes() {
        return new ArrayList<TaskNode>(nodeMap.values());
    }

    public String getGraphName() {
        return graphName;
    }

    /**
     * This method reads the input .dot file and inputs all Task Nodes into a HashMap with the name of the
     * task and information relating to the node.
     */
    public void readFile() {
        nodeMap = new HashMap<>();
        String filePath = ApplicationState.getInstance().getInputFile();

        try {
            // String line = br.readLine();
            // graphName = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
            String[] fileLines = Files.lines(Paths.get(filePath)).toArray(String[]::new);
            String fileString = String.join("\n", fileLines);
            String[] lines = fileString.split("\\{|;");

            String firstLine = lines[0];
            graphName = firstLine.substring(firstLine.indexOf("\"") + 1, firstLine.lastIndexOf("\""));
            for (int i = 1; i < lines.length - 1; i++) {
                String line = lines[i];
                if (line.contains("->")) {
                    String parentTaskName = line.substring(0, line.indexOf("->")).trim();
                    String childTaskName = line.substring(line.indexOf("->") + 2, line.indexOf("[")).trim();
                    String weightString = line.substring(line.indexOf("=") + 1, line.indexOf("]"));
                    int weight = Integer.parseInt(weightString.trim());

                    TaskNode parent = nodeMap.get(parentTaskName);
                    TaskNode child = nodeMap.get(childTaskName);
                    TaskEdge edge = new TaskEdge(parent, child, weight);

                    List<TaskEdge> outbound = nodeMap.get(parentTaskName).getOutbound();

                    outbound.add(edge);
                    nodeMap.get(parentTaskName).setOutbound(outbound);
                    List<TaskEdge> inbound = nodeMap.get(childTaskName).getInbound();
                    inbound.add(edge);
                    nodeMap.get(childTaskName).setInbound(inbound);
                } else if (line.contains("Weight=")) {
                    String taskName = line.substring(0, line.indexOf("[")).trim();
                    String weightString = line.substring(line.indexOf("Weight=") + 7, line.indexOf("]"));
                    int weight = Integer.parseInt(weightString.trim());
                    nodeMap.put(taskName, new TaskNode(taskName, weight));
                }
            }

            for (TaskNode nodeA : nodeMap.values()) {
                if (nodeA.hasUniqueName()) continue;
                nodeA.setUniqueName(nodeA.getTaskName());
                for (TaskNode nodeB : nodeMap.values()) {
                    if (nodeB.hasUniqueName()) continue;

                    boolean sameDuration = nodeA.getDuration() == nodeB.getDuration();
                    boolean parentLength = nodeA.getParents().size() == nodeB.getParents().size();
                    boolean parentsSame = nodeA.getInbound()
                        .stream()
                        .allMatch(edgeA -> nodeB.getInbound()
                            .stream()
                            .anyMatch(edgeB -> edgeA.getDelay() == edgeB.getDelay() && edgeA.getParentNode().getTaskName().equals(edgeB.getParentNode().getTaskName())));
                            
                    boolean childLength = nodeA.getOutbound().size() == nodeB.getOutbound().size();
                    boolean childSame = nodeA.getOutbound()
                        .stream()
                        .allMatch(edgeA -> nodeB.getOutbound()
                            .stream()
                            .anyMatch(edgeB -> edgeA.getDelay() == edgeB.getDelay() && edgeA.getChildNode() == edgeB.getChildNode()));

                    if (sameDuration && parentLength && parentsSame && childLength && childSame) {
                        nodeB.setUniqueName(nodeA.getTaskName());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
