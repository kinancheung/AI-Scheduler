package data;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.application.Platform;

/*
    Author: Benjamin Howard, Alex Nicholson
    Date: 08/08/2021

    This class stores the global application state from values passed in as command line arguments
 */

public class ApplicationState implements State {

    private static ApplicationState applicationState;

    private String inputFile;
    private int processorAmount;

    private Boolean visualise;
    private Boolean parallelize;
    private int parallelizeCount;
    private Boolean output;
    private String outputFileName;

    private List<Consumer<Schedule>> scheduleChangeObservers;
    private List<Consumer<ProgressState>> progressStateObservers;
    private List<Consumer<String>> timerChangeObserver;
    private List<Consumer<Boolean>> restartStateObserver;
    private List<Consumer<Integer>> zoomGraphObserver;
    private List<Consumer<ScheduledTaskNode>> TaskSelectedObservers;

    public static ApplicationState getInstance() {
        if (applicationState == null) {
            applicationState = new ApplicationState();
        }

        return applicationState;
    }


    private ApplicationState() {
        // default state information
        inputFile = "test2.dot";
        processorAmount = 2;
        visualise = false;
        parallelize = false;
        parallelizeCount = 0;
        output = false;
        outputFileName = "test2.dot";

        scheduleChangeObservers = new ArrayList<>();
        progressStateObservers = new ArrayList<>();
        timerChangeObserver = new ArrayList<>();
        restartStateObserver = new ArrayList<>();
        zoomGraphObserver = new ArrayList<>();
        TaskSelectedObservers = new ArrayList<>();
    }

    /**
     * @see State
     */
    public Boolean getVisualise() {
        return visualise;
    }

    /**
     * @see State
     */
    public void setVisualise(Boolean visualise) {
        this.visualise = visualise;
    }

    /**
     * @see State
     */
    public Boolean getParallelize() {
        return parallelize;
    }

    /**
     * @see State
     */
    public void setParallelize(Boolean parallelize) {
        this.parallelize = parallelize;
    }

    /**
     * @see State
     */
    public int getParallelizeCount() {
        return parallelizeCount;
    }

    /**
     * @see State
     */
    public void setParallelizeCount(int parallelizeCount) {
        this.parallelizeCount = parallelizeCount;
    }
    /**
     * @see State
     */
    public Boolean getOutput() {
        return output;
    }

    /**
     * @see State
     */
    public void setOutput(Boolean output) {
        this.output = output;
    }

    /**
     * @see State
     */
    public String getOutputFileName() {
        return outputFileName;
    }

    /**
     * @see State
     */
    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    /**
     * @see State
     */
    public String getInputFile() {
        return inputFile;
    }

    /**
     * @see State
     */
    public void setInputFile(String inputFile) {
        String filename = Paths.get(inputFile).getFileName().toString();

        this.inputFile = inputFile;
        this.outputFileName = filename.substring(0, filename.length() - 4) + "-output.dot";
    }

    /**
     * @see State
     */
    public int getProcessorAmount() {
        return processorAmount;
    }

    /**
     * @see State
     */
    public void setProcessorAmount(int processorAmount) {
        this.processorAmount = processorAmount;
    }

    /**
     * Set the optimalSchedule which observers observe
     * @param optimalSchedule
     */
    public void setOptimalSchedule(Schedule optimalSchedule) {
        if (this.visualise) {
            Platform.runLater(() -> {
                this.scheduleChangeObservers.forEach(observers -> {
                    observers.accept(optimalSchedule);
                });
            });
        }

    }

    /**
     * Add the observer for change in optimal schedule
     * @param observer
     */
    public void addOptimalScheduleObserver(Consumer<Schedule> observer) {
        this.scheduleChangeObservers.add(observer);
    }

    /**
     * Set the time which observers observe
     * @param time
     */
    public void setTimerChangeObserver(String time) {
        if (this.visualise) {
            Platform.runLater(() -> {
                this.timerChangeObserver.forEach(observers -> {
                    observers.accept(time);
                });
            });
        }
    }

    /**
     * Add the observers for time change
     * @param observer
     */
    public void addTimerChangeObserver(Consumer<String> observer) {
        this.timerChangeObserver.add(observer);
    }


    /**
     * Set the progress which observers observe
     * @param progressState
     */
    public void setProgressState(ProgressState progressState) {
        if (this.visualise) {
            Platform.runLater(() -> {
                this.progressStateObservers.forEach(observers -> {
                    observers.accept(progressState);
                });
            });
        }
    }

    /**
     * add observer which observes change in Progress state
     * @param observer
     */
    public void addProgressStateObserver(Consumer<ProgressState> observer) {
        this.progressStateObservers.add(observer);
    }

    /**
     * Set the restart state which observers observe
     * @param restart
     */
    public void setRestartStateObserver(Boolean restart) {
        this.restartStateObserver.forEach(observers -> {
            observers.accept(restart);
        });
    }

    /**
     * Add the observer which observe the changes in restart state
     * @param observer
     */
    public void addRestartStateObserver(Consumer<Boolean> observer) {
        this.restartStateObserver.add(observer);
    }

    /**
     * Set the edge width of the input graph to be watch by observer
     * @param edgeWidth
     */
    public void setZoomGraphObserver(int edgeWidth) {
        if (this.visualise) {
            Platform.runLater(() -> {
                this.zoomGraphObserver.forEach(observers -> {
                    observers.accept(edgeWidth);
                });
            });
        }
    }

    /**
     * Add the observer which observers the changes in edge width of the input graph
     * @param observer
     */
    public void addZoomGraphObserver(Consumer<Integer> observer) {
        this.zoomGraphObserver.add(observer);
    }

    /**
     * Add observer that obervers if a task if selected
     * @param observer
     */
    public void addTaskSelectedObserver(Consumer<ScheduledTaskNode> observer) {
        this.TaskSelectedObservers.add(observer);
    }

    /**
     * Set the task which will be notify observers when its selected
     * @param task
     */
    public void notifyTaskSelected(ScheduledTaskNode task) {
        if (this.visualise) {
            Platform.runLater(() -> {
                this.TaskSelectedObservers.forEach(observers -> {
                    observers.accept(task);
                });
            });
        }
    }

}
