package visualisation.controller.timer;

import java.util.concurrent.TimeUnit;

import data.ApplicationState;
import javafx.animation.AnimationTimer;

/*
  Author: Kirsty
  Date: 22/08/21

  The CustTimer class is a customize Timer class which handles the timer for the application.
 */
public class CustTimer extends AnimationTimer {

    private static CustTimer singleCustTimer = null;
    long beginTime;
    long nano;
    long elapsedTime = 0;
    String timeString = "00:00.00";

    private CustTimer(){
        beginTime = System.nanoTime();
        nano = System.nanoTime();

    }

    public static CustTimer getInstance(){
        if (singleCustTimer == null){
            singleCustTimer = new CustTimer();
        }
        return singleCustTimer;
    }

    /**
     * It overrides the animationTimer handle method and calculates the current running time. It also triggers a changes
     * to the GUI when time changes.
     * @param now current time in nonosec
     */
    @Override
    public void handle(long now) {
        ApplicationState.getInstance().addRestartStateObserver( restart -> {
            beginTime = System.nanoTime();
        });
        elapsedTime = System.nanoTime() - beginTime;
        int minutes = (int) (TimeUnit.NANOSECONDS.toMinutes(elapsedTime));
        int seconds = (int) (TimeUnit.NANOSECONDS.toSeconds(elapsedTime) - TimeUnit.MINUTES.toSeconds(minutes));
        int seconds10 = (int)  (TimeUnit.NANOSECONDS.toMillis(elapsedTime) - TimeUnit.SECONDS.toMillis(seconds) - TimeUnit.MINUTES.toMillis(minutes));
        timeString = String.format("%02d:%02d.%03d", minutes, seconds, seconds10);
        ApplicationState.getInstance().setTimerChangeObserver(timeString);
    }

    /**
     * Start the timer
     */
    @Override
    public void start() {
        handle(System.nanoTime());
        super.start();
    }

    /**
     * Stop the timer
     */
    @Override
    public void stop(){
        super.stop();
    }

}
