package visualisation.controller;

import data.ApplicationState;
import data.ProgressState;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import visualisation.App;
import visualisation.controller.timer.CustTimer;
import visualisation.util.Loader;

/**
 * Author Alex, Kirsty
 * Date: 22/08/08
 * AppController controls the overall GUI of the application
 */

public class AppController {

    @FXML
    private Text inputFileText;

    @FXML
    private Pane chartPane;

    @FXML
    private Pane inputGraphPane;

    @FXML
    private StackPane statisticsPane;


    @FXML
    private ImageView statusIcon;
    
    @FXML
    private Text statusText;
    
    @FXML
    private Text paraText;
    
    @FXML
    private StackPane resultsPanel;
    
    private RotateTransition rotateTransition;

    /**
     * Initialize the application by loading in different panels, creating the
     * gantt chart, starts the loading animation and controls the state of the
     * application.
     */
    @FXML
    void initialize() {

        rotateTransition = new RotateTransition();

        rotateTransition.setDuration(Duration.seconds(1.5));
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.setNode(statusIcon);

        rotateTransition.setByAngle(360);

        Chart chart = Chart.create();
        Results results = Results.create();
        InputGraphController inputGraph = InputGraphController.create();

        chartPane.getChildren().add(chart.render());
        inputGraphPane.getChildren().add(inputGraph.render());
        statisticsPane.getChildren().add(Loader.loadFXML(Views.STATS.getPath()));
        resultsPanel.getChildren().add(results.render());

        ApplicationState.getInstance().addRestartStateObserver(e -> {
            inputGraphPane.getChildren().add(Loader.loadFXML(Views.GRAPH.getPath()));
        });

        ApplicationState.getInstance().addProgressStateObserver((progressState) -> {
            Platform.runLater(() -> {
                setStatusIndicator(progressState);
                switch (progressState) {
                    case RUNNING:
                        CustTimer.getInstance().start();
                        break;
                    case FINISHED:
                        CustTimer.getInstance().stop();
                    default:
                        break;
                }
            });
        });

        inputFileText.setText(ApplicationState.getInstance().getInputFile());

    }

    /**
     * Starts or stops elements of the applications when its in different state.
     * 
     * @param state
     */
    private void setStatusIndicator(ProgressState state) {
        switch (state) {
            case RUNNING:
                Image progressImg = new Image(App.class.getResourceAsStream("images/status_progress.png"));
                statusIcon.setImage(progressImg);

                rotateTransition.play();
                statusText.setText("Running");
                statusText.setFill(Color.web("#49B4F0"));
                if (ApplicationState.getInstance().getParallelize()) {
                    paraText.setFill(Color.web("#7ed813"));
                }
                break;
            case FINISHED:
                Image finishedImg = new Image(App.class.getResourceAsStream("images/status_finished.png"));
                statusIcon.setImage(finishedImg);

                rotateTransition.stop();
                statusIcon.setRotate(0.0);
                statusText.setText("Finished");
                statusText.setFill(Color.web("#7ed813"));

                break;
            case ERROR:
                break;
            default:
                break;
        }

    }

}
