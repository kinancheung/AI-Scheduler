package visualisation.util;

import javafx.scene.paint.Color;

/**
 * Author: Alex Nicholson
 * Date: 22/08/2021
 *
 * Generates colours by picking equally spaced points along a gradient, used for the colouring of the nodes in the graph.
 */
public class ColourGenerator {

    // field to store number of colours to generate
    private int numColours;

    private static Color[] gradient = { Color.rgb(238, 41, 147), Color.rgb(233, 69, 36), Color.rgb(246, 197, 23) };

    public ColourGenerator(int number) {
        this.numColours = number;
    }

    public static void setGradient(Color[] newGradient) {
        gradient = newGradient;
    }

    /**
     * Calculates the color at a specific point along the gradient
     * 
     * @param pos the position along the gradient, as a double between 0 and 1
     */
    private Color colorAtPoint(double pos) {
        // Store colour components as double to avoid compound rounding errors
        double r = 0;
        double b = 0;
        double g = 0;

        for (int i = 0; i < gradient.length; i++) {
            double colorPos = i / (double) (gradient.length - 1);
            double distance = Math.abs(pos - colorPos);

            double weighting = Math.max(0, 1 - (distance / (1.0 / (gradient.length - 1))));

            r += ColourGenerator.gradient[i].getRed() * 255 * weighting;
            b += ColourGenerator.gradient[i].getBlue() * 255 * weighting;
            g += ColourGenerator.gradient[i].getGreen() * 255 * weighting;
        }

        return Color.rgb((int) r, (int) g, (int) b);
    }

    public Color getColor(int num) {       
        double pos = this.numColours == 1 ? 0.5 : (1 / (double) (this.numColours - 1)) * num;
        return colorAtPoint(pos);
    }
}
