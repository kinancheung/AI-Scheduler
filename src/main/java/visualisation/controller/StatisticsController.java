package visualisation.controller;
import algorithm.DependencyGraph;
import data.ApplicationState;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/*
    Author Kirsty, Alex

    Statistics Controller controls the Statistics panel
 */
public class StatisticsController {
    @FXML
    private Label tasksLabel;

    @FXML
    private Label memoryLabel;

    @FXML
    private Label processorsLabel;

    @FXML
    private Label timeLabel;

    /**
     * Initialise the controller and updates the timer
     */
    @FXML
    void initialize(){
        processorsLabel.setText(String.valueOf(ApplicationState.getInstance().getProcessorAmount()));
        tasksLabel.setText(String.valueOf(DependencyGraph.getDependencyGraph().getNodes().size()));
        setMemoryLabel();

        ApplicationState.getInstance().addTimerChangeObserver( time -> {
            timeLabel.setText(time);
        });

    }

    /**
     * Updates the memory label when it changes
     */
    public void setMemoryLabel(){
        Runtime runtime =Runtime.getRuntime();
        runtime.gc();

        new AnimationTimer(){
            @Override
            public void handle(long now) {
                ApplicationState.getInstance().addRestartStateObserver( restart -> {
                    memoryLabel.setText("00 GB");
                });
                int mb = 1024 * 1024*1024;
                String memory = String.format("%.2f GB",(double)runtime.totalMemory()/mb - runtime.freeMemory()/mb);
                memoryLabel.setText(memory);
            }
        }.start();
    }
}
