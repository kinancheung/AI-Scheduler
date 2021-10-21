package visualisation.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import data.ApplicationState;
import data.ProgressState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import visualisation.App;
import visualisation.util.Loader;

/*
    Author: Alex, Kirsty
    Results controls the Result info panel in the GUI
 */

public class Results {

    private Node nodes;

    @FXML
    private Label optimalTime;

    @FXML
    private Button restartBtn;

    @FXML
    private Label outputFilename;

    /**
     * initialize the result panel, updates the current optimal schedule time
     */
    @FXML
    void initialize() {
        optimalTime.setText("0");
        
        ApplicationState state = ApplicationState.getInstance();
        
        String output = state.getOutputFileName();
        outputFilename.setText(output);
        
        state.addOptimalScheduleObserver(schedule -> {
            optimalTime.setText(String.valueOf(schedule.getDuration()));
        });

        state.addProgressStateObserver(e -> {
            if(e == ProgressState.FINISHED){
                restartBtn.setDisable(false);                
            }
            if(e == ProgressState.RUNNING) {
                restartBtn.setDisable(true);
            }
        });    
        
    }

    /**
     * restart the application
     * @param event
     */
    @FXML
    void restartAction(ActionEvent event) {
        App.load();
        ApplicationState.getInstance().setRestartStateObserver(true);
    }


    @FXML
    void showOutput(ActionEvent event) {
        Path path = Paths.get(ApplicationState.getInstance().getOutputFileName());
        System.out.println(path);
    }


    /**
     * return the node of the Result panel
     * @return
     */
    public Node render() {
        return this.nodes;
    }

    /**
     * set up the nodes for Result panel
     * @param nodes
     */
    private void config(Node nodes) {
        this.nodes = nodes;
    }

    /**
     * constructor for the Result graph panel
     * @return
     */
    public static Results create() {

        FXMLLoader loader = Loader.manualLoad(Views.OPTIMALTIME.getPath());
        try {
            Node content = (Node) loader.load();
            Results controller = loader.getController();

            controller.config(content);

            return controller;
        } catch (IOException e) {
            return null;
        }
    }
}
