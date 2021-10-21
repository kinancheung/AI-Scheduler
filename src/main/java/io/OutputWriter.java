package io;

import java.io.FileWriter;
import java.io.IOException;

import data.ApplicationState;
import data.Schedule;

/**
 * Author: Peter Goedeke
 * Class to write an output dot file from a schedule
 */
public class OutputWriter {
    /**
     * Take a schedule and create an output dot file from it
     * @param schedule the schedule to output
     */
    public static void writeOutputDot(Schedule schedule) {
        String outputFileName = ApplicationState.getInstance().getOutputFileName();
        try {
            FileWriter writer = new FileWriter(outputFileName);
            writer.write(schedule.toDotFile());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
