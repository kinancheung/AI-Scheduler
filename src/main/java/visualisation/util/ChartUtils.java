package visualisation.util;

/**
 * Author: Alex Nicholson
 * Date: 21/08/2021
 *
 * Utility functions for rendering the gantt chart
 */
public class ChartUtils {
    
    /**
     * Returns the nearest "nice" number to the duration for displaying the schedule
     * For example an input of 63 it will return 70
     */
    public static int getNearestWidth(int duration){
        int length = (int)(Math.log10(duration) + 1);
        double divisor = Math.pow(10, length - 1);
        double res = Math.ceil(duration / divisor);
        return (int) (res * divisor);
    }
}
