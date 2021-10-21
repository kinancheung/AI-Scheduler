package visualisation;

import algorithm.Algorithm;
import algorithm.BranchAndBound;
import algorithm.BranchAndBoundParallel;
import algorithm.ListScheduleHeuristic;
import data.ApplicationState;
import data.Schedule;
import io.OutputWriter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import visualisation.util.Loader;

/**
 * Author: Alex Nicholson, Kirsty Gong
 * Date: 22/08/2021
 *
 * This class handles creating the JavaFX application and initialising the GUI, as well as beginning the algorithm
 */
public class App extends Application {

    private Stage stage;

    /**
     * The start method that is called by javaFX to start the application
     */
    @Override
    public void start(Stage s) {
        
        this.stage = s;
        this.stage.setMinWidth(1200);
        this.stage.setMinHeight(700);
        this.stage.setTitle("Visualisation");
        // this.stage.setResizable(false);
        
        // Set app icon
        s.getIcons().add(new Image(App.class.getResourceAsStream("images/favicon.png")));
        
        AnchorPane container = (AnchorPane) Loader.loadFXML("view/Container.fxml");
        
        Scene scene = new Scene(container);
        s.setScene(scene);

        EventHandler<WindowEvent> handleClose = new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        };

        s.setOnCloseRequest(handleClose);
                
        s.show();

        load();

    }

    /**
     * starts the algorithm calculation
     */
    public static void load(){
        new Thread(() -> {
            ListScheduleHeuristic list = ListScheduleHeuristic.getInstance();
            boolean isParallel = ApplicationState.getInstance().getParallelize();

            Algorithm branchAndBound = isParallel ? new BranchAndBoundParallel(list.schedule.getDuration(), list.schedule) : new BranchAndBound(list.schedule.getDuration(), list.schedule);
            Schedule opt = branchAndBound.BranchAndBoundRoutine();
            OutputWriter.writeOutputDot(opt);
        }).start();
    }
}
