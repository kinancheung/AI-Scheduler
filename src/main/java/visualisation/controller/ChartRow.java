package visualisation.controller;

import java.io.IOException;

import data.ApplicationState;
import data.Schedule;
import data.ScheduledTaskNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import visualisation.util.Loader;

/*
    Author: Alex Nicholson
    Date: 22/08/21

    ChartRow controls a row of the gantt chart
 */
public class ChartRow {

    @FXML
    private StackPane data;

    private Node nodes;

    private int processorNum;

    private Color color;


    @FXML
    void initialize() {
    }

    private void config(Node nodes, int number, Color color) {
        this.nodes = nodes;
        this.processorNum = number;
        this.color = color;

        VBox.setVgrow(nodes, Priority.ALWAYS);
    }

    /**
     * return the rendered row
     */
    public Node render() {
        return this.nodes;
    }

    public static ChartRow create(int number, Color color) {
        FXMLLoader loader = Loader.manualLoad(Views.CHART_ROW.getPath());
        try {
            Node content = (Node) loader.load();
            ChartRow controller = loader.getController();

            controller.config(content, number, color);

            return controller;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Update the GUI of the row
     * @param s
     * @param scale
     */
    public void updateRow(Schedule s, double scale) {
        data.getChildren().clear();
        // iterate over scheduled tasks
        for (ScheduledTaskNode snode : s.getScheduledNodes().values()) {
            if (snode.getProcessor() == processorNum) {

                //create pane
                StackPane pane = new StackPane();
                double prefWidth = snode.getEndTime() - snode.getStartTime();
                pane.setMaxWidth((prefWidth * scale));
                double offset = snode.getStartTime();
                StackPane.setMargin(pane, new Insets(0, 0, 0, offset * scale));
                String style = "-fx-border-color: #" + color.toString().substring(2, 8) + "; -fx-border-width: 2px; -fx-background-radius: 4px; -fx-border-radius: 4px; -fx-background-color: #" + color.toString().substring(2, 8) + "66;";
                pane.setStyle(style);
                data.getChildren().add(pane);
                pane.setAlignment(Pos.CENTER);
                Text text = new Text(snode.getTask().getTaskName());
                Font font = Font.font("", FontWeight.BOLD, 12);
                text.setFont(font);
                text.setFill(Color.web("#fff"));

                //add hover events to task
                pane.setOnMouseEntered(e -> {
                    ApplicationState.getInstance().notifyTaskSelected(snode);
                    String hoverStyle = "-fx-border-color: #" + color.toString().substring(2, 8) + "; -fx-border-width: 2px; -fx-background-radius: 4px; -fx-border-radius: 4px; -fx-background-color: #" + color.toString().substring(2, 8) + "99;";
                    pane.setStyle(hoverStyle);
                });

                pane.setOnMouseExited(e -> {
                    ApplicationState.getInstance().notifyTaskSelected(null);
                    pane.setStyle(style);
                });

                pane.getChildren().add(text); 
            }
        }
    }
}