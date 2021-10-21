package visualisation.controller;

import algorithm.DependencyGraph;
import data.ApplicationState;
import data.Schedule;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;

import org.graphstream.graph.Graph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.javafx.FxGraphRenderer;
import visualisation.graph.InputGraph;
import visualisation.util.ColourGenerator;
import visualisation.util.Loader;

public class InputGraphController {

    @FXML
    private GridPane inputGraphGrid;

    @FXML
    private GridPane graphDisplayGrid;

    @FXML
    private Label inputGraphLabel;

    @FXML
    private GridPane processorGrid;

    private InputGraph inputGraph;

    private DependencyGraph dependencyGraph;

    @FXML
    private Button zoomInBtn;

    @FXML
    private Button zoomOutBtn;

    private Node nodes;

    @FXML
    void initialize() {
        dependencyGraph = DependencyGraph.getDependencyGraph();
        inputGraph = new InputGraph();
        addProcessorLabel(ApplicationState.getInstance().getProcessorAmount());
        addGraph();

        zoomOutBtn.setDisable(true);

        ApplicationState.getInstance().addZoomGraphObserver(edgeWidth -> {
            setZoomBtnDisable(edgeWidth);
        });

        ApplicationState.getInstance().addOptimalScheduleObserver(schedule -> {
            changeColor(schedule);
        });

    }

    /**
     * Add processor GUI on to the visualisation
     * @param numberOfProcessor
     */
    public void addProcessorLabel(int numberOfProcessor) {

        int textCol = 1;
        int dotCol = 0;
        ColourGenerator colorGen = new ColourGenerator(numberOfProcessor);

        for (int j = 0; j < numberOfProcessor; j++) {
            int row = j;

            if (j > 3) {
                textCol = 3;
                row = row - 4;
                dotCol = 2;
            }

            processorGrid.add(new Label("Processor " + (j + 1)), textCol, row);

            Circle circle = new Circle(10.0f, 10.0f, 8.0f);
            Color color = colorGen.getColor(j);
            circle.setFill(color);
            processorGrid.add(circle, dotCol, row);
        }
    }

    /**
     * Add the visualisation of the input graph onto the visualisation
     */
    public void addGraph() {
        Graph graph = inputGraph.getGraph();

        FxViewer view = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        view.enableAutoLayout();
        FxViewPanel panel = (FxViewPanel) view.addDefaultView(false, new FxGraphRenderer());

        graphDisplayGrid.add(panel, 0, 0);
        inputGraphLabel.setText(dependencyGraph.getGraphName());
        inputGraphGrid.toFront();
    }

    /**
     * Changes input graph node colors
     * @param schedule
     */
    public void changeColor(Schedule schedule) {
        Graph graph = inputGraph.changeColor(ApplicationState.getInstance().getProcessorAmount(), schedule);

        FxViewer view = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        view.enableAutoLayout();
        FxViewPanel panel = (FxViewPanel) view.addDefaultView(false, new FxGraphRenderer());
        graphDisplayGrid.getChildren().remove(panel);
        graphDisplayGrid.add(panel, 0, 0);
    }

    /**
     * Input graph zoom in
     * @param event
     */
    @FXML
    void zoomInAction(ActionEvent event) {
        inputGraph.setEdgeWidth(inputGraph.getEdgeWidth() + 2);
        inputGraph.setNodeSize(inputGraph.getNodeSize() - 1);
    }

    /**
     * Input graph zoom out
     * @param event
     */
    @FXML
    void zoomOutAction(ActionEvent event) {
        inputGraph.setEdgeWidth(inputGraph.getEdgeWidth() - 2);
        inputGraph.setNodeSize(inputGraph.getNodeSize() + 1);
    }

    /**
     * Disable zoom button when the edge width is smaller than 2 or greater than 10
     * @param edgeWidth
     */
    public void setZoomBtnDisable(int edgeWidth) {

        if (edgeWidth >= 10) {
            zoomInBtn.setDisable(true);
        } else {
            zoomInBtn.setDisable(false);
        }

        if (edgeWidth <= 2) {
            zoomOutBtn.setDisable(true);
        } else {
            zoomOutBtn.setDisable(false);
        }

    }

    /**
     * Constructor for the input graph panel
     * @return
     */
    public static InputGraphController create() {

        FXMLLoader loader = Loader.manualLoad(Views.GRAPH.getPath());
        try {
            Node content = (Node) loader.load();
            InputGraphController controller = loader.getController();

            controller.config(content);

            return controller;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * setup the nodes for the input graph
     * @param nodes
     */
    private void config(Node nodes) {
        this.nodes = nodes;
    }

    /** get the node of the input graph
     *
     * @return node
     */
    public Node render() {
        return this.nodes;
    }

}
