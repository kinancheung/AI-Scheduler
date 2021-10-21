package visualisation.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import data.ApplicationState;
import data.Schedule;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import visualisation.util.ChartUtils;
import visualisation.util.ColourGenerator;
import visualisation.util.Loader;

/**
 * Author: Alex Nicholson
 * Date: 20/08/2021
 *
 * Renders a gantt chart representation of the optimal schedule
 */
public class Chart {

    @FXML
    private GridPane chartContainer;
    
    @FXML 
    private HBox taskInfo;

    @FXML
    private Text taskInfoStart;

    @FXML
    private Text taskInfoEnd;

    @FXML
    private Text taskInfoName;

    private List<ChartRow> rows;

    private Node nodes;

    private GridPane backgroundContainer;

    private GridPane axisContainer;

    private int majorLines = 5;

    private int minorLines = 5;

    private Schedule schedule;

    @FXML
    void initialize() {
        rows = new ArrayList<>();
    }

    /**
     * Sets up the rows of the chart and adds observers to the application state
     */
    private void config(Node nodes) {
        this.nodes = nodes;

        int numProcessors = ApplicationState.getInstance().getProcessorAmount();

        // generate colors
        ColourGenerator gen = new ColourGenerator(numProcessors);

        backgroundContainer = new GridPane();
        backgroundContainer.setStyle("-fx-border-width: 0px 0px 1px 1px; -fx-border-color: white ");
        chartContainer.add(backgroundContainer, 2, 0, 1, numProcessors);
        GridPane.setVgrow(backgroundContainer, Priority.ALWAYS);

        // Add background lines
        for (int i = 0; i < this.majorLines * this.minorLines; i++) {
            Pane divider = new Pane();
            GridPane.setVgrow(divider, Priority.ALWAYS);
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            cc.setHalignment(HPos.RIGHT);
            backgroundContainer.getColumnConstraints().add(cc);
            if(i % this.majorLines == this.majorLines - 1 ) {
                divider.setStyle("-fx-background-color: #fff3");
            } else {
                divider.setStyle("-fx-background-color: #eee1");
            }
            divider.setMaxWidth(1.0);
            backgroundContainer.add(divider, i, 0, 1, 1);    
        }        

        
        ApplicationState.getInstance().addTaskSelectedObserver(task -> {
            if(task != null){
                taskInfoStart.setText(String.valueOf(task.getStartTime()));
                taskInfoEnd.setText(String.valueOf(task.getEndTime()));
                taskInfoName.setText("Task " + task.getTask().getTaskName() + ":");
                taskInfo.setVisible(true);
            } else {
                taskInfo.setVisible(false);
            }
        });

        // iterate over num processors
        for (int i = 0; i < numProcessors; i++) {
            ChartRow row = ChartRow.create(i, gen.getColor(i));
            rows.add(row);
            Node rowNodes = row.render();
            Text text = new Text("Processor " + (i + 1));
            text.setFill(Color.WHITE);
            GridPane.setMargin(text, new Insets(0, 4, 0, 0));
            chartContainer.add(text, 0, i, 1, 1);
            chartContainer.add(rowNodes, 2, i, 1, 1);
            
            RowConstraints rc = new RowConstraints();
            rc.setVgrow(Priority.ALWAYS);
            chartContainer.getRowConstraints().add(rc);

            GridPane.setHgrow(rowNodes, Priority.ALWAYS);
            GridPane.setHgrow(text, Priority.NEVER);
        }

        axisContainer = new GridPane();
        GridPane.setHgrow(axisContainer, Priority.ALWAYS);
        chartContainer.add(axisContainer, 2, numProcessors);

        ApplicationState.getInstance().addOptimalScheduleObserver(schedule -> {
            Platform.runLater(() -> {
                drawChart(schedule);
            });
        });
    }

    /**
     * @return the node tree containing the chart, so that it can be added to the scene
     */
    public Node render() {
        return this.nodes;
    }

    /**
     * Creates a new chart instance
     */
    public static Chart create() {

        FXMLLoader loader = Loader.manualLoad(Views.CHART.getPath());
        try {
            Node content = (Node) loader.load();
            Chart controller = loader.getController();

            controller.config(content);

            return controller;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Renders the actual content of the chart. This includes the rows,
     * as well as the axis, to ensure it is up to date with the latest 
     * optimal schedule
     */
    public void renderChart() {
        Schedule s = this.schedule;

        if (s == null) return;

         // work out scale
         int maxTime = ChartUtils.getNearestWidth(s.getDuration());
         double scale = backgroundContainer.getWidth() / maxTime;       
 
         //update graph numbers to match scale
         axisContainer.getChildren().clear();
         axisContainer.getColumnConstraints().clear();
 
         for(int i = 0; i < this.majorLines; i++){
             StackPane textContainer = new StackPane();
             double amount = ((double)i + 1) / this.majorLines;
             String x = String.valueOf((int)(amount * maxTime));
             Text text = new Text(x);
             text.setFill(Color.WHITE);
 
             ColumnConstraints cc = new ColumnConstraints();
             cc.setPercentWidth(25);
             cc.setHalignment(HPos.RIGHT);
             axisContainer.getColumnConstraints().add(cc);
 
             textContainer.getChildren().add(text);
             textContainer.setAlignment(Pos.CENTER_RIGHT);
 
             axisContainer.add(textContainer, i, 0, 1, 1);
         }
 
         //render data
         rows.forEach(r -> {
             r.updateRow(s, scale);
         });
    }

    private void drawChart(Schedule s) {
        schedule = s;
        renderChart();
    }


}
