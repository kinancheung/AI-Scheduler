package visualisation.graph;

import algorithm.DependencyGraph;
import data.*;
import javafx.scene.paint.Color;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import visualisation.util.ColourGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/** 
 * Author: Kirsty
 * Date: 22/08/08
 *
 * The InputGraph class generates and controls the visualisation of the input graph.
 */
public class InputGraph {

    private List<String> edgeMap= new ArrayList<>();
    private List<String> nodeList= new ArrayList<>();

    private Graph graph;
    private int edgeWidth = 2;
    private int nodeSize = 25;

    private String style=  "graph {" +
            "fill-color: #312f31;}" +
            "node {" +
            "shape: circle;" +
            "stroke-mode: plain;" +
            "fill-color: #312f31;" +
            "stroke-width: 2.5px;" +
            "size: 25px;" +
            "text-size: 12px;" +
            "text-color: white;" +
            "text-alignment: center;" +
            "}" +
            "edge {" +
            "fill-color: #C4C4C4;" +
            "text-color: white;" +
            "text-size: 12px;" +
            "}" ;


    /**
     * Call to create the input graph and handles zoom in and out action.
     */
    public InputGraph(){
        createGraph();
        ApplicationState.getInstance().setZoomGraphObserver(edgeWidth);
        ApplicationState.getInstance().addZoomGraphObserver(e ->{
            zoom();
        });
    }

    /**
     * Creates the graph by using the Tasknode data from the DependencyGraph
     */
    public void createGraph(){

        HashMap<String, TaskNode> dependencyGraph = DependencyGraph.getDependencyGraph().nodeMap;

        Graph graph = new SingleGraph("Tutorial 1");
        graph.setAttribute("ui.stylesheet", style);
        dependencyGraph.forEach((k,v)->{
            String parentName = v.getTaskName();

            if(graph.getNode(parentName) == null){
                Node parentNode=graph.addNode(parentName);
                nodeList.add(parentName);
                parentNode.setAttribute("ui.label",parentName);
                parentNode.setAttribute("layout.weight", 10);
            }

            List<TaskEdge> edges = v.getInbound();

            for (TaskEdge edge : edges) {
                String childName = edge.getParentNode().getTaskName();
                if(graph.getNode(childName)==null){
                    Node childNode=graph.addNode(childName);
                    childNode.setAttribute("ui.label",edge.getParentNode().getDuration());
                    nodeList.add(childName);
                    childNode.setAttribute("layout.weight", 10);
                }
                Edge nodeEdge=graph.addEdge(parentName+childName,childName,parentName,true);
                nodeEdge.setAttribute("ui.stylesheet", style);
                nodeEdge.setAttribute("ui.label", edge.getDelay());
                nodeEdge.setAttribute("layout.weight", edgeWidth);
                edgeMap.add(parentName+childName);
            }
        });
        this.graph = graph;
    }

    /**
     * Changes the node color when the method is call
     * @param numberOfProcessor
     * @param schedule
     * @return changed input graph
     */
    public Graph changeColor(int numberOfProcessor, Schedule schedule){
        ColourGenerator colorGen = new ColourGenerator(numberOfProcessor);
        Map<String,Integer> data = generateData(schedule);

        data.forEach((k,v) -> {
            Color color = colorGen.getColor(v);
            String hexColor= "#" + Integer.toHexString(color.hashCode()).substring(0, 6).toUpperCase();
            graph.getNode(k).setAttribute("ui.style", "stroke-color: "+hexColor + ";");
        } );

        return graph;
    }

    /**
     * Changes the node size and edge width of the graph
     */
    public void zoom(){
        edgeMap.forEach(e -> {
            graph.getEdge(e).setAttribute("layout.weight", edgeWidth);
        });
        nodeList.forEach( e -> {
            graph.getNode(e).setAttribute("ui.style", "size:"+ nodeSize + "px;");
        });

    }

    /**
     * Get the generated graph
     * @return
     */
    public Graph getGraph(){
        return graph;
    }

    /**
     * process the scheduled node information into a hashmap for color change
     * @param schedule
     * @return a map of nodes name and processors it assigned to.
     */
    public Map<String,Integer> generateData(Schedule schedule){
        Map<String, Integer> map = new HashMap<>();

        Map<String, ScheduledTaskNode> scheduledTaskNodeMap = schedule.getScheduledNodes();
        scheduledTaskNodeMap.forEach((k,v) -> {
            map.put(k,v.getProcessor());
        });

        return map;

    }

    /**
     * Get edge width of the current graph
     * @return edge width
     */
    public int getEdgeWidth(){return edgeWidth;}

    /**
     * Set new edge width and triggers a update to the GUI
     * @param size
     */
    public void setEdgeWidth(int size){
        if( size <= 10 && size >= 2){
            edgeWidth = size;
            ApplicationState.getInstance().setZoomGraphObserver(edgeWidth);
        }
    }

    /**
     * Get node size
     * @return node size
     */
    public int getNodeSize(){return nodeSize;}

    /**
     * Set new node size
     * @param size
     */
    public void setNodeSize(int size){
        nodeSize = size;
    }
}
